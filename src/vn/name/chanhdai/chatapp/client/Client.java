package vn.name.chanhdai.chatapp.client;

import org.apache.commons.lang3.StringUtils;
import vn.name.chanhdai.chatapp.client.event.MessageListener;
import vn.name.chanhdai.chatapp.client.event.UserStatusListener;
import vn.name.chanhdai.chatapp.client.utils.MessageUtils;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 1:45 PM
 * @description
 */
public class Client {
    private final String serverName;
    private final int serverPort;

    private OutputStream serverOutputStream;
    private BufferedReader serverBufferedReader;

    private final ArrayList<UserStatusListener> userStatusListenerList = new ArrayList<>();
    private final ArrayList<MessageListener> messageListenerList = new ArrayList<>();

    private String user;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public String getUser() {
        return this.user;
    }

    public boolean login(String username, String password) {
        try {
            String command = "login " + username + " " + password + "\n";
            this.serverOutputStream.write(command.getBytes());

            String response = this.serverBufferedReader.readLine();
            System.out.println("server response " + response);

            if (response.equals("login ok")) {
                this.user = username;

                Thread thread = new Thread(() -> {
                    try {
                        String line;
                        while ((line = serverBufferedReader.readLine()) != null) {
                            System.out.println("server response " + line);

                            String[] tokens = StringUtils.split(line);
                            if (tokens != null && tokens.length > 0) {
                                String command1 = tokens[0];
                                switch (command1) {
                                    case "online":
                                        handleOnline(tokens);
                                        break;
                                    case "offline":
                                        handleOffline(tokens);
                                        break;
                                    case "message":
                                        String[] newTokens = StringUtils.split(line, " ", 3);
                                        handleReceiveMessage(newTokens);
                                        break;
                                }
                            }
                        }
                    } catch (IOException ioException) {
                        System.err.println("Client.java -> login() -> thread -> IOException");
                        ioException.printStackTrace();
                    }
                });
                thread.start();

                return true;
            }
        } catch (IOException ioException) {
            System.err.println("Client.java -> login() -> IOException");
            ioException.printStackTrace();
        }

        return false;
    }

    public void sendMessage(String receiver, String message) {
        try {
            String command = "message " + receiver + " " + message + "\n";
            this.serverOutputStream.write(command.getBytes());
        } catch (IOException ioException) {
            System.err.println("Client.java -> sendMessage() -> IOException");
            ioException.printStackTrace();
        }
    }

    private void handleReceiveMessage(String[] tokens) {
        if (tokens.length != 3) {
            return;
        }

        String[] senderAndReceiver = MessageUtils.getSenderAndReceiver(tokens[1]);
        String[] typeAndText = MessageUtils.getTypeAndText(tokens[2]);

        String sender = senderAndReceiver[0];
        String receiver = senderAndReceiver[1];
        String type = typeAndText[0];
        String text = typeAndText[1];

        for (MessageListener messageListener : messageListenerList) {
            messageListener.onReceiveMessage(sender, receiver, type, text);
        }
    }

    private void handleOffline(String[] tokens) {
        if (tokens.length != 2) {
            return;
        }

        String username = tokens[1];
        for (UserStatusListener userStatusListener : userStatusListenerList) {
            userStatusListener.onOffline(username);
        }
    }

    private void handleOnline(String[] tokens) {
        if (tokens.length != 2) {
            return;
        }

        String username = tokens[1];
        for (UserStatusListener userStatusListener : userStatusListenerList) {
            userStatusListener.onOnline(username);
        }
    }

    public boolean connect() {
        try {
            // Máy khách khởi tạo đối tượng Socket,
            // Xác định ServerName (IP hoặc Domain) và ServerPort để kết nối
            Socket socket = new Socket(this.serverName, this.serverPort);

            this.serverOutputStream = socket.getOutputStream();
            this.serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            return true;

        } catch (ConnectException connectException) {
            System.err.println("Client.java -> connect() -> ConnectException");
        } catch (IOException ioException) {
            System.err.println("Client.java -> connect() -> IOException");
            this.disconnect();
        }

        return false;
    }

    public void disconnect() {
        try {
            String command = "logout\n";
            this.serverOutputStream.write(command.getBytes());
        } catch (IOException ioException) {
            System.err.println("Client.java -> logout() -> IOException");
            ioException.printStackTrace();
        }
    }

    public void join(String groupKey) {
        try {
            String command = "join " + groupKey + "\n";
            serverOutputStream.write(command.getBytes());
        } catch (IOException ioException) {
            System.err.println("Client.java -> join() -> IOException");
            ioException.printStackTrace();
        }
    }

    public void leave(String groupKey) {
        try {
            String command = "leave " + groupKey + "\n";
            serverOutputStream.write(command.getBytes());
        } catch (IOException ioException) {
            System.err.println("Client.java -> join() -> IOException");
            ioException.printStackTrace();
        }
    }

    public void addUserStatusListener(UserStatusListener userStatusListener) {
        this.userStatusListenerList.add(userStatusListener);
    }

//    public void removeUserStatusListener(UserStatusListener userStatusListener) {
//        this.userStatusListenerList.remove(userStatusListener);
//    }

    public void addMessageListener(MessageListener messageListener) {
        this.messageListenerList.add(messageListener);
    }

//    public void removeMessageListener(MessageListener messageListener) {
//        System.out.println("removeMessageListener(" + messageListener.toString() + ")");
//        this.messageListenerList.remove(messageListener);
//    }
}
