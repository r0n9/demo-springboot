package vip.fanrong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rong on 2017/7/17.
 */
@RestController
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "<h1>Welcome!</h1>" +
                " <br> " +
                "<p>Click <a href=\"swagger-ui.html#\">here</a> to see the API doc.</p>";
    }
}
