package com.qianbao.account.exception;

public class ExceptionBox {
	
	public static BusinessException CHANNEL_SYSTEMNO_INVALID=new BusinessException("ACC00001","系统编号不合法");
	public static BusinessException CHANNEL_SERIALNO_INVALID=new BusinessException("ACC00002","渠道流水号重复");

	public static BusinessException ACCOUNT_NOT_FOUND=new BusinessException("ACC10001","账户不存在");
	public static BusinessException ACCOUNT_NOT_ENOUGH=new BusinessException("ACC10002","账户余额不足");
	
	public static BusinessException ORIGINALTRAN_NOT_FOUND=new BusinessException("ACC11001","原交易不存在");
	public static BusinessException ORIGINALTRAN_FAIL=new BusinessException("ACC11002","原交易失败，无需冲正");
	public static BusinessException ORIGINALTRAN_NOTSUPPORT_REVERSAL=new BusinessException("ACC11003","原交易不支持冲正");
	public static BusinessException ORIGINALTRAN_HAD_REVERSAL=new BusinessException("ACC11004","原交易已经冲正");
	
	public static BusinessException FIELD_INVALID=new BusinessException("ACC88888","数据校验失败");
	
	public static BusinessException OPERATION_FAIL=new BusinessException("ACC99999","系统异常");
	
}
