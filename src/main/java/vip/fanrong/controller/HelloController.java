package vip.fanrong.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Rong on 2017/7/13.
 */
@RestController
@RequestMapping(value = "/v1") // parent folder for request mapping path
@Api(value = "HelloWorld API", description = "第一版")
public class HelloController {

    @ApiOperation(value = "获取消息", notes = "获取第一个HelloWorld消息")
    @ApiImplicitParam(paramType = "path", name = "name", value = "姓名", required = true, dataType = "String")
    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String sayHello(@PathVariable String name) {
        return "Hello World！" + name;
    }
}
