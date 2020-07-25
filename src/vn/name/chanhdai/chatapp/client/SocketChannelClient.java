package vn.name.chanhdai.chatapp.client;

import vn.name.chanhdai.chatapp.common.Config;

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

public class SocketChannelClient {
//    public static void main(String[] args) {
//        new Thread(() -> SocketChannelUtils.sendFileToSocket(createChannel(), "/Users/ncdai3651408/Desktop/2017_2380_QD_CTN.pdf")).start();
//        new Thread(() -> SocketChannelUtils.sendFileToSocket(createChannel(), "/Users/ncdai3651408/Desktop/1.jpg")).start();
//
//        new Thread(() -> {
//            boolean isSuccess = SocketChannelUtils.downloadFile(createChannel(), "/Users/ncdai3651408/Workplace/2017_2380_QD_CTN.pdf");
//            if (isSuccess) {
//                System.out.println("download ok");
//            } else {
//                System.err.println("download failed");
//            }
//        }).start();
//    }

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
}
