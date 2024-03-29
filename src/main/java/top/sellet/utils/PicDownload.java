package top.sellet.utils;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;


import java.io.*;
import java.util.Random;

/**
 * @author mo
 */
public class PicDownload {

    public PicDownload() {
    }

    public void imageDownload(String title, String url, File file,int i){
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
//       httpGet.setHeader(":scheme:", "https");
//        httpGet.setHeader(":authority:","dongtimimi.com");
        //httpGet.setHeader(":path:","dongtimimi.com");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
        //设置代理ip
//        HttpHost proxy = new HttpHost("223.104.15.36", 8090);
//        RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
//        httpGet.setConfig(requestConfig);

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            String s = String.valueOf(i);
            InputStream inputStream = httpResponse.getEntity().getContent();
            FileOutputStream fileOutputStream = new FileOutputStream(file + "\\" + s + ".jpg");
            byte[] bytes = new byte[1024000];
            int length = 0;
            while ((length = inputStream.read(bytes, 0, bytes.length)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("下载这个链接时出错："+url);
            e.printStackTrace();
        }

    }
}
