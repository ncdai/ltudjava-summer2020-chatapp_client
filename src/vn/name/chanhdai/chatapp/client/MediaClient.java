package vn.name.chanhdai.chatapp.client;

import vn.name.chanhdai.chatapp.common.Config;
import vn.name.chanhdai.chatapp.common.MediaServerUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * vn.name.chanhdai.chatapp
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/23/20 - 5:29 PM
 * @description
 */

public class MediaClient {
    public static SocketChannel createChannel() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(Config.MEDIA_SERVER_HOST, Config.MEDIA_SERVER_PORT);
            socketChannel.connect(socketAddress);
            System.out.println(socketChannel.getLocalAddress() + " connect to " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socketChannel;
    }

    public static boolean downloadFile(String filePathToDownload) {
        return MediaServerUtils.downloadFile(MediaClient.createChannel(), filePathToDownload);
    }

    public static boolean uploadFile(String filePathToSend) {
        return MediaServerUtils.sendFileToSocket(MediaClient.createChannel(), filePathToSend);
    }
}
