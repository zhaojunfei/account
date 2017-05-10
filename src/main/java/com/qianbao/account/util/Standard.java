package com.qianbao.account.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class Standard {
	/**
	 * 商户待清算账户号
	 */
	public static final String MERCHANT_NOTSETTLE_ACCOUNT_NO="2015010100000001";
	/**
	 * 商户待清算账户名
	 */
	public static final String MERCHANT_NOTSETTLE_ACCOUNT_NAME="商户待清算账户";
	/**
	 * 用户保证金账户号
	 */
	public static final String USER_GUARANTEE_ACCOUNT_NO="2015010100000002";
	/**
	 * 用户保证金账户名
	 */
	public static final String USER_GUARANTEE_ACCOUNT_NAME="用户预付金账户";
	/**
	 * 平台待结转利润账户号
	 */
	public static final String NOT_CLEAR_PROFIT_ACCOUNT_NO="2015010100000003";
	/**
	 * 平台待结转利润账户名
	 */
	public static final String NOT_CLEAR_PROFIT_ACCOUNT_NAME="利润待清分账户";
	/**
	 * 平台待结转利润账户号
	 */
	public static final String BATCH_TRANSFER_MIDDLEACCOUNT_NO="2015010100000004";
	/**
	 * 平台待结转利润账户名
	 */
	public static final String BATCH_TRANSFER_MIDDLEACCOUNT_NAME="批量交易中间账户";
	/**
	 * 冲正交易支持列表
	 */
	public static final HashSet<String> FUNC_SUPPORT_REVERSAL=new HashSet<String>();
	
	static{
		FUNC_SUPPORT_REVERSAL.add("TRANSFER");
		FUNC_SUPPORT_REVERSAL.add("PAYMENT");
	}
	/**
	 * 取得格式化的服务器时间
	 * @param timeFormat 时间：yyyyMMddHHmmss；日期：yyyyMMdd
	 * @return String
	 */
	public static String getServerTime( String timeFormat )
	{
		String format = (timeFormat == null) ? "yyyyMMddHHmmss" : timeFormat;
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat( format );
		return formatter.format( date );
	}
	/**
	 * 判断字符串是否为空
	 * @param obj
	 * @return boolean
	 */
	public static boolean nvl(Object obj){
		return obj==null||obj.toString().trim().length()==0;
	}
	public static void main(String[] args) {
		

	}

}
