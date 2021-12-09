package top.sellet.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mo
 */
public class JsoupHtml {
    private HttpClient httpClient;

    public JsoupHtml() {
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void getHtmlUrl(String url) throws Exception {
        CloseableHttpResponse response = null;
        //创建get请求，详单与在浏览器地址栏输入网址
        HttpGet request = new HttpGet(url);
        //设置请求头，反反爬虫机制
        request.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
        //设置代理ip
//        HttpHost proxy = new HttpHost("223.104.15.36", 8090);
//        RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
//        request.setConfig(requestConfig);
        try {
            //执行get请求，相当于在输入地址栏后回车
            response = (CloseableHttpResponse) httpClient.execute(request);
            //判断响应状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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
                int size = postItems.size();
                System.out.println("抓取到："+size+"个目标资源");
                System.out.println("---------------------------------------");
                int i = 0;
                //遍历
                for (Element postItem : postItems) {
                    i=i+1;
                    System.out.println("第"+i+"个目标资源");
                    //获取文章标题元素
                    Elements titleEle = postItem.select(".post-info h2 a[target='_blank']");
                    String imageHtml = titleEle.attr("href");
                    String text = titleEle.text();
                    System.out.println("文章标题:" + text);
                    System.out.println("文章地址:" + imageHtml);
                    getImageUrl(imageHtml, text);
                    System.out.println("**********************************************");
                }
            } else {
                //如果不是200根据情况做处理
                System.out.print("返回状态不是200");
                System.out.print(EntityUtils.toString(response.getEntity(), "utf-8"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }

    }

    /**
     * 解析每个文章的所有图片
     *
     */
    public void getImageUrl(String url, String title) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
        //设置代理ip
//        HttpHost proxy = new HttpHost("223.104.15.36", 8090);
//        RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
//        httpGet.setConfig(requestConfig);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String html = EntityUtils.toString(entity, "utf-8");
                Document document = Jsoup.parse(html);
                Element primaryHome = document.getElementById("primary-home");
                Elements elements = primaryHome.getElementsByClass("entry-content");
                String replace = null;
                File file = new File("E:\\pic\\");
                if (title.contains("/") || title.contains(":")) {
                    String newTitle = title.replace("/", "-");
                    replace = newTitle.replace(":", "_");
                    file = new File(file + "\\" + replace);
                } else {
                    file = new File(file + "\\" + title);
                }
                //检查该文件夹是否存在。如果不存在就创建
                if (!file.exists() && !file.isDirectory()) {
                    boolean b = file.mkdirs();
                    if (b) {
                        System.out.println(file);
                        System.out.println("创建文件夹");
                    }
                } else {
                    System.out.println("文件夹已存在");
                    return;
                }
                String startData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                System.out.println("开始爬取:" + startData);
                for (Element element : elements) {
                    PicDownload picDownload = new PicDownload();
                    Elements select = element.select(".entry-content img");
                    int i = 0;
                    for (Element element1 : select) {
                        String src = element1.attr("src");
                        picDownload.imageDownload(title, src, file);
                        i = i + 1;
                        System.out.println("第" + i + "张图片下载完成");
                        Thread.sleep(1000);
                    }
                    //当前线程休眠5秒，避免过高频率爬取被封
                    Thread.sleep(5000);
                }
                String endData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                System.out.println("结束爬取:" + endData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
