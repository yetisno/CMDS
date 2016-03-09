package org.yetiz.serv.cmds.service_entry.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.yetiz.utils.cmds.PermitPair;
import org.yetiz.utils.cmds.service.CMDSChannelHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * cmds
 * Created by yeti on 16/3/7.
 */
public class RequestHandler extends CMDSChannelHandler {
    private static final String FUNC_URI = "v1.send";

    private static void badRequest(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST))
            .addListener(ChannelFutureListener.CLOSE);
    }

    private static void unAuthorized(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED))
            .addListener(ChannelFutureListener.CLOSE);
    }

    private static boolean checkPath(ChannelHandlerContext ctx, HttpRequest request) {
        if (!FUNC_URI.equals(new QueryStringDecoder(request.uri())
            .path()
            .substring(1)
            .replace('/', '.'))) {
            badRequest(ctx);
            return false;
        }

        return true;
    }

    private static void sendBroadcast(String project_id, ObjectNode body) {
        throw new NotImplementedException();
    }

    private static void sendGeneric(String project_id, ObjectNode body) {
        throw new NotImplementedException();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        if (!checkPath(ctx, request)) {
            return;
        }

        // Get post body
        int readLength = request.content().capacity();
        byte[] data = new byte[0];
        ObjectNode body = null;
        if (readLength > 0) {
            request.content().readBytes(data);
            body = (ObjectNode) JsonMapper.readTree(data);
        }

        PermitPair permitPair = null;
        // Check Secret
        try {
            permitPair = new PermitPair(request.headers().get("Authorization").getBytes());
        } catch (Throwable throwable) {
            unAuthorized(ctx);
        }

        // Get Name
        String project_id = permitPair.name();

        // Classify Request Type (Broadcast, Generic)
        boolean isBroadcast = body.get(RestParameter.broadcast).asBoolean(false);

        // If Broadcast, Send to all RC
        if (isBroadcast) {
            sendBroadcast(project_id, body);
        }

        // If Generic, Send to RC by DIDs
        else {
            sendGeneric(project_id, body);
        }

        request.release();
        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK))
            .addListener(ChannelFutureListener.CLOSE);
    }

    private class RestParameter {
        private static final String broadcast = "broadcast";
    }
}
