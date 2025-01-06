package vip.openpark.at.order.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.at.AtOrderFacade;
import vip.openpark.at.order.service.OrderService;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 16:47
 */
@Slf4j
@RestController
public class OrderController implements AtOrderFacade {
	@Resource
	private OrderService orderService;

	@Override
	@PostMapping("/order/create")
	public Boolean createOrder(@RequestParam(name = "userId") Long userId,
	                           @RequestParam(name = "productId") Long productId,
	                           @RequestParam(name = "quantity") Integer quantity) {
		log.info("userId:{}, productId:{}, quantity:{}", userId, productId, quantity);
		try {
			orderService.createOrder(userId, productId, quantity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}