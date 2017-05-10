# account
linux 
cd ~/git
git clone https://github.com/zhaojunfei/account.git
cd account
#install.txt start
mysql -uroot -p
CREATE DATABASE IF NOT EXISTS account DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
grant all privileges on account.* to account@localhost identified by 'account';
grant all privileges on account.* to account@'%' identified by 'account';
grant create routine on account.* to account@'%';
grant alter  routine on account.* to account@'%';
grant execute on account.* to account@'%';
--登录mysql
mysql -uaccount -paccount
use account;
--导入相关初始化数据
source ACCOUNTINFO.sql
source ACCOUNTLOG.SQL
source ACCOUNTRECORD.sql
source getSequence.sql
#install.txt end
mvn install
cp -R target/account.war $CATALINA_HOME/webapps/
sh $CATALINA_HOME/bin/catalina.sh start


http://localhost:8080/account/index.html
