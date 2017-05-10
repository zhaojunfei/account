package com.qianbao.account.model;

import com.qianbao.account.util.Standard;

public class QueryAccountRecord {
	private String accountNo;
	private String startDate;
	private String endDate;
	private String page;
	private String pageShowNum;
	private String channelSystemNo;
	private String channelSerialNo;
	private String clientIP;

	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPageShowNum() {
		return pageShowNum;
	}
	public void setPageShowNum(String pageShowNum) {
		this.pageShowNum = pageShowNum;
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
		if(!Standard.nvl(startDate)){
			buffer.append("startDate=");
			buffer.append(startDate);
			buffer.append(",");
		}
		if(!Standard.nvl(endDate)){
			buffer.append("endDate=");
			buffer.append(endDate);
			buffer.append(",");
		}
		if(!Standard.nvl(page)){
			buffer.append("page=");
			buffer.append(page);
			buffer.append(",");
		}
		if(!Standard.nvl(pageShowNum)){
			buffer.append("pageShowNum=");
			buffer.append(pageShowNum);
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
