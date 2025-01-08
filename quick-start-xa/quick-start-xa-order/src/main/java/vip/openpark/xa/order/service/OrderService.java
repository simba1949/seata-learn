package vip.openpark.xa.order.service;

import io.seata.core.context.RootContext;
import io.seata.core.model.BranchType;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;
import vip.openpark.api.quick.start.xa.XaAccountFacade;
import vip.openpark.api.quick.start.xa.XaStockFacade;
import vip.openpark.xa.order.domain.OrderDO;
import vip.openpark.xa.order.mapper.OrderDOMapper;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 15:05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {
	private final OrderDOMapper orderDOMapper;

	private final XaStockFacade xaStockFacade;
	private final XaAccountFacade xaAccountFacade;

	@GlobalTransactional(name = "xa-order-stock-account", rollbackFor = Exception.class)
	public void createOrder(Long userId, Long productId, Integer quantity) {
		// 获取当前全局事务id（重要，但是可忽略）
		String xid = RootContext.getXID();
		log.info("全局事务id:{}", xid);
		BranchType branchType = RootContext.getBranchType();
		log.info("当前分支事务类型:{}", branchType);

		log.info("开始创建订单");
		OrderDO orderDO = buildOrder(userId, productId, quantity, xid);
		int affectNum = orderDOMapper.insertSelective(orderDO);
		if (affectNum != 1) {
			throw new RuntimeException("创建订单失败");
		}
		log.info("创建订单完成");

		log.info("开始扣减库存");
		Boolean deduct = xaStockFacade.deduct(productId, quantity);
		log.info("扣减库存结果:{}", deduct);
		if (!deduct) {
			log.info("扣减库存失败，订单回滚");
			throw new RuntimeException("扣减库存失败");
		}

		log.info("开始扣减余额");
		Boolean deduct1 = xaAccountFacade.deduct(userId, BigDecimal.TEN);
		log.info("扣减余额结果:{}", deduct1);
		if (!deduct1) {
			log.info("扣减余额失败，订单回滚");
			throw new RuntimeException("扣减余额失败");
		}

		// XA 是强一致性，不支持可重入
//		log.info("开始修改订单状态");
//		updateOrderStatus(orderDO);
//		log.info("修改订单状态完成");
	}

	private OrderDO buildOrder(Long userId, Long productId, Integer quantity, String code) {
		OrderDO orderDO = new OrderDO();
		orderDO.setCode(code);
		orderDO.setUserId(userId);
		orderDO.setProductId(productId);
		orderDO.setCount(quantity);
		BigDecimal multiply = BigDecimal.TEN.multiply(new BigDecimal(quantity));
		orderDO.setMoney(multiply);
		orderDO.setStatus((byte) 1); // 订单状态，0表示创建中，1表示已完结

		return orderDO;
	}

	private void updateOrderStatus(OrderDO orderDO) {
		WeekendSqls<OrderDO> sql = WeekendSqls.custom();
		sql.andEqualTo(OrderDO::getId, orderDO.getId());
		sql.andEqualTo(OrderDO::getStatus, (byte) 0);
		Example example = new Example.Builder(OrderDO.class).where(sql).build();

		OrderDO willUpdate = new OrderDO();
		willUpdate.setStatus((byte) 1);

		int affectNum = orderDOMapper.updateByExampleSelective(willUpdate, example);
		if (affectNum != 1) {
			throw new RuntimeException("更新订单状态失败");
		}
	}
}