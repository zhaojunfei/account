/*************************************
������:�˻���־��
�ֶ�ǰ׺:ALG
����޸���:genghs
*************************************/
DROP TABLE IF EXISTS ACCOUNTLOG;

CREATE TABLE ACCOUNTLOG 
(
	ALG_TIME		CHAR(14)	NOT NULL,  /*����ʱ��*/
	ALG_CODE		VARCHAR(20)	NOT NULL,  /*���ױ���*/
	ALG_REQUESTMESSAGE	VARCHAR(4000)	,  /*����������*/
	ALG_RESPONSEMESSAGE	VARCHAR(4000)	,  /*���׷��ر���*/
	ALG_RETURNCODE		VARCHAR(8)	,  /*������*/
	ALG_RETURNMESSAGE	VARCHAR(30)	,  /*��������*/
	ALG_REVERSALSTATUS	CHAR(1)	,  
	ALG_REVERSALSERIALNO	VARCHAR(30)	,  
	ALG_CHANNELSYSTEMNO	VARCHAR(30)	,  /*����ϵͳ��*/
	ALG_CHANNELSERIALNO	VARCHAR(30)	  /*������ˮ��*/
)DEFAULT CHARSET=utf8;