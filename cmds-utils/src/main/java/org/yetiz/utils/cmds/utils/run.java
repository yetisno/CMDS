package org.yetiz.utils.cmds.utils;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class Run {

    public static void withoutException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
        }
    }
}
