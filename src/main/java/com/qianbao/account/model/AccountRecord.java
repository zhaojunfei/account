package com.qianbao.account.model;

import com.qianbao.account.util.Standard;


/**
 * 
 * @author genghs
 *
 */

public class AccountRecord {
	private String serialNo;
	private String accountNo;
	private String tranTime;
	private String tranType;
	private String otherAccountNo;
	private String otherAccountName;
	private String amount;
	private String balance;
	private String status="0";
	private String channelSystemNo;
	private String channelSerialNo;
	private String clientIP;
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getTranTime() {
		return tranTime;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getOtherAccountNo() {
		return otherAccountNo;
	}
	public void setOtherAccountNo(String otherAccountNo) {
		this.otherAccountNo = otherAccountNo;
	}
	public String getOtherAccountName() {
		return otherAccountName;
	}
	public void setOtherAccountName(String otherAccountName) {
		this.otherAccountName = otherAccountName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChannelSystemNo() {
		return channelSystemNo;
	}
	public void setChannelSystemNo(String channelSystemNo) {
		this.channelSystemNo = channelSystemNo;
	}
	public String getChannelSerialNo() {
		return channelSerialNo;
	}
	public void setChannelSerialNo(String channelSerialNo) {
		this.channelSerialNo = channelSerialNo;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
    public String toString() {
		StringBuffer buffer=new StringBuffer();
		if(!Standard.nvl(accountNo)){
			buffer.append("accountNo=");
			buffer.append(accountNo);
			buffer.append(",");
		}
		if(!Standard.nvl(otherAccountNo)){
			buffer.append("otherAccountNo=");
			buffer.append(otherAccountNo);
			buffer.append(",");
		}
		if(!Standard.nvl(otherAccountName)){
			buffer.append("otherAccountName=");
			buffer.append(otherAccountName);
			buffer.append(",");
		}
		if(!Standard.nvl(amount)){
			buffer.append("amount=");
			buffer.append(amount);
			buffer.append(",");
		}
		return buffer.length()==0?null:buffer.substring(0, buffer.length()-1);
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
}
