/*************************************
表名称:账户日志表
字段前缀:ALG
最后修改人:genghs
*************************************/
DROP TABLE IF EXISTS ACCOUNTLOG;

CREATE TABLE ACCOUNTLOG 
(
	ALG_TIME		CHAR(14)	NOT NULL,  /*交易时间*/
	ALG_CODE		VARCHAR(20)	NOT NULL,  /*交易编码*/
	ALG_REQUESTMESSAGE	VARCHAR(4000)	,  /*交易请求报文*/
	ALG_RESPONSEMESSAGE	VARCHAR(4000)	,  /*交易返回报文*/
	ALG_RETURNCODE		VARCHAR(8)	,  /*返回码*/
	ALG_RETURNMESSAGE	VARCHAR(30)	,  /*返回描述*/
	ALG_REVERSALSTATUS	CHAR(1)	,  
	ALG_REVERSALSERIALNO	VARCHAR(30)	,  
	ALG_CHANNELSYSTEMNO	VARCHAR(30)	,  /*渠道系统号*/
	ALG_CHANNELSERIALNO	VARCHAR(30)	  /*渠道流水号*/
)DEFAULT CHARSET=utf8;