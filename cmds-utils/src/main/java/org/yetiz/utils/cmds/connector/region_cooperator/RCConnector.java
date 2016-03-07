package org.yetiz.utils.cmds.connector.region_cooperator;

import org.yetiz.utils.cmds.messages.BroadcastMessage;
import org.yetiz.utils.cmds.messages.GenericMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.SocketAddress;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class RCConnector {

    public boolean connect(SocketAddress socketAddress) {
        throw new NotImplementedException();
    }

    public boolean broadcast(BroadcastMessage broadcastMessage) {
        throw new NotImplementedException();
    }

    public boolean generic(GenericMessage genericMessage) {
        throw new NotImplementedException();
    }
}
