package com.udabe.cmmn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class PoolThreadConfig{

    private static int corePoolSize = 5;
    private static int maximumPoolSize = 100;
    private static long keepAliveTime = 500;
    private static TimeUnit unit = TimeUnit.SECONDS;
    private static  int workQueueCapacity = 100;

    private static ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(workQueueCapacity);

    private static RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }
}
