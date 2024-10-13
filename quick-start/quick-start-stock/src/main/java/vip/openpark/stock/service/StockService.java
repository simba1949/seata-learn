package vip.openpark.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;
import vip.openpark.stock.domain.StockDO;
import vip.openpark.stock.mapper.StockDOMapper;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 19:49
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockService {
	private final StockDOMapper stockDOMapper;

	@Transactional
	public void deduct(Long productId, Integer quantity) {
		StockDO stockDO = getStockDO(productId);
		if (stockDO.getResidue() < quantity) {
			throw new RuntimeException("库存不足");
		}

		int oldUsed = stockDO.getUsed();
		int oldResidue = stockDO.getResidue();

		// 扣减库存
		StockDO stockDO4Update = deduct(stockDO, quantity);

		WeekendSqls<StockDO> sql4Update = WeekendSqls.custom();
		sql4Update.andEqualTo(StockDO::getProductId, productId);
		sql4Update.andEqualTo(StockDO::getUsed, oldUsed);
		sql4Update.andEqualTo(StockDO::getResidue, oldResidue);
		Example example4Update = new Example.Builder(StockDO.class).where(sql4Update).build();

		int affectNum = stockDOMapper.updateByExampleSelective(stockDO4Update, example4Update);
		if (affectNum != 1) {
			throw new RuntimeException("库存扣件失败");
		}
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
	 * @return 库存对象
	 */
	private StockDO deduct(StockDO stockDO, Integer quantity) {
		stockDO.setUsed(stockDO.getUsed() + quantity);
		stockDO.setResidue(stockDO.getResidue() - quantity);
		return stockDO;
	}
}