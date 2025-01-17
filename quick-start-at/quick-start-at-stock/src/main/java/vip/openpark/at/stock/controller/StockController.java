package vip.openpark.at.stock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.at.AtStockFacade;
import vip.openpark.at.stock.service.StockService;

import java.util.concurrent.TimeUnit;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 19:46
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockController implements AtStockFacade {
	private final StockService stockService;

	@Override
	@PostMapping("/stock/deduct")
	public Boolean deduct(@RequestParam(name = "productId") Long productId,
	                      @RequestParam(name = "quantity") Integer quantity) {
		log.info("deduct productId:{}, quantity:{}", productId, quantity);

		try {
			stockService.deduct(productId, quantity);

			TimeUnit.SECONDS.sleep(5);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}