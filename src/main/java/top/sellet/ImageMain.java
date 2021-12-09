package top.sellet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import top.sellet.utils.JsoupHtml;
/**
 * @author mo
 */
public class ImageMain {
    public static void main(String[] args) throws Exception {
        JsoupHtml jsoupHtml = new JsoupHtml();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        jsoupHtml.setHttpClient(httpClient);
        jsoupHtml.getHtmlUrl("https://www.moe17.com/category/cos//page/3");
    }
}
