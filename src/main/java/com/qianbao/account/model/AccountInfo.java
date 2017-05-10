package com.qianbao.account.model;

import com.qianbao.account.util.Standard;

/**
 * 
 * @author genghs
 *
 */
public class AccountInfo {

	private String accountNo;
	private String accountName;
	private String userNo;
	private String balance="0";
	private String balanceFrozen="0";
	private String balanceAvailable="0";
	private String amount;
	private String status="0";
	private String openTime;
	private String channelSystemNo;
	private String channelSerialNo;
	private String clientIP;
	private String payMode;

	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getBalanceFrozen() {
		return balanceFrozen;
	}
	public void setBalanceFrozen(String balanceFrozen) {
		this.balanceFrozen = balanceFrozen;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	@Override
    public String toString() {
		StringBuffer buffer=new StringBuffer();
		if(!Standard.nvl(accountNo)){
			buffer.append("accountNo=");
			buffer.append(accountNo);
			buffer.append(",");
		}
		if(!Standard.nvl(accountName)){
			buffer.append("accountName=");
			buffer.append(accountName);
			buffer.append(",");
		}
		if(!Standard.nvl(userNo)){
			buffer.append("userNo=");
			buffer.append(userNo);
			buffer.append(",");
		}
		if(!Standard.nvl(amount)){
			buffer.append("amount=");
			buffer.append(amount);
			buffer.append(",");
		}
		if(!Standard.nvl(payMode)){
			buffer.append("payMode=");
			buffer.append(payMode);
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
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getBalanceAvailable() {
		return balanceAvailable;
	}
	public void setBalanceAvailable(String balanceAvailable) {
		this.balanceAvailable = balanceAvailable;
	}
}
