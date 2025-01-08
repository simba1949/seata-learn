package vip.openpark.tcc.stock.service.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;
import vip.openpark.tcc.stock.domain.StockDO;
import vip.openpark.tcc.stock.mapper.StockDOMapper;
import vip.openpark.tcc.stock.service.ITccStockService;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author anthony
 * @version 2025/1/8
 * @since 2025/1/8 21:28
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TccStockServiceImpl implements ITccStockService {
	private static final Set<String> idempotentKeys = new HashSet<>();

	private final StockDOMapper stockDOMapper;

	@Override
	@TwoPhaseBusinessAction(name = "prepare", commitMethod = "commit", rollbackMethod = "rollback")
	public boolean prepare(@BusinessActionContextParameter(paramName = "idempotentKey") String idempotentKey,
	                       @BusinessActionContextParameter(paramName = "productId") Long productId,
	                       @BusinessActionContextParameter(paramName = "count") Integer count) {
		if (idempotentKeys.contains(idempotentKey)) {
			log.info("幂等key:{}已存在，无需重复创建", idempotentKey);
			return false;
		}

		StockDO stockDO = getStockDO(productId);
		if (Objects.isNull(stockDO)) {
			log.info("商品:{}不存在，创建商品", productId);
			return false;
		}

		if (stockDO.getResidue().compareTo(count) < 0) {
			log.info("商品:{}库存不足", productId);
			return false;
		}

		return true;
	}

	@Override
	@Transactional
	public boolean commit(BusinessActionContext businessActionContext) {
		Object productId = businessActionContext.getActionContext("productId");
		Object count = businessActionContext.getActionContext("count");

		if (null == productId || null == count) {
			return false;
		}

		int productId1 = (int) productId;
		StockDO stockDO = getStockDO((long) productId1);
		deduct(stockDO, (int) count);
		if (stockDOMapper.updateByPrimaryKeySelective(stockDO) == 1) {
			idempotentKeys.add(businessActionContext.getXid());
			return true;
		}
		return false;
	}

	@Override
	public boolean rollback(BusinessActionContext businessActionContext) {
		Object productId = businessActionContext.getActionContext("productId");
		Object count = businessActionContext.getActionContext("count");

		if (null == productId || null == count) {
			return true;
		}

		StockDO stockDO = getStockDO((long) productId);
		deduct(stockDO, -(int) count);
		if (stockDOMapper.updateByPrimaryKeySelective(stockDO) == 1) {
			idempotentKeys.remove(businessActionContext.getXid());
			return true;
		}
		return false;
	}

	private StockDO getStockDO(Long productId) {
		WeekendSqls<StockDO> sql4Get = WeekendSqls.custom();
		sql4Get.andEqualTo(StockDO::getProductId, productId);
		Example example4Get = new Example.Builder(StockDO.class).where(sql4Get).build();
		return stockDOMapper.selectOneByExample(example4Get);
	}

	/**
	 * 内存中扣减库存
	 *
	 * @param stockDO  库存对象
	 * @param quantity 扣减数量
	 */
	private void deduct(StockDO stockDO, Integer quantity) {
		stockDO.setUsed(stockDO.getUsed() + quantity);
		stockDO.setResidue(stockDO.getResidue() - quantity);
	}
}