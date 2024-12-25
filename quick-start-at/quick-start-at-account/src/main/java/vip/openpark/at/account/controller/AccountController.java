package vip.openpark.at.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.at.account.service.AccountService;
import vip.openpark.api.AccountFacade;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 19:57
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController implements AccountFacade {
	private final AccountService accountService;

	@Override
	@PostMapping("/account/deduct")
	public Boolean deduct(@RequestParam(name = "userId") Long userId,
	                      @RequestParam(name = "amount") BigDecimal amount) {
		log.info("deduct: userId: {}, amount: {}", userId, amount);
		try {
			accountService.deduct(userId, amount);

			TimeUnit.SECONDS.sleep(5);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
