package vip.openpark.tcc.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.tcc.TccOrderFacade;
import vip.openpark.tcc.order.service.TccOrderService;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 15:04
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController implements TccOrderFacade {
	private final TccOrderService tccOrderService;

	@Override
	@PostMapping("/order/create")
	public Boolean createOrder(@RequestParam(name = "userId") Long userId,
	                           @RequestParam(name = "productId") Long productId,
	                           @RequestParam(name = "quantity") Integer quantity) {
		log.info("userId:{}, productId:{}, quantity:{}", userId, productId, quantity);
		try {
			tccOrderService.createOrder(userId, productId, quantity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}