package vip.fanrong.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import vip.fanrong.common.util.DateUtil;
import vip.fanrong.common.util.JsonUtil;

/**
 * Created by Rong on 2017/7/13.
 */
@RestController
@RequestMapping(value = "/v1/hello") // parent folder for request mapping path
@Api(value = "HelloWorld API", description = "v1")
public class HelloController {

    @ApiOperation(value = "获取问候消息", notes = "获取来自系统的问候消息")
    @ApiImplicitParam(paramType = "path", name = "name", value = "姓名", required = true, dataType = "String")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ObjectNode sayHello(@PathVariable String name) {
        ObjectNode objectNode = JsonUtil.createObjectNode();
        objectNode.put("name", name);
        objectNode.put("greeting", "你好！");
        objectNode.put("date", DateUtil.getDateNow());
        return objectNode;
    }
}
