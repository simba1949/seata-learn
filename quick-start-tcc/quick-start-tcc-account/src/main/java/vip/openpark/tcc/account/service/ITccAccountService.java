package vip.openpark.tcc.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2025/1/8
 * @since 2025/1/8 19:59
 */
@LocalTCC
public interface ITccAccountService {
	/**
	 * 【注意】实现类也需要使用 @TwoPhaseBusinessAction 和 @TwoPhaseBusinessAction 注解进行修饰，否则会获取不到参数值
	 * Prepare 逻辑，@TwoPhaseBusinessAction 用于指定 Commit 逻辑，方法名称要和 @TwoPhaseBusinessAction 中的 commitMethod 属性值一致
	 *
	 * @param idempotentKey 幂等键
	 * @param userId        用户id
	 * @param amount        金额
	 * @return boolean
	 */
	@TwoPhaseBusinessAction(name = "prepare", commitMethod = "commit", rollbackMethod = "rollback")
	boolean prepare(@BusinessActionContextParameter(paramName = "idempotentKey") String idempotentKey,
	                @BusinessActionContextParameter(paramName = "userId") Long userId,
	                @BusinessActionContextParameter(paramName = "amount") BigDecimal amount);

	/**
	 * Commit 逻辑，@TwoPhaseBusinessAction 用于指定 Commit 逻辑，方法名称要和 @TwoPhaseBusinessAction 中的 commitMethod 属性值一致
	 * 提交逻辑
	 *
	 * @param businessActionContext 上下文，可以获取到 Try 逻辑中 BusinessActionContextParameter 注解标记的参数值
	 * @return boolean
	 */
	boolean commit(BusinessActionContext businessActionContext);

	/**
	 * Rollback 逻辑，@TwoPhaseBusinessAction 用于指定 Rollback 逻辑，方法名称要和 @TwoPhaseBusinessAction 中的 rollbackMethod 属性值一致
	 * 回滚逻辑
	 *
	 * @param businessActionContext 上下文，可以获取到 Try 逻辑中 BusinessActionContextParameter 注解标记的参数值
	 * @return boolean
	 */
	boolean rollback(BusinessActionContext businessActionContext);
}