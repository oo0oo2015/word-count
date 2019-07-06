package wang.oo0oo.wordcount.hadoop;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;

public class IKAnalyzerTest {

    public static void main(String[] args) throws Exception {
        //检索内容
        String text = "lxw的大数据田地 -- lxw1234.com 专注Hadoop、Spark、Hive等大数据技术博客。 北京优衣库";
        Analyzer analyzer = new IKAnalyzer(true);

        //创建分词对象
        StringReader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream("", reader);
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
        while(ts.incrementToken()){
            System.out.print(term.toString()+"|");
        }
        analyzer.close();
        reader.close();
    }
}
