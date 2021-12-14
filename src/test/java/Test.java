import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://dongtimimi.com/wp-json/b2/v1/getPostList");
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity responseEntity = httpResponse.getEntity();
            String s = EntityUtils.toString
            (responseEntity, "utf-8");
            Document document = Jsoup.parse(s);
            System.out.println(document);
            System.out.println(statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
