package vip.fanrong.testapi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rong on 2017/7/14.
 */
public class Main {

    private static String httpUrlHello = "http://fanrong.vip:8090/v1/hello";

    public static void main(String[] args) {
        String jsonResult = request(httpUrlHello, "fanrong");
        System.out.println(jsonResult);
    }

    private static String request(String httpUrl, String httpArg) {

        String urlStr = httpUrl + "/" + httpArg;

        BufferedReader reader;
        String result = null;
        StringBuilder sbf = new StringBuilder();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            // connection.setRequestProperty("apikey", "您自己的apikey");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
