package vip.fanrong.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import vip.fanrong.common.util.CrawlerUtil;
import vip.fanrong.common.util.DateUtil;
import vip.fanrong.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rong on 2017/7/14.
 */
@RestController
@RequestMapping(value = "/v1/kds") // parent folder for request mapping path
@Api(value = "KDS Crawler API", description = "v1")
public class KdsCrawlerController {

    private String UrlReply1 = "https://m.kdslife.com/f_15_0_2_";
    private String UrlReply2 = "_0.html";

    private String homepageUrl = "https://m.kdslife.com/";

    private Pattern postPattern = Pattern.compile("<li>.+?</li>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 支持匹配多行，忽略\n

    private Pattern imgPattern = Pattern.compile("<img src=\"(.*?)@");
    private Pattern titlePattern = Pattern.compile("title=\"(.*?)\" target");
    private Pattern linkPattern = Pattern.compile("<a href=\"(.*?\\.html)");


    @ApiOperation(value = "获取最新回复的帖子", notes = "可以指定第几页，默认第一页")
    @RequestMapping(value = "/getFromPage", method = RequestMethod.GET)
    public ObjectNode getLatestReply(@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {

        String url = UrlReply1 + pageNo + UrlReply2;

        String html = CrawlerUtil.request(url);
        List<Post> posts = getPostsFromOnePage(html);

        ObjectNode objectNode = JsonUtil.createObjectNode();

        objectNode.putPOJO("posts", posts);
        return objectNode;
    }

    private List<Post> getPostsFromOnePage(String pageHtml) {
        Matcher matcher = postPattern.matcher(pageHtml);
        List<Post> posts = new ArrayList<>();
        while (matcher.find()) {
            String postStr = matcher.group();

            Matcher imgUrlMat = imgPattern.matcher(postStr);
            String imgUrl = imgUrlMat.find() ? imgUrlMat.group(1) : "";

            Matcher titleMatcher = titlePattern.matcher(postStr);
            String title = titleMatcher.find() ? titleMatcher.group(1) : "";

            Matcher linkMather = linkPattern.matcher(postStr);
            String link = linkMather.find() ? homepageUrl + linkMather.group(1) : "";

            posts.add(new Post(title, link, imgUrl));
        }
        return posts;
    }

    class Post {
        public Post(String title, String link, String imgUrl) {
            this.title = title;
            this.link = link;
            this.imgUrl = imgUrl;
        }

        String title;
        String link;
        String imgUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        @Override
        public String toString() {
            return "Post{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    '}';
        }
    }
}
