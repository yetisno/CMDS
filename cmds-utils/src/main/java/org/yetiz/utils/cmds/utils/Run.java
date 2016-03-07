package org.yetiz.utils.cmds.utils;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class Run {

    public static void withoutException(RunTask runTask) {
        try {
            runTask.run();
        } catch (Throwable throwable) {
        }
    }

    public static <T> T withoutException(RunGenericTask<T> runGenericTask) {
        try {
            return runGenericTask.run();
        } catch (Throwable throwable) {
        }

        return null;
    }

    public interface RunTask extends Runnable {
    }

    public interface RunGenericTask<T> {
        T run();
    }
}
