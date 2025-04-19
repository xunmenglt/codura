package com.xunmeng.codura.scheduler;

import com.intellij.util.concurrency.AppExecutorUtil;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedulerTest {
    @Test
    public void schedulerTest() throws InterruptedException {
        ScheduledExecutorService scheduler = AppExecutorUtil.createBoundedScheduledExecutorService("SMCCompletionScheduler", 1);
        System.out.println(new Date().getTime());
        ScheduledFuture<?> schedule = scheduler.schedule(() -> {
            System.out.println("哈哈哈" + new Date().getTime());
        }, 5, TimeUnit.SECONDS);
        Thread.sleep(2000);
        schedule.cancel(true);
        System.out.println(new Date().getTime());
    }
}
