package vip.openpark.facade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 15:59
 */
@FeignClient(name = "quick-start-account")
public interface AccountFacade {
	/**
	 * 扣款
	 *
	 * @param userId 用户id
	 * @param amount 金额
	 * @return 扣款结果，true为扣款成功，false为扣款失败
	 */
	@PostMapping("/account/deduct")
	Boolean deduct(@RequestParam(name = "userId") Long userId,
	               @RequestParam(name = "amount") BigDecimal amount);
}