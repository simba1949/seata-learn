package vip.openpark.xa.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;
import vip.openpark.xa.account.domain.AccountDO;
import vip.openpark.xa.account.mapper.AccountDOMapper;

import java.math.BigDecimal;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 15:03
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {
	private final AccountDOMapper accountDOMapper;

	@Transactional
	public void deduct(Long userId, BigDecimal amount) {
		AccountDO accountDO = getAccount(userId);
		if (accountDO == null){
			throw new RuntimeException("用户不存在");
		}
		if (accountDO.getResidue().compareTo(amount) < 0){
			throw new RuntimeException("余额不足");
		}

		BigDecimal oldResidue = accountDO.getResidue();
		BigDecimal oldUsed = accountDO.getUsed();
		WeekendSqls<AccountDO> sql4Update = WeekendSqls.custom();
		sql4Update.andEqualTo(AccountDO::getUserId, userId);
		sql4Update.andEqualTo(AccountDO::getResidue, oldResidue);
		sql4Update.andEqualTo(AccountDO::getUsed, oldUsed);
		Example example4Update = new Example.Builder(AccountDO.class).where(sql4Update).build();

		AccountDO willUpdate = deduct(accountDO, amount);
		int affectNum = accountDOMapper.updateByExampleSelective(willUpdate, example4Update);
		if (affectNum != 1){
			throw new RuntimeException("并发修改异常");
		}
	}

	private AccountDO getAccount(Long userId) {
		WeekendSqls<AccountDO> sql = WeekendSqls.custom();
		sql.andEqualTo(AccountDO::getUserId, userId);
		Example example = new Example.Builder(AccountDO.class).where(sql).build();
		return accountDOMapper.selectOneByExample(example);
	}

	private AccountDO deduct(AccountDO accountDO, BigDecimal amount){
		AccountDO willUpdate = new AccountDO();
		willUpdate.setUsed(accountDO.getUsed().add(amount));
		willUpdate.setResidue(accountDO.getResidue().subtract(amount));
		return willUpdate;
	}
}