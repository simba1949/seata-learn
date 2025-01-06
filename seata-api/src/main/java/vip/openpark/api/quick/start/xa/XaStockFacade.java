package vip.openpark.api.quick.start.xa;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author anthony
 * @version 2025/01/06
 * @since 2025/01/06 13:35
 */
@FeignClient(name = "quick-start-xa-stock")
public interface XaStockFacade {
	/**
	 * 扣减库存
	 *
	 * @param productId 商品id
	 * @param quantity  扣减数量
	 * @return 扣减结果，true 表示成功，false 表示失败
	 */
	@PostMapping("/stock/deduct")
	Boolean deduct(@RequestParam(name = "productId") Long productId,
	               @RequestParam(name = "quantity") Integer quantity);
}