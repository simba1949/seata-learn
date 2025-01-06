package vip.openpark.xa.stock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.xa.XaStockFacade;
import vip.openpark.xa.stock.service.StockService;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 14:47
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockController implements XaStockFacade {
	private final StockService stockService;

	@Override
	@PostMapping("/stock/deduct")
	public Boolean deduct(Long productId, Integer quantity) {
		log.info("deduct productId:{}, quantity:{}", productId, quantity);

		try {
			stockService.deduct(productId, quantity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}