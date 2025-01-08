package vip.openpark.tcc.account.service.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;
import vip.openpark.tcc.account.domain.AccountDO;
import vip.openpark.tcc.account.mapper.AccountDOMapper;
import vip.openpark.tcc.account.service.ITccAccountService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author anthony
 * @version 2025/1/8
 * @since 2025/1/8 20:00
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TccAccountServiceImpl implements ITccAccountService {
	private static final Set<String> idempotentKeys = new HashSet<>();

	private final AccountDOMapper accountDOMapper;

	@Override
	@TwoPhaseBusinessAction(name = "prepare", commitMethod = "commit", rollbackMethod = "rollback")
	public boolean prepare(@BusinessActionContextParameter(paramName = "idempotentKey") String idempotentKey,
	                       @BusinessActionContextParameter(paramName = "userId") Long userId,
	                       @BusinessActionContextParameter(paramName = "amount") BigDecimal amount) {
		if (idempotentKeys.contains(idempotentKey)) {
			log.info("幂等key:{}已存在，无需重复创建", idempotentKey);
			return false;
		}

		AccountDO account = getAccount(userId);
		if (Objects.isNull(account)) {
			log.info("用户:{}不存在，创建用户", userId);
			return false;
		}

		if (account.getResidue().compareTo(amount) < 0) {
			log.info("用户:{}余额不足", userId);
			return false;
		}

		return true;
	}

	@Override
	public boolean commit(BusinessActionContext businessActionContext) {
		Object userId = businessActionContext.getActionContext("userId");
		Object amount = businessActionContext.getActionContext("amount");
		if (null == userId || null == amount) {
			return false;
		}

		String xid = businessActionContext.getXid();

		AccountDO account = getAccount(((Integer) userId).longValue());
		deduct(account, new BigDecimal((int) amount));

		if (accountDOMapper.updateByPrimaryKeySelective(account) == 1) {
			idempotentKeys.add(xid);
			return true;
		}

		return false;
	}

	@Override
	public boolean rollback(BusinessActionContext businessActionContext) {
		Object userId = businessActionContext.getActionContext("userId");
		Object amount = businessActionContext.getActionContext("amount");
		if (null == userId || null == amount) {
			return true;
		}
		String xid = businessActionContext.getXid();

		if (!idempotentKeys.contains(xid)) {
			log.info("幂等key:{}不存在，无需重复创建", xid);
			return true;
		}

		AccountDO account = getAccount((long) userId);
		deduct(account, ((BigDecimal) amount).negate());
		if (accountDOMapper.updateByPrimaryKeySelective(account) == 1) {
			idempotentKeys.remove(xid);
			return true;
		}

		return false;
	}

	private AccountDO getAccount(Long userId) {
		WeekendSqls<AccountDO> sql = WeekendSqls.custom();
		sql.andEqualTo(AccountDO::getUserId, userId);
		Example example = new Example.Builder(AccountDO.class).where(sql).build();
		return accountDOMapper.selectOneByExample(example);
	}

	private void deduct(AccountDO accountDO, BigDecimal amount) {
		accountDO.setUsed(accountDO.getUsed().add(amount));
		accountDO.setResidue(accountDO.getResidue().subtract(amount));
	}
}