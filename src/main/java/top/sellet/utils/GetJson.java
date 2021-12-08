package top.sellet.utils;



import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author mo
 */
public class GetJson {
    public JSONObject getHttpJson(String url, int comfort) throws Exception {
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0(compatible; MSIE 6.0; windows NT 5.1;SV1)");
            connection.connect();
            if (connection.getResponseCode() == 200){
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[10485760];
                int len = 0;
                while ((len=is.read(buffer)) != -1){
                    baos.write(buffer,0,len);
                }
                String jsonString = baos.toString();
                baos.close();
                is.close();
               JSONObject jsonArray = getJsonString(jsonString,comfort);
                return jsonArray;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJsonString(String str, int comfort) throws Exception {
    JSONObject jo = null;
    if (comfort==1){
        return new JSONObject(str);
    }else if (comfort==2){
        int indexStart=0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) =='('){
                indexStart=i;
                break;
            }
        }
        StringBuilder strNew= new StringBuilder();
        for (int i = indexStart+1; i <str.length()-1; i++) {
            strNew.append(str.charAt(i));

        }
        return new JSONObject(strNew.toString());
    }
    return null;
    }
}
