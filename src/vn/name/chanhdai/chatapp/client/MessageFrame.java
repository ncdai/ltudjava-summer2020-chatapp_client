package vn.name.chanhdai.chatapp.client;

import vn.name.chanhdai.chatapp.client.utils.EmojiUtils;
import vn.name.chanhdai.chatapp.common.SocketChannelUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.channels.SocketChannel;

class MessageItem {
    private String type;
    private String text;

    private String sender;
    private String receiver;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

class MessageRenderer extends JLabel implements ListCellRenderer<MessageItem> {
    private final String me;

    MessageRenderer(String user) {
        this.me = user;
    }

    @Override
    public Component getListCellRendererComponent(
        JList<? extends MessageItem> list,
        MessageItem messageItem,
        int index, boolean isSelected,
        boolean cellHasFocus
    ) {
        String sender = messageItem.getSender();
        String type = messageItem.getType();
        String text = messageItem.getText();

        String backgroundColor;
        String textColor;
        String fullName = "";
        String textColor2;

        if (sender != null && sender.equals(this.me)) {
            setHorizontalAlignment(RIGHT);
            backgroundColor = "#eeeeee";
            textColor = "#000000";
            textColor2 = "#9b9b9b";
        } else {
            setHorizontalAlignment(LEFT);
            backgroundColor = "#3366FF";
            textColor = "#ffffff";
            textColor2 = "#B5C7FF";
            fullName = "<div style='color: " + textColor2 + "; margin-bottom: 4px'>" + sender + "</div>";
        }

        if (type.equals("emoji")) {
            setIcon(EmojiUtils.getImage(text, "large"));
            setText(null);
            setBorder(BorderFactory.createEmptyBorder(12, 16, 20, 16));
        } else if (type.equals("file")) {
            setIcon(null);
            setText("<html>" +
                "<div style='background-color: " + backgroundColor + "; color: " + textColor + "; padding: 8px 16px;'>" +
                "<div style='color: " + textColor2 + "; margin-bottom: 4px'>Double Click to Download</div>" +
                "<div>" + text + "</div>" +
                "</div>" +
                "</html>");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        } else {
            setIcon(null);
            setText("<html>" +
                "<div style='background-color: " + backgroundColor + "; color: " + textColor + "; padding: 8px 16px;'>" +
                fullName +
                "<div>" + text + "</div>" +
                "</div>" +
                "</html>");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        }

        return this;
    }
}

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 5:19 PM
 * @description
 */
public class MessageFrame extends JFrame implements MessageListener {
    private final Client client;
    private final String receiver;

    private JList<MessageItem> messageList;
    private JTextField textFieldMessage;
    private JFileChooser fileChooser;
    JScrollPane scrollPaneMessageList;

    public MessageFrame(Client client, String username) {
        this.client = client;
        this.client.addMessageListener(this);
        this.receiver = username;

        boolean isGroup = this.receiver.charAt(0) == '#';
        if (isGroup) {
            this.client.join(this.receiver);
        }

        this.createUI();
    }

    void uploadClick() {
        fileChooser.setDialogTitle("Chọn File Tải Lên");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        System.out.println("upload " + filePath);

        new Thread(() -> {
            SocketChannel socketChannel = SocketChannelClient.createChannel();
            boolean isSuccess = SocketChannelUtils.sendFileToSocket(socketChannel, filePath);
            if (isSuccess) {
                this.client.sendMessage(this.receiver, "file=" + new File(filePath).getName());
                System.out.println("Upload Success");
            } else {
                System.out.println("Upload Error cmnr");
            }
            textFieldMessage.requestFocus();
        }).start();
    }

    void downloadClick(String fileName) {
        fileChooser.setDialogTitle("Chọn Thư Mục Lưu File");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();

        String destDir = file.getAbsolutePath() + "/" + fileName;
        System.out.println("download " + fileName);
        System.out.println("dest " + destDir);

        new Thread(() -> {
            boolean isSuccess = SocketChannelUtils.downloadFile(SocketChannelClient.createChannel(), destDir);
            if (isSuccess) {
                JOptionPane.showMessageDialog(null, "Đã tải xuống File thành công (" + destDir + ")", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Lỗi tải xuống File! Bạn hãy thử lại sau!", "Thông Báo", JOptionPane.ERROR_MESSAGE);
            }
            textFieldMessage.requestFocus();
        }).start();
    }

    void createUI() {
        fileChooser = new JFileChooser();

        this.setTitle(client.getUser() + " - " + this.receiver);

        this.setSize(new Dimension(600, 500));
        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();
            }
        });

        messageList = new JList<>(new DefaultListModel<>());
        textFieldMessage = new JTextField();
        textFieldMessage.setPreferredSize(new Dimension(120, 32));
        textFieldMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        JButton buttonUpload = new JButton("Gửi File");
        buttonUpload.setBackground(Color.decode("#eeeeee"));
        buttonUpload.setFocusPainted(false);
        buttonUpload.addActionListener(e -> this.uploadClick());

        JButton buttonSend = new JButton("Gửi");
        buttonSend.setFocusPainted(false);
        buttonSend.addActionListener(e -> sendMessage());

        JPanel panelFooter = new JPanel();
        panelFooter.setLayout(new BorderLayout());
        panelFooter.setBackground(Color.decode("#fafafa"));
        panelFooter.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));

        JPanel panelEmoji = new JPanel();
        panelEmoji.setLayout(new GridLayout(1, 8));
        panelEmoji.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panelEmoji.setBackground(Color.decode("#fafafa"));

        for (String emoji : EmojiUtils.getAllEmoji()) {
            JButton button = new JButton(EmojiUtils.getImage(emoji, EmojiUtils.SMALL));
            button.setPreferredSize(new Dimension(32, 32));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setBackground(null);
            button.addActionListener(e -> client.sendMessage(this.receiver, "emoji=" + emoji));
            panelEmoji.add(button);
        }

        panelFooter.add(panelEmoji, BorderLayout.PAGE_START);

        JPanel panelFooterRight = new JPanel();
        panelFooterRight.setBackground(Color.decode("#fafafa"));
        panelFooterRight.setLayout(new GridLayout(1, 2, 8, 0));
        panelFooterRight.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        panelFooterRight.add(buttonUpload);
        panelFooterRight.add(buttonSend);

        panelFooter.add(textFieldMessage, BorderLayout.CENTER);
        panelFooter.add(panelFooterRight, BorderLayout.LINE_END);

        DefaultListModel<MessageItem> messageListModel = new DefaultListModel<>();

//        MessageItem item1 = new MessageItem();
//        item1.setType("text");
//        item1.setText("Hello World!");
//        item1.setSender("ncdai");
//        item1.setReceiver("nttam");
//
//        MessageItem item2 = new MessageItem();
//        item2.setType("text");
//        item2.setText("Goodbye, see you again!");
//        item2.setSender("nttam");
//        item2.setReceiver("ncdai");

//        listModel.addElement(item1);
//        listModel.addElement(item2);

        messageList = new JList<>(messageListModel);
        messageList.setCellRenderer(new MessageRenderer(this.client.getUser()));
        messageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    MessageItem messageItem = messageList.getSelectedValue();
                    if (messageItem.getType().equals("file")) {
                        String fileName = messageItem.getText();
                        downloadClick(fileName);
                    }
                }
            }
        });

        scrollPaneMessageList = new JScrollPane(messageList);
        scrollPaneMessageList.setBorder(null);

        JLabel title = new JLabel("<html><h1 style='margin-top: 0; margin-bottom: 0'>" + this.receiver + "</h1></html>");

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 16));
        panelHeader.add(title);

        this.add(panelHeader, BorderLayout.PAGE_START);
        this.add(scrollPaneMessageList, BorderLayout.CENTER);
        this.add(panelFooter, BorderLayout.PAGE_END);

        this.setLocationRelativeTo(null);
    }

    void sendMessage() {
        String message = textFieldMessage.getText();
        if (message.equals("")) {
            return;
        }

        client.sendMessage(this.receiver, "text=" + message);
        textFieldMessage.setText("");
    }

    @Override
    public void onReceiveMessage(String sender, String receiver, String type, String message) {
        if (sender.equalsIgnoreCase(this.receiver) || receiver.equalsIgnoreCase(this.receiver)) {
            DefaultListModel<MessageItem> model = (DefaultListModel<MessageItem>) messageList.getModel();

            MessageItem messageItem = new MessageItem();
            messageItem.setType(type);
            messageItem.setText(message);
            messageItem.setSender(sender);
            messageItem.setReceiver(receiver);

            model.addElement(messageItem);

            EventQueue.invokeLater(() -> {
                JScrollBar bar = scrollPaneMessageList.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        }
    }

    public static void main(String[] args) {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle Exception
            System.out.println("Error!");
        }

        EventQueue.invokeLater(() -> {
            Client client = new Client("localhost", 8080);
            client.connect();
            client.login("ncdai", "ncdai");

            new MessageFrame(client, "nttam").setVisible(true);
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        textFieldMessage.requestFocus();
    }
}
