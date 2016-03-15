package cmds.service_entry;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.SocketAddress;

/**
 * cmds
 * Created by yeti on 16/3/7.
 */
public class RCConnectorManager {
    private static final RCConnectorManager SELF = new RCConnectorManager();

    public static RCConnectorManager instance() {
        return SELF;
    }

    public boolean connect(SocketAddress socketAddress) {
        throw new NotImplementedException();
    }

    public boolean disconnect(SocketAddress socketAddress) {
        throw new NotImplementedException();
    }

}
