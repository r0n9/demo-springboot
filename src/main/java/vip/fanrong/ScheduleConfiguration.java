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
        scheduledTaskRegistrar.addCronTask(() -> kdsCrawlerService.getHotTopics(20), "0 0/30 * * * ?");
    }
}
