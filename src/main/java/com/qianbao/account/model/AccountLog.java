package com.qianbao.account.model;

import com.qianbao.account.util.Standard;

public class AccountLog {
	private String code;
	private String time;
	private String channelSystemNo;
	private String channelSerialNo;
	private String clientIP;
	private String returnCode;
	private String returnMessage;
	private String originalSerialNo;
	private String reversalStatus;
	private String accountNo;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getOriginalSerialNo() {
		return originalSerialNo;
	}
	public void setOriginalSerialNo(String originalSerialNo) {
		this.originalSerialNo = originalSerialNo;
	}
    public String toString() {
		StringBuffer buffer=new StringBuffer();
		if(!Standard.nvl(originalSerialNo)){
			buffer.append("originalSerialNo=");
			buffer.append(originalSerialNo);
			buffer.append(",");
		}
		return buffer.length()==0?null:buffer.substring(0, buffer.length()-1);
    }
	public String getReversalStatus() {
		return reversalStatus;
	}
	public void setReversalStatus(String reversalStatus) {
		this.reversalStatus = reversalStatus;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
}
