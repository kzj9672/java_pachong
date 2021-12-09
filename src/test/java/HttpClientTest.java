import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HttpClientTest {
    public static void main(String[] args) {
        //创建HTTPclient 相当于打开一个浏览器解析
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response =null;
        //创建get请求，详单与在浏览器地址栏输入网址
        HttpGet request = new HttpGet("https://www.moe17.com/category/cos/");
        //设置请求头，反反爬虫机制
        request.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
        //如果ip被封
//        HttpHost proxy = new HttpHost("", 9999);
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//        request.setConfig(config);
        try {
            //执行get请求，相当于在输入地址栏后回车
            response = httpClient.execute(request);
            //判断响应状态是否为200
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                //获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                //解析html
                Document document = Jsoup.parse(html);
                //通过标签获取title
                //System.out.print(document.getElementsByTag("title").first());
                //通过id获取文章列表元素对象
                Element postList = document.getElementById("post-list");
                Elements postItems = postList.getElementsByClass("post-list-item");
                //遍历
                for (Element postItem : postItems) {
                    //获取文章标题元素
                    Elements titleEle = postItem.select(".post-info h2 a[target='_blank']");
                    System.out.println("文章标题:"+titleEle.text());
                    System.out.println("文章地址:"+titleEle.attr("href"));
                    //获取图片
                    Elements pic = postItem.select(".post-thumb");
                    System.out.println("图片的标题"+pic.attr("alt"));
                    System.out.println("图片的地址"+pic.attr("src"));

                    System.out.println("**********************************************");

                }
                //System.out.print(html);
            }else {
                //如果不是200根据情况做处理
                System.out.print("返回状态不是200");
                System.out.print(EntityUtils.toString(response.getEntity(),"utf-8"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }

}
