package vip.openpark.tcc.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.tcc.TccAccountFacade;
import vip.openpark.tcc.account.service.ITccAccountService;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 19:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController implements TccAccountFacade {
	private final ITccAccountService itccAccountService;

	@Override
	@PostMapping("/account/deduct")
	public Boolean deduct(@RequestParam(name = "userId") String idempotentKey,
	                      @RequestParam(name = "userId") Long userId,
	                      @RequestParam(name = "amount") BigDecimal amount) {
		log.info("deduct: userId: {}, amount: {}", userId, amount);
		try {
			return itccAccountService.prepare(idempotentKey, userId, amount);
		} catch (Exception e) {
			return false;
		}
	}
}