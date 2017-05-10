package com.qianbao.mapper;

import java.util.List;
import java.util.Map;

import com.qianbao.account.model.AccountInfo;
import com.qianbao.account.model.AccountLog;
import com.qianbao.account.model.AccountRecord;
/**
 * 
 * @author genghs
 *
 */
public interface AccountMapper {
	/*
	 * 账户开户
	 */
	public int createAccount(AccountInfo accountInfo);
	
	/*
	 * 账户查询
	 */
	public AccountInfo queryAccount(String accountNo);
	
	/*
	 * 冻结账户
	 */
	public int freezeAccount(AccountInfo accountInfo);
	
	/*
	 * 解冻账户
	 */
	public int unFreezeAccount(AccountInfo accountInfo);
	/*
	 * 账户充值或提现
	 */
	public int updateAccount(AccountInfo accountInfo);
	/*
	 * 记录明细账
	 */
	public int insertAccountRecord(AccountRecord accountRecord);
	/*
	 * 校验账号户名
	 */
	public AccountInfo checkAccount(AccountInfo accountInfo);
	/*
	 * 查询账户交易记录总数
	 */
	public int countAccountRecord(Map<String,Object> params);
	/*
	 * 查询账户交易记录明细
	 */
	public List<AccountRecord> queryAccountRecord(Map<String,Object> params);
	/*
	 * 校验系统编号和流水号
	 */
	public int checkAccessNo(Map<String,Object> params);
	/*
	 * 记录日志信息
	 */
	public int insertAccountLog(Map<String,Object> params);
	/*
	 * 更新日志信息
	 */
	public int updateAccountLog(Map<String,Object> params);
	/*
	 * 查询日志信息
	 */
	public AccountLog queryAccountLog(AccountLog accountLog);
	/*
	 * 根据请求流水号查询账户信息
	 */
	public AccountInfo queryAccountByLog(AccountLog accountLog);
	/*
	 * 查询账户交易记录
	 */
	public AccountRecord getAccountRecord(AccountLog accountLog);
	/*
	 * 冲正成功后更新原交易日志信息
	 */
	public int updateReversalAccountLog(Map<String,Object> params);
}
