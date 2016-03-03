package org.yetiz.utils.cmds;

import java.util.Map;

/**
 * cmds
 * Created by yeti on 16/2/29.
 */
public interface Service {
    boolean start(Object... params);

    boolean stop(Object... params);

    boolean reload(Object... params);

    boolean restart(Object... params);

    Map<String, Object> status(Object... params);
}
