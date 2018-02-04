package com.link.admin.task;

import com.jfinal.kit.LogKit;

/**
 * Created by linkzz on 2017-06-16.
 */
public class MyTask implements Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LogKit.info("执行了定时任务！");
    }
}
