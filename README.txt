简单记录一下流程：
一、Linux下配置hadoop集群（伪分布式或完全分布式）
1、https://blog.csdn.net/z1148059382/article/details/89459182
2、Windows下管理HDFS的神器：HDFS Explorer


二、在Windows环境下通过IDEA远程连接Linux里的Hadoop并运行MapReduce程序
1、下载winutils.exe和hadoop.dll
https://github.com/steveloughran/winutils
2、将hadoop.dll放到E:\windows\System32里
3、将hadoop-2.7.7.tar.gz（二进制包）解压到Windows的E:\里
4、将winutils.exe放到E:\hadoop-2.7.7\bin里
5、将Linux里配置好的hadoop的配置文件目录里的log4j.properties和core-site.xml复制到项目根目录（IDEA的maven项目的话是放到resources目录里，下同）
6、往HDFS里/user/joe/wordcount/input传要统计的文件（不要创建output文件夹）
7、执行程序
8、下次执行前要手动删除output目录【待完善】


三、中文分词能力（使用IKAnalyzer分词器）
1、下载IKAnalyzer2012_u6.zip（最新版）
https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/ik-analyzer/IKAnalyzer2012_u6.zip
2、由于maven库里没有ik的坐标，所以我们需要手动将IKAnalyzer2012_u6.zip里的jar包添加到本地Maven仓库里（也可以直接引入到项目的外部jar包库中）
（1）cmd，进入放置jar的文件夹
（2）mvn install:install-file -DgroupId=org.wltea.ik-analyzer -DartifactId=ik-analyzer -Dversion=2012_u6 -Dpackaging=jar -Dfile=IKAnalyzer2012_u6.jar
（3)在项目的pom.xml里用以下内容引入依赖：
        <!-- IKAnalyzer -->
        <dependency>
            <groupId>org.wltea.ik-analyzer</groupId>
            <artifactId>ik-analyzer</artifactId>
            <version>2012_u6</version>
        </dependency>
        <!-- lucene-core -->
        <dependency>
            <groupId>org.apache.lucene</groupId>
            <artifactId>lucene-core</artifactId>
            <version>3.6.0</version>
        </dependency>
3、将IKAnalyzer.cfg.xml和stopword.dic复制到项目根目录
4、配置用户自定义词库
（1）项目根目录下创建MyDic.dic，在里边打单词，一行一个
（2）修改IKAnalyzer.cfg.xml
        <!--用户可以在这里配置自己的扩展字典 -->
     	<entry key="ext_dict">MyDic.dic;</entry>
