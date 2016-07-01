# HttpsTest
使用AsyncHttpClient框架的Https请求

帮助：
使用java keytool生成.bks文件的方法
1.获得一个.cer 或 .crt格式的证书
2.下载JCE Provider 即文件bcprov-jdk15on-146.jar ，百度网盘下载地址http://pan.baidu.com/s/1c1ur13y 
3.将以上两个文件放到同一文件夹，在此文件夹下执行如下命令
keytool -importcert -v -trustcacerts -alias test① -file srca.cer② \
-keystore test.bks③ -storetype BKS \
-providerclass org.bouncycastle.jce.provider.BouncyCastleProvider \
-providerpath ./bcprov-jdk15on-146.jar⑤ -storepass 123456⑥

① —— 是别名
② —— 是证书全地址
③ —— 生成的bks文件名称
④ —— jar包的全地址
⑤ —— 密码

运行后将显示证书内容并提示是否确认，按Y回车确认，至此.bks文件生成完毕。
