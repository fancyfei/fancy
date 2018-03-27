package com.fancy.common.util.comm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2018/3/27.
 */
public class QueueProcess {

    private static Queue<Object> needProcessQueue = new ConcurrentLinkedQueue<>();

    /**
     * 放缓存
     * @param geocode
     */
    public void push(Object geocode) {
        needProcessQueue.add(geocode);
    }
    /**
     * 启动处理线程
     */
    public void insertWithThread() {
        new Thread(() -> {
            int sleepTime = 10;
            while (true) {
                Object geo = needProcessQueue.poll();
                if (null == geo) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                try {
                    System.out.println("发现数据，开始处理");
                    // TODO 处理业务
                    System.out.println("处理业务");
                } catch (Exception e) {
                    System.out.println("处理数据失败");
                }
            }
        }, "QueueThread").start();
    }

}