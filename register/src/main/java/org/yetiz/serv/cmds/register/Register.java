package org.yetiz.serv.cmds.register;

import org.yetiz.utils.cmds.service.Service;

import java.util.Map;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class Register implements Service {
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
