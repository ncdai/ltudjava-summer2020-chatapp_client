package vn.name.chanhdai.chatapp.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 4:50 PM
 * @description
 */
public class UserListPanel extends JPanel implements UserStatusListener, MessageListener {
    Client client;

    JList<String> userList;
    JList<String> groupList;

    public UserListPanel(Client client) {
        createAndShowUI();

        this.client = client;

        this.client.addUserStatusListener(this);
        this.client.addMessageListener(this);
    }

    private void createAndShowUI() {
        userList = new JList<>();
        userList.setModel(new DefaultListModel<>());
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    String username = userList.getSelectedValue();
                    System.out.println("chat with " + username);
                    Thread thread = new Thread(() -> new MessageFrame(client, username).setVisible(true));
                    thread.start();
                }
            }
        });

        groupList = new JList<>();
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("#react");
        model.addElement("#node");
        model.addElement("#angular");
        groupList.setModel(model);

        groupList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    String groupKey = groupList.getSelectedValue();
                    System.out.println("chat group " + groupKey);
                    Thread thread = new Thread(() -> new MessageFrame(client, groupKey).setVisible(true));
                    thread.start();
                }
            }
        });

        JScrollPane scrollPaneUserList = new JScrollPane(userList);
        JScrollPane scrollPaneGroupList = new JScrollPane(groupList);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scrollPaneUserList);
        panel.add(scrollPaneGroupList);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    public void onOnline(String username) {
        System.out.println("online " + username);

        DefaultListModel<String> model = (DefaultListModel<String>) userList.getModel();
        model.addElement(username);
    }

    @Override
    public void onOffline(String username) {
        System.out.println("offline " + username);

        DefaultListModel<String> model = (DefaultListModel<String>) userList.getModel();
        model.removeElement(username);
    }

    @Override
    public void onReceiveMessage(String sender, String receiver, String type, String message) {
        System.out.println(type + " message from " + sender + " : " + message);
    }

//    public static void main(String[] args) {
//        Client client = new Client("localhost", 8080);
//        UserListPanel userListPanel = new UserListPanel(client);
//
//        JFrame frame = new JFrame("User List");
//        frame.setLayout(new BorderLayout());
//        frame.setSize(new Dimension(500, 500));
//
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                client.logout();
//            }
//        });
//
//        frame.getContentPane().add(userListPanel, BorderLayout.CENTER);
//        frame.setVisible(true);
//
//        if (!client.connect()) {
//            System.out.println("connect failed");
//            return;
//        }
//
//        System.out.println("connect ok");
//
//        client.login("ncdai", "ncdai");
//    }
}
