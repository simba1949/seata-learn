package vip.openpark.api.quick.start.tcc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author anthony
 * @version 2025/01/06
 * @since 2025/01/06 13:35
 */
public interface TccOrderFacade {
	/**
	 * 创建订单
	 * @param userId 创建人
	 * @param productId 商品id
	 * @param quantity 购买数量
	 * @return 创建结果
	 */
	@PostMapping("/order/create")
	Boolean createOrder(@RequestParam(name = "userId") Long userId,
	                    @RequestParam(name = "productId") Long productId,
	                    @RequestParam(name = "quantity") Integer quantity);
}