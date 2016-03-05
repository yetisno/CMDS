package org.yetiz.utils.cmds.utils;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class Lazy<R> {
    private LazyMethod<R> shell;
    private R result;

    private Lazy(LazyMethod lazyMethod) {
        this.shell = lazyMethod;
    }

    public static <T> Lazy<T> method(LazyMethod<T> lazyMethod) {
        return new Lazy(lazyMethod);
    }

    public R get() {
        if (result == null) {
            result = this.shell.execute();
        }

        return result;
    }

    public interface LazyMethod<T> {
        T execute();
    }
}
