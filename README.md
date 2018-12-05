# account<br/>
linux <br/>
cd ~/git<br/>
git clone https://github.com/zhaojunfei/account.git<br/>
cd account<br/>
#install.txt start<br/>
mysql -uroot -p<br/>
CREATE DATABASE IF NOT EXISTS account DEFAULT CHARSET utf8 COLLATE utf8_general_ci;<br/>
grant all privileges on account.* to account@localhost identified by &#39;account&#39;;<br/>
grant select, insert, update, delete on *.* to common@localhost identified by &#39;123123&#39;;<br/>
grant select, insert, update, delete on *.* to common@'%' identified by &#39;123123&#39;;<br/>
grant all privileges on account.* to account@&#39;%&#39; identified by &#39;account&#39;;<br/>
grant create routine on account.* to account@&#39;%&#39;;<br/>
grant alter&nbsp; routine on account.* to account@&#39;%&#39;;<br/>grant execute on account.* to account@&#39;%&#39;;<br/>
--登录mysql<br/>mysql -uaccount -paccount<br/>
use account;<br/>--导入相关初始化数据<br/>
source ACCOUNTINFO.sql<br/>
source ACCOUNTLOG.SQL<br/>
source ACCOUNTRECORD.sql<br/>
source getSequence.sql<br/>
#install.txt end<br/>
mvn install<br/>
cp -R target/account.war $CATALINA_HOME/webapps/<br/>
sh $CATALINA_HOME/bin/catalina.sh start<br/>
<br/>
<br/>http://localhost:8080/account/index.html<br/>
