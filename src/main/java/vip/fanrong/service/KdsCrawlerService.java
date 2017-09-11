package vip.fanrong.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import vip.fanrong.common.util.CrawlerUtil;
import vip.fanrong.common.util.DateUtil;
import vip.fanrong.common.util.JsonUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Created by Rong on 2017/9/11.
 */
@Service
public class KdsCrawlerService {

    class Post {

        String title;
        String link;
        String imgUrl;

        long replyto;
        long userto;
        String aside;

        public Post(String title, String link, String imgUrl, long replyto, long userto, String aside) {
            this.title = title;
            this.link = link;
            this.imgUrl = imgUrl;
            this.replyto = replyto;
            this.userto = userto;
            this.aside = aside;
        }

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

        public long getReplyto() {
            return replyto;
        }

        public void setReplyto(long replyto) {
            this.replyto = replyto;
        }

        public long getUserto() {
            return userto;
        }

        public void setUserto(long userto) {
            this.userto = userto;
        }

        public String getAside() {
            return aside;
        }

        public void setAside(String aside) {
            this.aside = aside;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Post post = (Post) o;

            if (title != null ? !title.equals(post.title) : post.title != null) return false;
            return link != null ? link.equals(post.link) : post.link == null;
        }

        @Override
        public int hashCode() {
            int result = title != null ? title.hashCode() : 0;
            result = 31 * result + (link != null ? link.hashCode() : 0);
            return result;
        }
    }

    private String homepageUrl = "https://m.kdslife.com/";

    private Pattern postPattern = Pattern.compile("<li>.+?</li>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 支持匹配多行，忽略\n

    private Pattern imgPattern = Pattern.compile("<img src=\"(.*?)@");
    private Pattern titlePattern = Pattern.compile("title=\"(.*?)\" target");
    private Pattern linkPattern = Pattern.compile("<a href=\"(.*?\\.html)");

    private Pattern replytoPattern = Pattern.compile("<span class=\"replyto\">(.*?)</span>");
    private Pattern usertoPattern = Pattern.compile("<span class=\"userto\">(.*?)</span>");
    private Pattern asidePattern = Pattern.compile("<aside>(.*?)</aside>");

    private DateFormat asideFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ObjectNode getByReplyOrder(int pageNo) {
        String url = getReplyPageUrl(pageNo);
        return getNodeByUrl(url);
    }
    public ObjectNode getByCreateOrder(int pageNo) {
        String url = getCreatePageUrl(pageNo);
        return getNodeByUrl(url);
    }

    public ObjectNode getHotTopics(int limit) {
        Set<Post> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            String url = this.getReplyPageUrl(i);
            set.addAll(this.getPostList(url));
        }
        for (int i = 0; i < 5; i++) {
            String url = this.getCreatePageUrl(i);
            set.addAll(this.getPostList(url));
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 5); // 五天内
        List<Post> collect = set.stream().filter((Post t) -> {
            try {
                return asideFormat.parse(t.aside).after(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return true;
            }
        }).sorted(comparing(Post::getReplyto).reversed()).limit(limit).collect(toList());

        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("count", collect.size());
        objectNode.putPOJO("posts", collect);
        objectNode.put("date", DateUtil.getDateNow());
        return objectNode;
    }

    private String getReplyPageUrl(int pageNo) {
        String urlReply1 = "https://m.kdslife.com/f_15_0_2_";
        String urlReply2 = "_0.html";
        String url = urlReply1 + pageNo + urlReply2;
        return url;
    }

    private String getCreatePageUrl(int pageNo) {
        String urlCreate1 = "https://m.kdslife.com/f_15_0_3_";
        String urlCreate2 = "_0.html";
        String url = urlCreate1 + pageNo + urlCreate2;
        return url;
    }


    private ObjectNode getNodeByUrl(String url) {
        List<Post> posts = getPostList(url);

        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("count", posts.size());
        objectNode.putPOJO("posts", posts);
        objectNode.put("date", DateUtil.getDateNow());
        return objectNode;
    }

    private List<Post> getPostList(String url) {
        String html = CrawlerUtil.request(url);
        return parseHtml(html);
    }

    private List<Post> parseHtml(String pageHtml) {
        Matcher matcher = postPattern.matcher(pageHtml);
        List<Post> posts = new ArrayList<>();
        while (matcher.find()) {
            String postStr = matcher.group();

            Matcher imgUrlMat = imgPattern.matcher(postStr);
            String imgUrl = imgUrlMat.find() ? imgUrlMat.group(1).trim() : "";

            Matcher titleMatcher = titlePattern.matcher(postStr);
            String title = titleMatcher.find() ? titleMatcher.group(1).trim() : "";

            Matcher linkMatcher = linkPattern.matcher(postStr);
            String link = linkMatcher.find() ? homepageUrl + linkMatcher.group(1).trim() : "";

            Matcher replytoMatcher = replytoPattern.matcher(postStr);
            String replyto = replytoMatcher.find() ? replytoMatcher.group(1).trim() : "0";

            Matcher usertoMatcher = usertoPattern.matcher(postStr);
            String userto = usertoMatcher.find() ? usertoMatcher.group(1).trim() : "0";

            Matcher asideMatcher = asidePattern.matcher(postStr);
            String aside = asideMatcher.find() ? asideMatcher.group(1).trim() : "";


            if ("".equals(title) || "".equals(link)) {
                // remove AD
                continue;
            }

            posts.add(new Post(title, link, imgUrl, Long.parseLong(replyto), Long.parseLong(userto), aside));
        }
        return posts;
    }

}
