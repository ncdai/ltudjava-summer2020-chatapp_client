package vn.name.chanhdai.chatapp.client.view;

import vn.name.chanhdai.chatapp.client.Client;
import vn.name.chanhdai.chatapp.client.event.MessageListener;
import vn.name.chanhdai.chatapp.client.event.UserStatusListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ListRenderer extends JLabel implements ListCellRenderer<String> {
    @Override
    public Component getListCellRendererComponent(
        JList<? extends String> list,
        String item,
        int index, boolean isSelected,
        boolean cellHasFocus
    ) {
        setText("<html><div style='padding: 8px 16px;'>" + item + "</div></html>");
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        return this;
    }
}

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
        userList.setCellRenderer(new ListRenderer());
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String username = userList.getSelectedValue();
                    openChat(username);
                }
            }
        });

        DefaultListModel<String> groupListModel = new DefaultListModel<>();
        groupListModel.addElement("#react");
        groupListModel.addElement("#node");
        groupListModel.addElement("#angular");

        groupList = new JList<>();
        groupList.setModel(groupListModel);
        groupList.setCellRenderer(new ListRenderer());
        groupList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String groupKey = groupList.getSelectedValue();
                    openChat(groupKey);
                }
            }
        });

        JScrollPane scrollPaneUserList = new JScrollPane(userList);
        scrollPaneUserList.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JScrollPane scrollPaneGroupList = new JScrollPane(groupList);
        scrollPaneGroupList.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel labelUserList = new JLabel("<html><div style='padding: 8px 16px;'>Đang Online</div></html>");
        labelUserList.setOpaque(true);
        labelUserList.setBackground(Color.decode("#eeeeee"));

        JPanel panelUserList = new JPanel(new BorderLayout());
        panelUserList.add(labelUserList, BorderLayout.PAGE_START);
        panelUserList.add(scrollPaneUserList, BorderLayout.CENTER);

        JLabel labelGroupList = new JLabel("<html><div style='padding: 8px 16px;'>Nhóm</div></html>");
        labelGroupList.setOpaque(true);

        JButton buttonJoinGroup = new JButton("Tham Gia Nhóm");
        buttonJoinGroup.setPreferredSize(new Dimension(144, 24));
        buttonJoinGroup.setFocusPainted(false);
        buttonJoinGroup.addActionListener(e -> {
            String groupKey = JOptionPane.showInputDialog(null, "Nhập Mã Nhóm (VD : #join_group_name)", "Tham Gia Nhóm", JOptionPane.INFORMATION_MESSAGE);
            if (groupKey != null) {
                groupListModel.addElement(groupKey);
                openChat(groupKey);
            }
        });

        JButton buttonLeaveGroup = new JButton("Rời Nhóm");
        buttonLeaveGroup.setPreferredSize(new Dimension(144, 24));
        buttonLeaveGroup.setBackground(Color.decode("#fafafa"));
        buttonLeaveGroup.addActionListener(e -> {
            String groupKey = JOptionPane.showInputDialog(null, "Nhập Mã Nhóm (VD : #leave_group_name)", "Rời Nhóm", JOptionPane.INFORMATION_MESSAGE);
            if (groupKey != null) {
                groupListModel.removeElement(groupKey);
            }
        });

        JPanel panelGroupHeader = new JPanel();
        panelGroupHeader.setLayout(new BoxLayout(panelGroupHeader, BoxLayout.X_AXIS));
        panelGroupHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        panelGroupHeader.setBackground(Color.decode("#eeeeee"));
        panelGroupHeader.add(labelGroupList);
        panelGroupHeader.add(Box.createHorizontalGlue());
        panelGroupHeader.add(buttonLeaveGroup);
        panelGroupHeader.add(Box.createRigidArea(new Dimension(5, 0)));
        panelGroupHeader.add(buttonJoinGroup);

        JPanel panelGroupList = new JPanel(new BorderLayout());
        panelGroupList.add(panelGroupHeader, BorderLayout.PAGE_START);
        panelGroupList.add(scrollPaneGroupList, BorderLayout.CENTER);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridLayout(2, 1));
        panelMain.add(panelUserList);
        panelMain.add(panelGroupList);

        this.setLayout(new BorderLayout());
        this.add(panelMain, BorderLayout.CENTER);
    }

    void openChat(String key) {
        System.out.println("chat with " + key);
        Thread thread = new Thread(() -> new MessageView(client, key).setVisible(true));
        thread.start();
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
