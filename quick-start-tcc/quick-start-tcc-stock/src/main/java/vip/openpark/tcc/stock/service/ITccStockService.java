package vip.openpark.tcc.stock.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author anthony
 * @version 2025/1/8
 * @since 2025/1/8 21:26
 */
@LocalTCC
public interface ITccStockService {
	@TwoPhaseBusinessAction(name = "prepare", commitMethod = "commit", rollbackMethod = "rollback")
	boolean prepare(@BusinessActionContextParameter(paramName = "idempotentKey") String idempotentKey,
	                @BusinessActionContextParameter(paramName = "productId") Long productId,
	                @BusinessActionContextParameter(paramName = "count") Integer count);

	boolean commit(BusinessActionContext businessActionContext);

	boolean rollback(BusinessActionContext businessActionContext);
}