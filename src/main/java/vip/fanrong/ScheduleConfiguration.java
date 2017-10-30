package vip.fanrong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import vip.fanrong.service.KdsCrawlerService;

import java.util.Calendar;

/**
 * Created by Rong on 2017/10/30.
 */
@Configuration
@EnableScheduling
public class ScheduleConfiguration implements SchedulingConfigurer {
    @Autowired
    KdsCrawlerService kdsCrawlerService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addCronTask(new Runnable() {
            @Override
            public void run() {
                if(kdsCrawlerService == null){
                    System.out.println("周期配置里无法拿到 KdsCrawlerService。。。。。");
                } else {
                    System.out.println("执行时间：" + Calendar.getInstance().getTime());
                    kdsCrawlerService.getHotTopics(20);
                }
            }
        }, "0 */30 * * * ?");
    }
}
