package vip.openpark.xa.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.openpark.api.quick.start.xa.XaAccountFacade;
import vip.openpark.xa.account.service.AccountService;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 15:02
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController implements XaAccountFacade {
	private final AccountService accountService;
	@Override
	@PostMapping("/account/deduct")
	public Boolean deduct(Long userId, BigDecimal amount) {
		log.info("deduct: userId: {}, amount: {}", userId, amount);
		try {
			accountService.deduct(userId, amount);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}