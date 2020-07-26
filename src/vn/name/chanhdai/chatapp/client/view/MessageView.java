package vn.name.chanhdai.chatapp.client.view;

import vn.name.chanhdai.chatapp.client.Client;
import vn.name.chanhdai.chatapp.client.MediaClient;
import vn.name.chanhdai.chatapp.client.event.MessageListener;
import vn.name.chanhdai.chatapp.client.utils.EmojiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class MessageItem {
    private String type;
    private String text;
    private String sender;

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
        String textColor2;
        String fullName;

        if (sender != null && sender.equals(this.me)) {
            setHorizontalAlignment(RIGHT);
            backgroundColor = "#eeeeee";
            textColor = "#000000";
            textColor2 = "#9b9b9b";
            fullName = "";
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
public class MessageView extends JFrame implements MessageListener {
    private final Client client;
    private final String receiver;
    private final boolean isGroup;

    private JList<MessageItem> messageList;
    private JTextField textFieldMessage;
    private JFileChooser fileChooser;
    JScrollPane scrollPaneMessageList;

    public MessageView(Client client, String username) {
        this.client = client;
        this.client.addMessageListener(this);
        this.receiver = username;

        boolean isGroup = this.receiver.charAt(0) == '#';
        this.isGroup = isGroup;
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
            boolean isSuccess = MediaClient.uploadFile(filePath);
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
            boolean isSuccess = MediaClient.downloadFile(destDir);
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

        this.setTitle("DaiChat - " + client.getUser() + " và " + this.receiver);
        this.setSize(new Dimension(600, 500));
        this.setLayout(new BorderLayout());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isGroup) {
                    client.leave(receiver);
                }
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
        if (sender.equals(this.receiver) || receiver.equals(this.receiver)) {
            DefaultListModel<MessageItem> model = (DefaultListModel<MessageItem>) messageList.getModel();

            MessageItem messageItem = new MessageItem();
            messageItem.setType(type);
            messageItem.setText(message);
            messageItem.setSender(sender);

            model.addElement(messageItem);

            EventQueue.invokeLater(() -> {
                JScrollBar bar = scrollPaneMessageList.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        textFieldMessage.requestFocus();
    }
}
