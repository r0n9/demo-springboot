package vip.fanrong.testapi;

import vip.fanrong.common.util.CrawlerUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rong on 2017/7/14.
 */
public class Main {

    private static String httpUrlHello = "http://fanrong.vip:8090/v1/hello";
    private static Pattern postPattern = Pattern.compile("<li>.+?</li>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 支持匹配多行，忽略\n

    public static void main(String[] args) {
        String jsonResult = request("https://m.kdslife.com/f_15_0_2.html", "");
        for (String post : getPostsFromOnePage(jsonResult)) {
            System.out.println(post);
        }

    }

    private static String request(String httpUrl, String httpArg) {

        String urlStr = httpUrl + "/" + httpArg;

        return CrawlerUtil.request(urlStr);
    }

    private static List<String> getPostsFromOnePage(String pageHtml) {
        Matcher matcher = postPattern.matcher(pageHtml);
        List<String> posts = new ArrayList<>();
        while (matcher.find()) {
            String post = matcher.group();
            posts.add(matcher.group());
        }
        return posts;
    }

}
