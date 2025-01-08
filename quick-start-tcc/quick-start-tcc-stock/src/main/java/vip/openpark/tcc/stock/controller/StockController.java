package vip.openpark.tcc.stock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.tcc.TccStockFacade;
import vip.openpark.tcc.stock.service.ITccStockService;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 14:47
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockController implements TccStockFacade {
	private final ITccStockService tccStockService;

	@Override
	@PostMapping("/stock/deduct")
	public Boolean deduct(@RequestParam(name = "idempotentKey") String idempotentKey,
	                      @RequestParam(name = "productId") Long productId,
	                      @RequestParam(name = "quantity") Integer quantity) {
		log.info("deduct productId:{}, quantity:{}", productId, quantity);

		try {
			return tccStockService.prepare(idempotentKey, productId, quantity);
		} catch (Exception e) {
			return false;
		}
	}
}