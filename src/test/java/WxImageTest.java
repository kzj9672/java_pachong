import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;


public class WxImageTest {

    private String title;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private JSONObject jsonObject;

    @Test
    public void main() throws IOException, InterruptedException {
        jsonObject = new JSONObject();
        String url = "https://mp.weixin.qq.com/s/bC2k64p-UU1R7kFwOlFzRg";
        jsoupHtml(url);
    }

    public void jsoupHtml(String url) throws IOException, InterruptedException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            HttpEntity responseEntity = httpResponse.getEntity();
            String html = EntityUtils.toString(responseEntity, "utf-8");
            Document document = Jsoup.parse(html);
            Element byId = document.getElementById("activity-name");
            title = byId.text();
            System.out.println(title);
            Element element = document.getElementById("js_content");
            Elements elements = element.getElementsByTag("p");
            System.out.println("共抓取到" + elements.size() + "个资源");

            File file = getFile();
            if (file == null) {
                return;
            }
            int i = 0;
            for (Element element1 : elements) {
                Elements img = element1.getElementsByTag("img");
                if (img.size() < 3) {
                    System.out.println("*************************");
                } else {
                    for (Element element2 : img) {
                        String attr = element2.attr("data-src");
                        i = i + 1;
                        download(attr, i, file);
                        System.out.println("第" + i + "张下载完成");
                        Thread.sleep(1000);
                    }
                }
                Elements select = element1.getElementsByTag("a");
                if (select.size() > 0) {
                    for (Element element2 : select) {
                        String s = element2.attr("href");
                        jsonObject.put(String.valueOf(i), s);
                    }
                    run(jsonObject);
                }
            }
        }
    }

    public void download(String url, int i, File file) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream inputStream = httpResponse.getEntity().getContent();
            String s = String.valueOf(i);


            FileOutputStream fileOutputStream = new FileOutputStream(file + "\\" + s + ".jpg");
            byte[] bytes = new byte[102400];
            int len = 0;
            while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            inputStream.close();
            fileOutputStream.close();
        }
    }

    public void run(JSONObject jsonObject) throws IOException {
        System.out.println(jsonObject);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String url = (String) entry.getValue();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity responseEntity = httpResponse.getEntity();
                String html = EntityUtils.toString(responseEntity, "utf-8");
                Document document = Jsoup.parse(html);
                Element byId = document.getElementById("activity-name");
                title = byId.text();
                System.out.println(title);
                File file = getFile();
                if (file == null) {
                    return;
                }
                Element element = document.getElementById("js_content");
                Elements elements = element.getElementsByTag("p");
                System.out.println("共抓取到" + elements.size() + "个资源");
                int i = 0;
                for (Element element1 : elements) {
                    Elements img = element1.getElementsByTag("img");
                    if (img.size() < 3) {
                        System.out.println("不是目标");
                    } else {
                        for (Element element2 : img) {
                            String attr = element2.attr("data-src");
                            i = i + 1;
                            download(attr, i, file);
                            System.out.println("第" + i + "张下载完成");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public File getFile() {
        File file = new File("E:\\image\\" + title);
        if (title.contains("/")) {
            String newTitle = title.replace("/", "-");
            file = new File("E:\\image\\" + newTitle);
        }
        //检测文件夹是否存在
        if (!file.exists() && !file.isDirectory()) {
            boolean b = file.mkdirs();
            if (b) {
                System.out.println(file);
                System.out.println("创建文件夹");
            }
        } else {
            System.out.println("文件夹已存在");
            int length = Objects.requireNonNull(file.listFiles()).length;
            if (length == 0) {
                file.delete();
            }
            return null;
        }
        return file;
    }
}
