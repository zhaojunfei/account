package com.qianbao.account.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qianbao.account.exception.ExceptionBox;
import com.qianbao.account.exception.BusinessException;
import com.qianbao.account.model.AccountInfo;
import com.qianbao.account.model.AccountLog;
import com.qianbao.account.model.AccountRecord;
import com.qianbao.account.model.QueryAccountRecord;
import com.qianbao.account.util.Standard;
import com.qianbao.mapper.AccountMapper;
/**
 * 
 * @author genghs
 *
 */
@Service
public class AccountServiceImpl implements AccountService {
	//日期记录器
	private static Logger logger= LogManager.getLogger(AccountServiceImpl.class);
	@Autowired
	private AccountMapper accountInfoMapper;
	
	@Resource
	private HashMap<String,String> allowAccessSystem;
	/*
	 * 判断渠道系统号是否合法，渠道ip与登记ip是否一致
	 */
	private void checkAccess(String channelSystemNo,String clientIP)throws BusinessException{
		if(Standard.nvl(channelSystemNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[channelSystemNo]校验失败");
		}
		//判断渠道系统编号是否合法，系统请求IP是否合法
		logger.info("开始校验渠道访问权限，渠道IP："+clientIP);
		if(!(","+allowAccessSystem.get(channelSystemNo)+",").contains(","+clientIP+",")){
			throw ExceptionBox.CHANNEL_SYSTEMNO_INVALID;
		}
	}
	/*
	 * 判断渠道流水号是否重复
	 */
	private void checkSerialNo(String channelSystemNo,String channelSerialNo)throws BusinessException{
		if(Standard.nvl(channelSystemNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[channelSystemNo]校验失败");
		}
		if(Standard.nvl(channelSerialNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[channelSerialNo]校验失败");	
		}
		//判断渠道流水号是否重复
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("channelSystemNo", channelSystemNo);
		params.put("channelSerialNo", channelSerialNo);
		if(accountInfoMapper.checkAccessNo(params)>0){
			throw ExceptionBox.CHANNEL_SERIALNO_INVALID;
		}
	}
	/*
	 * 账户记账
	 */
	private void keepAccount(AccountRecord accountRecord)throws Exception {
		String tranType=accountRecord.getTranType();
		String accountNo=accountRecord.getAccountNo();
		String amount=accountRecord.getAmount();
		if(Standard.nvl(accountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountNo]校验失败");
		}
		if(Standard.nvl(amount)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");	
		}
		BigDecimal amountDecimal;
		try{
			amountDecimal=new BigDecimal(amount);
		}catch(NumberFormatException e){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
		}
		//判断金额是否合法，金额大于零
		if(amountDecimal.compareTo(new BigDecimal(0))!=1){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
		}
		AccountInfo accountInfo=accountInfoMapper.queryAccount(accountNo);
		if(accountInfo==null){
			throw ExceptionBox.ACCOUNT_NOT_FOUND;
		}
		BigDecimal balanceDecimal=new BigDecimal(accountInfo.getBalance());
		logger.info("账户【"+accountNo+"】记账前余额:"+balanceDecimal);
		if(tranType.charAt(0)=='0'){
			BigDecimal balanceFrozenDecimal=new BigDecimal(accountInfo.getBalanceFrozen());
			//获取可用余额
			BigDecimal balanceAvailableDecimal=balanceDecimal.subtract(balanceFrozenDecimal);
			//判断取出金额是否大于可用余额
			if(amountDecimal.compareTo(balanceAvailableDecimal)==1){
				throw ExceptionBox.ACCOUNT_NOT_ENOUGH;
			}		
			amountDecimal=amountDecimal.negate();
		}
		logger.info("账户【"+accountNo+"】记账金额:"+amountDecimal);
		accountInfo.setAmount(amountDecimal.toString());
		int result=accountInfoMapper.updateAccount(accountInfo);
		if(result==0){
			throw ExceptionBox.OPERATION_FAIL;
		}
		String balanceAfterOperation=balanceDecimal.add(amountDecimal).toString();
		
		accountRecord.setAmount(amountDecimal.toString());	
		accountRecord.setBalance(balanceAfterOperation);
		result=accountInfoMapper.insertAccountRecord(accountRecord);
		if(result==0){
			throw ExceptionBox.OPERATION_FAIL;
		}
		logger.info("账户【"+accountNo+"】记账成功，记账后余额:"+balanceAfterOperation);
	}
	/*
	 * 检查账户号,账户名是否正确
	 */
	public AccountInfo checkAccount(AccountInfo accountInfo){
		return accountInfoMapper.checkAccount(accountInfo);
	}
	/**
	 * 插入账户服务日志
	 * @param channelSystemNo
	 * @param channelSerialNo
	 * @param tranType
	 * @param requestMessage
	 * @throws BusinessException
	 */
	public void insertAccountLog(String channelSystemNo,String channelSerialNo,String clientIP,String tranType,String requestMessage)throws BusinessException{
		checkAccess(channelSystemNo,clientIP);
		checkSerialNo(channelSystemNo,channelSerialNo);
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("channelSystemNo", channelSystemNo);
		params.put("channelSerialNo", channelSerialNo);
		params.put("tranType", tranType);
		params.put("requestMessage", requestMessage);
		accountInfoMapper.insertAccountLog(params);
	}
	/**
	 * 更新账户服务日志
	 * @param channelSystemNo
	 * @param channelSerialNo
	 * @param returnCode
	 * @param returnMessage
	 * @param responseMessage
	 * @throws BusinessException
	 */
	public void updateAccountLog(String channelSystemNo,String channelSerialNo,String returnCode,String returnMessage,String responseMessage){

		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("channelSystemNo", channelSystemNo);
		params.put("channelSerialNo", channelSerialNo);
		params.put("returnCode", returnCode);
		params.put("returnMessage", returnMessage);
		params.put("responseMessage", responseMessage);
		accountInfoMapper.updateAccountLog(params);
	}

	/**
	 * 创建账户
	 * @throws BusinessException 
	 */
	public AccountInfo createAccount(AccountInfo accountInfo) throws BusinessException{
		
		if(Standard.nvl(accountInfo.getUserNo())){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[userNo]校验失败");
		}
		if(Standard.nvl(accountInfo.getAccountName())){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountName]校验失败");		
		}
		int result=accountInfoMapper.createAccount(accountInfo);
		if(result==0){
			throw ExceptionBox.OPERATION_FAIL;
		}
		
		return accountInfo;



		
	}
	/**
	 * 查询账户
	 * @throws BusinessException 
	 */
	public AccountInfo queryAccount(AccountInfo accountInfo) throws BusinessException{
		String accountNo=accountInfo.getAccountNo();
		checkAccess(accountInfo.getChannelSystemNo(),accountInfo.getClientIP());
		if(Standard.nvl(accountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountNo]校验失败");	
		}
		accountInfo = accountInfoMapper.queryAccount(accountNo);
		if(accountInfo==null){
			throw ExceptionBox.ACCOUNT_NOT_FOUND;
		}	

		
		return accountInfo;


	}
	/**
	 * 查询账户交易明细
	 * @throws BusinessException 
	 */
	public Map<String, Object> queryAccountRecord(QueryAccountRecord queryAccountRecord) throws BusinessException{
		Map<String,Object> model=new HashMap<String,Object>();
		String accountNo=queryAccountRecord.getAccountNo();
		String startDate=queryAccountRecord.getStartDate();
		String endDate=queryAccountRecord.getEndDate();
		String page=queryAccountRecord.getPage();
		String pageShowNum=queryAccountRecord.getPageShowNum();
		
		checkAccess(queryAccountRecord.getChannelSystemNo(),queryAccountRecord.getClientIP());
		if(Standard.nvl(accountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountNo]校验失败");
		}
		if(Standard.nvl(startDate)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[startDate]校验失败");
		}
		if(Standard.nvl(endDate)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[endDate]校验失败");
		}
		if(Standard.nvl(page)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[page]校验失败");	
		}
		if(Standard.nvl(pageShowNum)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[pageShowNum]校验失败");	
		}
		if(startDate.trim().length()!=8){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[startDate]校验失败");
		}
		if(endDate.trim().length()!=8){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[endDate]校验失败");
		}
		int pageInt,pageShowNumInt;
		try{
			pageInt=Integer.valueOf(page).intValue();
		}catch(Exception e){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[page]校验失败");
		}
		try{
			pageShowNumInt=Integer.valueOf(pageShowNum).intValue();
		}catch(Exception e){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[pageShowNum]校验失败");
		}
		int startPos=pageShowNumInt*(pageInt-1);
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("accountNo", accountNo);
		params.put("startTime", startDate+"000000");
		params.put("endTime", endDate+"240000");
		params.put("endTime", endDate+"240000");
		params.put("startPos",startPos);
		params.put("pageShowNum",pageShowNumInt);
		int totalNum=accountInfoMapper.countAccountRecord(params);
		
		model.put("totalNum", totalNum);
		if(totalNum>0){
			List<AccountRecord> accountRecordList=accountInfoMapper.queryAccountRecord(params);
			model.put("accountRecordList", accountRecordList);
		}
		
		return model;

	}
	/**
	 * 冻结账户余额
	 * @throws BusinessException 
	 */
	public void freezeAccount(AccountInfo accountInfo) throws BusinessException{
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String accountNo=accountInfo.getAccountNo();
		String freezeAmount=accountInfo.getAmount();
		

		if(Standard.nvl(accountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountNo]校验失败");	
		}
		if(Standard.nvl(freezeAmount)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");	
		}
		BigDecimal freezeAmountDecimal;
		try{
			freezeAmountDecimal=new BigDecimal(freezeAmount);
		}catch(NumberFormatException e){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
		}
		//判断冻结金额是否合法，大于零
		if(freezeAmountDecimal.compareTo(new BigDecimal(0))!=1){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
		}
		
		accountInfo=accountInfoMapper.queryAccount(accountNo);
		if(accountInfo==null){
			throw ExceptionBox.ACCOUNT_NOT_FOUND;
		}
		BigDecimal balanceDecimal=new BigDecimal(accountInfo.getBalance());
		BigDecimal balanceFrozenDecimal=new BigDecimal(accountInfo.getBalanceFrozen());
		//判断可用余额大于等于冻结金额
		if(balanceDecimal.subtract(balanceFrozenDecimal).compareTo(freezeAmountDecimal)==-1){
			throw ExceptionBox.ACCOUNT_NOT_ENOUGH;
		}
		accountInfo.setAmount(freezeAmount);
		int result=accountInfoMapper.freezeAccount(accountInfo);
		if(result==0){
			throw ExceptionBox.OPERATION_FAIL;
		}
	}
	/**
	 * 解冻账户余额
	 * @throws BusinessException 
	 */
	public void unFreezeAccount(AccountInfo accountInfo) throws BusinessException {
		
		String accountNo=accountInfo.getAccountNo();
		String unFreezeAmount=accountInfo.getAmount();

		if(Standard.nvl(accountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountNo]校验失败");	
		}
		if(Standard.nvl(unFreezeAmount)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");	
		}
		BigDecimal unFreezeAmountDecimal;
		try{
			unFreezeAmountDecimal=new BigDecimal(unFreezeAmount);
		}catch(NumberFormatException e){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
		}
		//判断金额是否合法，金额大于零
		if(unFreezeAmountDecimal.compareTo(new BigDecimal(0))!=1){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
		}
		
		
		accountInfo=accountInfoMapper.queryAccount(accountNo);
		if(accountInfo==null){
			throw ExceptionBox.ACCOUNT_NOT_FOUND;
		}
		
		BigDecimal balanceFrozenDecimal=new BigDecimal(accountInfo.getBalanceFrozen());
		//判断已冻结余额大于等于解冻金额
		if(balanceFrozenDecimal.compareTo(unFreezeAmountDecimal)==-1){
			throw ExceptionBox.ACCOUNT_NOT_ENOUGH;
		}
		accountInfo.setAmount(unFreezeAmount);
		int result=accountInfoMapper.unFreezeAccount(accountInfo);
		if(result==0){
			throw ExceptionBox.OPERATION_FAIL;
		}

	}


	/**
	 * 账户充值
	 * @throws BusinessException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void fundIn(AccountRecord accountRecord) throws Exception{
		accountRecord.setTranType("11");
		keepAccount(accountRecord);
					
	}
	/**
	 * 账户提现
	 * @throws BusinessException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void fundOut(AccountRecord accountRecord) throws Exception{
		accountRecord.setTranType("01");
		keepAccount(accountRecord);
				
	}
	/**
	 * 账户转账
	 * @throws BusinessException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void transfer(AccountRecord accountRecord) throws Exception {

		String accountNo=accountRecord.getAccountNo();
		String amount=accountRecord.getAmount();
		String otherAccountNo=accountRecord.getOtherAccountNo();
		String otherAccountName=accountRecord.getOtherAccountName();
		
		if(Standard.nvl(accountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[accountNo]校验失败");
		}
		if(Standard.nvl(otherAccountNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[otherAccountNo]校验失败");
		}
		if(Standard.nvl(otherAccountName)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[otherAccountName]校验失败");
		}
		if(Standard.nvl(amount)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");	
		}
		AccountInfo accountInfo=accountInfoMapper.queryAccount(accountNo);
		if(accountInfo==null){
			throw ExceptionBox.ACCOUNT_NOT_FOUND;
		}
		AccountInfo otherAccountInfo=new AccountInfo();
		otherAccountInfo.setAccountNo(otherAccountNo);
		otherAccountInfo.setAccountName(otherAccountName);
		otherAccountInfo=accountInfoMapper.checkAccount(otherAccountInfo);
		if(otherAccountInfo==null){
			throw ExceptionBox.ACCOUNT_NOT_FOUND;
		}
		accountRecord.setTranType("02");
		AccountRecord otherAccountRecord=new AccountRecord();
		otherAccountRecord.setAccountNo(otherAccountNo);
		otherAccountRecord.setOtherAccountNo(accountNo);
		otherAccountRecord.setOtherAccountName(accountInfo.getAccountName());
		otherAccountRecord.setAmount(amount);
		otherAccountRecord.setTranType("12");
		otherAccountRecord.setChannelSystemNo(accountRecord.getChannelSystemNo());
		otherAccountRecord.setChannelSerialNo(accountRecord.getChannelSerialNo());
		
		keepAccount(accountRecord);
		keepAccount(otherAccountRecord);
	}
	/**
	 * 交易查证
	 * @throws BusinessException 
	 */
	public AccountLog queryAccountLog(AccountLog accountLog) throws BusinessException {
		String channelSystemNo=accountLog.getChannelSystemNo();
		String originalSerialNo=accountLog.getOriginalSerialNo();

		checkAccess(channelSystemNo,accountLog.getClientIP());
		if(Standard.nvl(originalSerialNo)){
			throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[originalSerialNo]校验失败");	
		}
		AccountLog returnAccountLog = accountInfoMapper.queryAccountLog(accountLog);
		if(returnAccountLog==null){
			//交易不存在
			returnAccountLog=accountLog;
			returnAccountLog.setReturnCode("-1");
			returnAccountLog.setChannelSerialNo(null);
			returnAccountLog.setChannelSystemNo(null);
			returnAccountLog.setClientIP(null);
		}else{
			if(returnAccountLog.getReturnCode().equals("00000000")){
				//交易成功
				returnAccountLog.setReturnCode("0");
				if(returnAccountLog.getCode().equals("CREATE")){
					AccountInfo accountInfo=accountInfoMapper.queryAccountByLog(accountLog);
					returnAccountLog.setAccountNo(accountInfo.getAccountNo());
				}
			}else{
				//交易失败
				returnAccountLog.setReturnCode("1");
			}
		}

		return returnAccountLog;

	}
	/**
	 * 查询原交易信息
	 * @throws BusinessException 
	 */
	public AccountRecord getAccountRecord(AccountLog accountLog) throws BusinessException {
		
		return accountInfoMapper.getAccountRecord(accountLog);
	}
	/**
	 * 冲正成功后更新原交易日志信息
	 */
	public void updateReversalAccountLog(String channelSystemNo,String channelSerialNo,String originalSerialNo){

		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("channelSystemNo", channelSystemNo);
		params.put("channelSerialNo", channelSerialNo);
		params.put("originalSerialNo", originalSerialNo);
		accountInfoMapper.updateReversalAccountLog(params);
	}
}
