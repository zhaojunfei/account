package com.qianbao.account.service;

import java.util.Map;

import com.qianbao.account.model.AccountInfo;
import com.qianbao.account.model.AccountLog;
import com.qianbao.account.model.AccountRecord;
import com.qianbao.account.model.QueryAccountRecord;
/**
 * 
 * @author genghs
 *
 */
public interface AccountService {
	public AccountInfo checkAccount(AccountInfo accountInfo);
	
	public void insertAccountLog(String channelSystemNo,String channelSerialNo,String clientIP,
			String tranType,String requestMessage)throws Exception;
	
	public void updateAccountLog(String channelSystemNo,String channelSerialNo,
			String returnCode,String returnMessage,String responseMessage);
	
	public AccountInfo createAccount(AccountInfo accountInfo) throws Exception;
	
	public AccountInfo queryAccount(AccountInfo accountInfo) throws Exception;
	
	public void freezeAccount(AccountInfo accountInfo) throws Exception;
	
	public void unFreezeAccount(AccountInfo accountInfo) throws Exception;

	public void fundIn(AccountRecord accountRecord) throws Exception;
	
	public void fundOut(AccountRecord accountRecord) throws Exception;

	public void transfer(AccountRecord accountRecord) throws Exception;

	public Map<String, Object> queryAccountRecord(QueryAccountRecord queryAccountRecord) throws Exception;
	
	public AccountLog queryAccountLog(AccountLog accountLog) throws Exception;
	
	public AccountRecord getAccountRecord(AccountLog accountLog) throws Exception;
	
	public void updateReversalAccountLog(String channelSystemNo,String channelSerialNo,String originalSerialNo);
}
