package org.yetiz.serv.cmds.service_entry;

import org.yetiz.utils.cmds.Service;

import java.util.Map;

/**
 * cmds
 * Created by yeti on 16/2/29.
 */
public class ServiceEntry implements Service {
    @Override
    public boolean start(Object... params) {
        return false;
    }

    @Override
    public boolean stop(Object... params) {
        return false;
    }

    @Override
    public boolean reload(Object... params) {
        return false;
    }

    @Override
    public boolean restart(Object... params) {
        return false;
    }

    @Override
    public Map<String, Object> status(Object... params) {
        return null;
    }
}
