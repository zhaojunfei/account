package com.qianbao.account.model;

import java.util.List;

import com.qianbao.account.util.Standard;

public class AccountRecordBatch {

	private String accountNo;
	private String channelSystemNo;
	private String channelSerialNo;
	private String clientIP;
	private List<AccountRecord> accountRecordList;
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
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
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public List<AccountRecord> getAccountRecordList() {
		return accountRecordList;
	}
	public void setAccountRecordList(List<AccountRecord> accountRecordList) {
		this.accountRecordList = accountRecordList;
	}
	@Override
    public String toString() {
		StringBuffer buffer=new StringBuffer();
		if(!Standard.nvl(accountNo)){
			buffer.append("accountNo=");
			buffer.append(accountNo);
			buffer.append(",");
		}
		buffer.append("accountRecordList=");
		buffer.append(accountRecordList.toString());
		buffer.append(",");
		return buffer.length()==0?null:buffer.substring(0, buffer.length()-1);
	}
}
