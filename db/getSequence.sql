-- ------------------------------------
--  表名称--序列号信息表
-- ------------------------------------
DROP TABLE IF EXISTS sequence;
CREATE TABLE sequence
(
	seq_type	varchar(20)		NOT NULL,	-- 序列号名
	seq_no		bigint(20)		NOT NULL,	-- 序列号值，初始时第一位应为1
	seq_length	tinyint(2)		NOT NULL,	-- 序列号位数
	PRIMARY KEY (seq_type)
)DEFAULT CHARSET=utf8;

INSERT INTO sequence VALUES ('serialNo',100000000000,12);
INSERT INTO sequence VALUES ('accountNo',10000000,8);
INSERT INTO sequence VALUES ('clearNo',1000000000,10);



DROP FUNCTION IF EXISTS getSequence;
-- 创建生成序列号的函数
DELIMITER $
CREATE FUNCTION getSequence ( in_type varchar(20) ) RETURNS varchar(60)
NOT DETERMINISTIC
READS SQL DATA
BEGIN
DECLARE thisFlowNo bigint(20);
UPDATE sequence SET seq_no =
(
select tmp.thisFlowNo from (
SELECT seq_no,seq_length,
(CASE WHEN (POWER(10,seq_length)-seq_no=1) THEN POWER(10,seq_length-1)ELSE  seq_no + 1 END) thisFlowNo FROM sequence WHERE  seq_type = in_type
)
tmp
)
 WHERE  seq_type = in_type;


select seq_no into thisFlowNo from sequence WHERE seq_type = in_type;

RETURN CONCAT(DATE_FORMAT(now(), '%Y%m%d'),thisFlowNo);
END
$
DELIMITER ;