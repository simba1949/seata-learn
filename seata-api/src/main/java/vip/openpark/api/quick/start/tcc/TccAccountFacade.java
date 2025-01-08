package vip.openpark.api.quick.start.tcc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2025/01/06
 * @since 2025/01/06 13:35
 */
@FeignClient(name = "quick-start-tcc-account")
public interface TccAccountFacade {
	/**
	 * 扣款
	 *
	 * @param idempotentKey 幂等key
	 * @param userId        用户id
	 * @param amount        金额
	 * @return 扣款结果，true为扣款成功，false为扣款失败
	 */
	@PostMapping("/account/deduct")
	Boolean deduct(@RequestParam(name = "idempotentKey") String idempotentKey,
	               @RequestParam(name = "userId") Long userId,
	               @RequestParam(name = "amount") BigDecimal amount);
}