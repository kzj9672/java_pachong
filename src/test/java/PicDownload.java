import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.*;

class PicDownload {
    public static void main(String[] args) {
        Run run = new Run();
        try {
            run.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static class  Run{
        public Run() {
        }

        public void  run() throws Exception {
            HttpClient httpClient = HttpClients.createDefault();
            String url = "https://img.acg12.us/images/2020/04/22/8lQst.jpg";
            //File file = new File("F:\\图片");
            //FileOutputStream fileOutputStream = new FileOutputStream(file);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Referer","https://www.moe17.com/");
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            InputStream inputStream = httpResponse.getEntity().getContent();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\pic\\1.jpg"));
            byte[] bytes = new byte[1024000];
            int length=0;
            while ((length=inputStream.read(bytes,0,bytes.length)) != -1){
                fileOutputStream.write(bytes,0,length);
            }
            inputStream.close();
            fileOutputStream.close();
            System.out.println();
            System.out.println(statusCode);
//        URL uri = new URL(url);
//        uri.openConnection().setRequestProperty("Referer","https://www.moe17.com/");
//        uri.openConnection().setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Mobile Safari/537.36");
//        uri.openConnection().setRequestProperty("method: ","GET");
//        InputStream in = uri.openConnection().getInputStream();
//        FileOutputStream fo = new FileOutputStream(new File("E:\\backup"));//文件输出流
//        byte[] buf = new byte[1024];
//        int length = 0;
//        System.out.println("开始下载:" + url);
//        while ((length = in.read(buf, 0, buf.length)) != -1) {
//            fo.write(buf, 0, length);
//        }
//        //关闭流
//        in.close();
//        fo.close();
//        System.out.println("下载完成");


        }
    }


}
