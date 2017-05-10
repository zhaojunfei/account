package com.qianbao.account.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianbao.account.exception.ExceptionBox;
import com.qianbao.account.exception.BusinessException;
import com.qianbao.account.model.AccountInfo;
import com.qianbao.account.model.AccountLog;
import com.qianbao.account.model.AccountRecord;
import com.qianbao.account.model.AccountRecordBatch;
import com.qianbao.account.model.QueryAccountRecord;
import com.qianbao.account.service.AccountService;
import com.qianbao.account.util.Standard;
/**
 * 
 * @author genghs
 *
 */
@Controller
public class AccountController {
	@Autowired
	private AccountService accountService;
	//日期记录器
	private static Logger logger= LogManager.getLogger(AccountController.class);
		
	/**
	 * 查询账户信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryAccount")
	@ResponseBody
	protected Map<String,Object> queryAccount(AccountInfo accountInfo,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始查询账户，账户号【"+accountInfo.getAccountNo()+"】...");
		//定义返回map，初始化返回结果为成功
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		//获取请求方IP地址
		String clientIP=request.getRemoteAddr();
		accountInfo.setClientIP(clientIP);
		//调用账户查询service
		try {
			accountInfo = accountService.queryAccount(accountInfo);
			model.put("accountInfo", accountInfo);
			logger.info("查询账户成功，账户号【"+accountInfo.getAccountNo()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.info("查询账户失败【"+e.getErrorMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());		
			logger.info("查询账户失败【"+t.getMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		}
		return model;
	}
	/**
	 * 创建账户
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/createAccount")
	@ResponseBody
	protected Map<String,Object> createAccount(AccountInfo accountInfo,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始创建账户，账户名【"+accountInfo.getAccountName()+"】，用户号【"+accountInfo.getUserNo()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();
		accountInfo.setClientIP(clientIP);

		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),clientIP,"CREATE",accountInfo.toString());
			//调用service
			accountInfo=accountService.createAccount(accountInfo);
			model.put("accountInfo", accountInfo);
			logger.info("创建账户成功，账户名【"+accountInfo.getAccountName()+"】，用户号【"+accountInfo.getUserNo()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.info("创建账户失败【"+e.getErrorMessage()+"】，账户名【"+accountInfo.getAccountName()+"】，用户号【"+accountInfo.getUserNo()+"】");
		}  catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());		
			logger.info("创建账户失败【"+t.getMessage()+"】，账户名【"+accountInfo.getAccountName()+"】，用户号【"+accountInfo.getUserNo()+"】");
		} finally{
			accountService.updateAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),accountInfo.toString());
		}
		return model;
	}
	/**
	 * 冻结账户余额
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/freezeAccount")
	@ResponseBody
	protected Map<String,Object> freezeAccount(AccountInfo accountInfo,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始冻结账户【"+accountInfo.getAccountNo()+"】，冻结金额:"+accountInfo.getAmount()+"...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		
		String clientIP=request.getRemoteAddr();
		accountInfo.setClientIP(clientIP);
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),clientIP,"FREEZE",accountInfo.toString());
			//调用service
			accountService.freezeAccount(accountInfo);
			logger.info("冻结账户【"+accountInfo.getAccountNo()+"】成功，冻结金额:"+accountInfo.getAmount());
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("冻结账户失败【"+e.getErrorMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());	
			logger.error("冻结账户失败【"+t.getMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	/**
	 * 冻结账户余额
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/unFreezeAccount")
	@ResponseBody
	protected Map<String,Object> unFreezeAccount(AccountInfo accountInfo,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始解冻账户【"+accountInfo.getAccountNo()+"】，解冻金额:"+accountInfo.getAmount()+"...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();
		accountInfo.setClientIP(clientIP);
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),clientIP,"UNFREEZE",accountInfo.toString());
			//调用service
			accountService.unFreezeAccount(accountInfo);
			logger.info("解冻账户【"+accountInfo.getAccountNo()+"】成功，解冻金额:"+accountInfo.getAmount());
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("解冻账户失败【"+e.getErrorMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());
			logger.error("解冻账户失败【"+t.getMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	/**
	 * 充值
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fundIn")
	@ResponseBody
	protected Map<String,Object> fundIn(AccountRecord accountRecord,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始账户充值，账户号【"+accountRecord.getAccountNo()+"】，充值金额【"+accountRecord.getAmount()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();

		accountRecord.setClientIP(clientIP);
		accountRecord.setTranType("11");
		
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountRecord.getChannelSystemNo(),accountRecord.getChannelSerialNo(),clientIP,"FUNDIN",accountRecord.toString());
			//调用service
			accountService.fundIn(accountRecord);
			logger.info("账户充值成功，账户号【"+accountRecord.getAccountNo()+"】，充值金额【"+accountRecord.getAmount()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("账户充值失败【"+e.getErrorMessage()+"】，账户号【"+accountRecord.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());	
			logger.error("账户充值失败【"+t.getMessage()+"】，账户号【"+accountRecord.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountRecord.getChannelSystemNo(),accountRecord.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	/**
	 * 充值
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fundOut")
	@ResponseBody
	protected Map<String,Object> fundOut(AccountRecord accountRecord,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始账户提现，账户号【"+accountRecord.getAccountNo()+"】，提现金额【"+accountRecord.getAmount()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();

		accountRecord.setClientIP(clientIP);

		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountRecord.getChannelSystemNo(),accountRecord.getChannelSerialNo(),clientIP,"FUNDOUT",accountRecord.toString());
			//调用service
			accountService.fundOut(accountRecord);
			logger.info("账户提现成功，账户号【"+accountRecord.getAccountNo()+"】，提现金额【"+accountRecord.getAmount()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("账户取现失败【"+e.getErrorMessage()+"】，账户号【"+accountRecord.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());
			logger.error("账户取现失败【"+t.getMessage()+"】，账户号【"+accountRecord.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountRecord.getChannelSystemNo(),accountRecord.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	/**
	 * 转账
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/transfer")
	@ResponseBody
	protected Map<String,Object> transfer(AccountRecord accountRecord,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始账户转账，付方账户号【"+accountRecord.getAccountNo()+"】，收方账户号【"+accountRecord.getOtherAccountNo()+"】，收方账户名【"
				+accountRecord.getOtherAccountName()+"】，转账金额【"+accountRecord.getAmount()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();
		accountRecord.setClientIP(clientIP);
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountRecord.getChannelSystemNo(),accountRecord.getChannelSerialNo(),clientIP,"TRANSFER",accountRecord.toString());
			//调用service
			accountService.transfer(accountRecord);
			logger.info("账户转账成功，付方账户号【"+accountRecord.getAccountNo()+"】，收方账户号【"+accountRecord.getOtherAccountNo()+"】，收方账户名【"
					+accountRecord.getOtherAccountName()+"】，转账金额【"+accountRecord.getAmount()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("账户转账失败【"+e.getErrorMessage()+"】，账户号【"+accountRecord.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());
			logger.error("账户转账失败【"+t.getMessage()+"】，账户号【"+accountRecord.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountRecord.getChannelSystemNo(),accountRecord.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	@RequestMapping(value="/queryAccountRecord")
	@ResponseBody
	protected Map<String,Object> queryAccountRecord(QueryAccountRecord queryAccountRecord,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始查询账户明细，账户号【"+queryAccountRecord.getAccountNo()+"】，开始日期【"+queryAccountRecord.getStartDate()+"】，结束日期【"
				+queryAccountRecord.getEndDate()+"】，第【"+queryAccountRecord.getPage()+"】页，每页显示【"+queryAccountRecord.getPageShowNum()+"】条...");
		Map<String,Object> model;
		String clientIP=request.getRemoteAddr();
		queryAccountRecord.setClientIP(clientIP);
		//调用service
		try {
			model = accountService.queryAccountRecord(queryAccountRecord);
			model.put("resultCode", "00000000");
			model.put("resultMessage", "交易成功");
			logger.info("查询账户明细成功，账户号【"+queryAccountRecord.getAccountNo()+"】，总条数【"+model.get("totalNum")+"】");
		} catch (BusinessException e) {
			model=new HashMap<String,Object>();
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("账户明细查询失败【"+e.getErrorMessage()+"】，账户号【"+queryAccountRecord.getAccountNo()+"】");
		} catch (Exception t) {
			model=new HashMap<String,Object>();
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());		
			logger.error("账户明细查询失败【"+t.getMessage()+"】，账户号【"+queryAccountRecord.getAccountNo()+"】");
		} 

		return model;
	}
	@RequestMapping(value="/checkOperationResult")
	@ResponseBody
	protected Map<String,Object> checkOperationResult(AccountLog accountLog,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始查证交易，系统号【"+accountLog.getChannelSystemNo()+"】，查询流水号【"+accountLog.getChannelSerialNo()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();
		accountLog.setClientIP(clientIP);
		//调用service
		try {
			accountLog=accountService.queryAccountLog(accountLog);
			model.put("accountLog", accountLog);
			logger.info("查证交易成功，系统号【"+accountLog.getChannelSystemNo()+"】，查询流水号【"+accountLog.getChannelSerialNo()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("查证交易失败【"+e.getErrorMessage()+"】，系统号【"+accountLog.getChannelSystemNo()+"】，查询流水号【"+accountLog.getChannelSerialNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());
			logger.error("查证交易失败【"+t.getMessage()+"】，系统号【"+accountLog.getChannelSystemNo()+"】，查询流水号【"+accountLog.getChannelSerialNo()+"】");
		}
		return model;
	}
	@RequestMapping(value="/pay")
	@ResponseBody
	protected Map<String,Object> pay(AccountInfo accountInfo,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始支付处理，账户【"+accountInfo.getAccountNo()+"】，金额:"+accountInfo.getAmount()+"，支付模式:"+accountInfo.getPayMode()+"...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		
		String clientIP=request.getRemoteAddr();
		accountInfo.setClientIP(clientIP);
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),clientIP,"PAYMENT",accountInfo.toString());
			AccountRecord accountRecord=new AccountRecord();
			accountRecord.setAccountNo(accountInfo.getAccountNo());
			accountRecord.setAmount(accountInfo.getAmount());
			accountRecord.setChannelSystemNo(accountInfo.getChannelSystemNo());
			accountRecord.setChannelSerialNo(accountInfo.getChannelSerialNo());
			if(accountInfo.getPayMode().equals("1")){
				accountRecord.setOtherAccountNo(Standard.MERCHANT_NOTSETTLE_ACCOUNT_NO);
				accountRecord.setOtherAccountName(Standard.MERCHANT_NOTSETTLE_ACCOUNT_NAME);
			}else if(accountInfo.getPayMode().equals("2")){
				accountRecord.setOtherAccountNo(Standard.USER_GUARANTEE_ACCOUNT_NO);
				accountRecord.setOtherAccountName(Standard.USER_GUARANTEE_ACCOUNT_NAME);				
			}else{
				throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据【payMode】校验异常");
			}
			//调用service
			accountService.transfer(accountRecord);
			logger.info("账户支付【"+accountInfo.getAccountNo()+"】成功，冻结金额:"+accountInfo.getAmount());
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("账户支付失败【"+e.getErrorMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());	
			logger.error("账户支付失败【"+t.getMessage()+"】，账户号【"+accountInfo.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountInfo.getChannelSystemNo(),accountInfo.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	@RequestMapping(value="/reversal")
	@ResponseBody
	protected Map<String,Object> reversal(AccountLog accountLog,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始冲正交易，系统号【"+accountLog.getChannelSystemNo()+"】，原流水号【"+accountLog.getOriginalSerialNo()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();
		accountLog.setClientIP(clientIP);
		//调用service
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountLog.getChannelSystemNo(),accountLog.getChannelSerialNo(),clientIP,"REVERAL",accountLog.toString());
			
			//查询原交易信息
			AccountLog originalAccountLog=accountService.queryAccountLog(accountLog);
			
			if(originalAccountLog.getReturnCode().equals("-1")){
				throw ExceptionBox.ORIGINALTRAN_NOT_FOUND;
			}
			if(originalAccountLog.getReturnCode().equals("1")){
				throw ExceptionBox.ORIGINALTRAN_FAIL;
			}
			if("1".equals(originalAccountLog.getReversalStatus())){
				throw ExceptionBox.ORIGINALTRAN_HAD_REVERSAL;
			}
			if(!Standard.FUNC_SUPPORT_REVERSAL.contains(originalAccountLog.getCode())){
				throw ExceptionBox.ORIGINALTRAN_NOTSUPPORT_REVERSAL;
			}
			
			AccountRecord accountRecord=accountService.getAccountRecord(accountLog);
			accountRecord.setChannelSystemNo(accountLog.getChannelSystemNo());
			accountRecord.setChannelSerialNo(accountLog.getChannelSerialNo());
			//原交易冲正
			accountService.transfer(accountRecord);
			
			//更新原交易冲正状态
			accountService.updateReversalAccountLog(accountLog.getChannelSystemNo(),accountLog.getChannelSerialNo(), accountLog.getOriginalSerialNo());;
			logger.info("交易冲正成功，系统号【"+accountLog.getChannelSystemNo()+"】，原流水号【"+accountLog.getOriginalSerialNo()+"】");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("交易冲正失败【"+e.getErrorMessage()+"】，系统号【"+accountLog.getChannelSystemNo()+"】，原流水号【"+accountLog.getOriginalSerialNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());
			logger.error("交易冲正失败【"+t.getMessage()+"】，系统号【"+accountLog.getChannelSystemNo()+"】，原流水号【"+accountLog.getOriginalSerialNo()+"】");
		}finally{
			accountService.updateAccountLog(accountLog.getChannelSystemNo(),accountLog.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
	/**
	 * 批量代付
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/transferBatch")
	@ResponseBody
	protected Map<String,Object> transferBatch(AccountRecordBatch accountRecordBatch,HttpServletRequest request,HttpServletResponse response){
		logger.info("开始批量代付，付方账户号【"+accountRecordBatch.getAccountNo()+"】...");
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("resultCode", "00000000");
		model.put("resultMessage", "交易成功");
		String clientIP=request.getRemoteAddr();
		try {
			//记录账户服务访问日志
			accountService.insertAccountLog(accountRecordBatch.getChannelSystemNo(),accountRecordBatch.getChannelSerialNo(),clientIP,"TRANSFERBATCH",accountRecordBatch.toString());
			
			//判断收款信息是否正确,计算代付总金额
			BigDecimal allAmount=new BigDecimal("0.00");
			BigDecimal amountDecimal;
			AccountInfo otherAccountInfo=new AccountInfo();
			
			for(AccountRecord accountRecord:accountRecordBatch.getAccountRecordList()){
				String amount=accountRecord.getAmount();
				String otherAccountNo=accountRecord.getOtherAccountNo();
				String otherAccountName=accountRecord.getOtherAccountName();
				
				if(Standard.nvl(otherAccountNo)){
					throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[otherAccountNo]校验失败");
				}
				if(Standard.nvl(otherAccountName)){
					throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[otherAccountName]校验失败");
				}
				if(Standard.nvl(amount)){
					throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");	
				}
				try{
					amountDecimal=new BigDecimal(amount);
				}catch(NumberFormatException e){
					throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
				}
				//判断金额是否合法，金额大于零
				if(amountDecimal.compareTo(new BigDecimal(0))!=1){
					throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"数据[amount]校验失败");
				}
				otherAccountInfo.setAccountNo(otherAccountNo);
				otherAccountInfo.setAccountName(otherAccountName);
				otherAccountInfo=accountService.checkAccount(otherAccountInfo);
				if(otherAccountInfo==null){
					throw ExceptionBox.ACCOUNT_NOT_FOUND;
				}
				allAmount=allAmount.add(amountDecimal);
			}
			//判断金额是否合法，金额大于零
			if(allAmount.compareTo(new BigDecimal(0))!=1){
				throw new BusinessException(ExceptionBox.FIELD_INVALID.getErrorCode(),"收款总金额错误");
			}		
			AccountRecord accountRecordAll=new AccountRecord();
			accountRecordAll.setClientIP(clientIP);
			accountRecordAll.setAccountNo(accountRecordBatch.getAccountNo());
			accountRecordAll.setAmount(allAmount.toString());
			accountRecordAll.setOtherAccountNo(Standard.BATCH_TRANSFER_MIDDLEACCOUNT_NO);
			accountRecordAll.setOtherAccountName(Standard.BATCH_TRANSFER_MIDDLEACCOUNT_NAME);
			//调用service，将总金额转入中间账户
			accountService.transfer(accountRecordAll);
			//逐笔转入收款账户
			for(AccountRecord accountRecord:accountRecordBatch.getAccountRecordList()){
				accountRecord.setAccountNo(Standard.BATCH_TRANSFER_MIDDLEACCOUNT_NO);
				accountService.transfer(accountRecord);
			}
			
			logger.info("批量代付成功，付方账户号【"+accountRecordBatch.getAccountNo()+"】，付款总金额【"+allAmount.toString()+"】，");
		} catch (BusinessException e) {
			model.put("resultCode", e.getErrorCode());
			model.put("resultMessage", e.getErrorMessage());
			logger.error("批量代付失败【"+e.getErrorMessage()+"】，账户号【"+accountRecordBatch.getAccountNo()+"】");
		} catch (Exception t) {
			model.put("resultCode", ExceptionBox.OPERATION_FAIL.getErrorCode());
			model.put("resultMessage", ExceptionBox.OPERATION_FAIL.getErrorMessage());
			logger.error("批量代付失败【"+t.getMessage()+"】，账户号【"+accountRecordBatch.getAccountNo()+"】");
		} finally{
			accountService.updateAccountLog(accountRecordBatch.getChannelSystemNo(),accountRecordBatch.getChannelSerialNo(),(String)model.get("resultCode"),(String)model.get("resultMessage"),null);
		}
		return model;
	}
}
