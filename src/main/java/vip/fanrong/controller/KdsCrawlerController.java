package vip.fanrong.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.fanrong.service.KdsCrawlerService;

/**
 * Created by Rong on 2017/7/14.
 */
@RestController
@RequestMapping(value = "/v1/kds") // parent folder for request mapping path
@Api(value = "KDS Crawler API", description = "v1")
public class KdsCrawlerController {

    @Autowired
    private KdsCrawlerService kdsCrawlerService;

    @ApiOperation(value = "获取近期热门帖", notes = "按照回帖数量排行")
    @RequestMapping(value = "/getHotTopics", method = RequestMethod.GET)
    public ObjectNode getHotTopics(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        return kdsCrawlerService.getHotTopics(limit);
    }


    @ApiOperation(value = "获取最新回复的帖子", notes = "可以指定第几页，默认第一页")
    @RequestMapping(value = "/getReply", method = RequestMethod.GET)
    public ObjectNode getByReplyOrder(@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
        String urlReply1 = "https://m.kdslife.com/f_15_0_2_";
        String urlReply2 = "_0.html";
        String url = urlReply1 + pageNo + urlReply2;
        return kdsCrawlerService.getNodeByUrl(url);
    }

    @ApiOperation(value = "获取最新发布的帖子", notes = "可以指定第几页，默认第一页")
    @RequestMapping(value = "/getCreate", method = RequestMethod.GET)
    public ObjectNode getByCreateOrder(@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
        String urlCreate1 = "https://m.kdslife.com/f_15_0_3_";
        String urlCreate2 = "_0.html";
        String url = urlCreate1 + pageNo + urlCreate2;
        return kdsCrawlerService.getNodeByUrl(url);
    }


}
