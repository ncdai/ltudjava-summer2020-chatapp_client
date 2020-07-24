package vn.name.chanhdai.chatapp.client;

import vn.name.chanhdai.chatapp.client.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 6:58 PM
 * @description
 */
public class AuthView extends JFrame {
    private Client client;

//    private JFrame mainFrame;

    private JTextField textFieldUsername;
    private JPasswordField passwordField;

    private JLabel jLabelRePassword;
    private JPasswordField rePasswordField;

    private JButton buttonSubmit;

    public AuthView() {
        createUI();
    }

    private void createUI() {
        client = new Client("localhost", 8080);
        if (!client.connect()) {
            return;
        }

        this.setTitle("Chat App");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();
            }
        });

        this.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(32, 64, 32, 64));
        form.setBackground(Color.WHITE);

        JLabel title = new JLabel("Đăng Nhập");
        title.setFont(new Font("", Font.BOLD, 20));
        form.add(title, ViewUtils.createFormConstraints(0, 0, 3, 0, 0, 8, 0));

        JRadioButton radioButtonLogin = ViewUtils.createRadioButton("Đăng Nhập", 128, 24, SwingConstants.CENTER);
        radioButtonLogin.addActionListener(e -> {
            setRePasswordVisible(false);
            resetForm();
        });
        JRadioButton radioButtonRegister = ViewUtils.createRadioButton("Đăng Ký", 128, 24, SwingConstants.CENTER);
        radioButtonRegister.addActionListener(e -> {
            setRePasswordVisible(true);
            resetForm();
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonLogin);
        buttonGroup.add(radioButtonRegister);

        form.add(radioButtonLogin, ViewUtils.createFormConstraints(0, 1, 1));
        form.add(radioButtonRegister, ViewUtils.createFormConstraints(1, 1, 1));

        form.add(new JLabel("Tên Đăng Nhập"), ViewUtils.createFormConstraints(0, 2, 1, 8, 8, 8, 0));
        textFieldUsername = new JTextField(10);
        form.add(textFieldUsername, ViewUtils.createFormConstraints(1, 2, 2, 8, 0));
        textFieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Pressed Enter -> Focus passwordField
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        form.add(new JLabel("Mật Khẩu"), ViewUtils.createFormConstraints(0, 3, 1));
        passwordField = new JPasswordField();
        form.add(passwordField, ViewUtils.createFormConstraints(1, 3, 2));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Pressed Enter
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (radioButtonRegister.isSelected()) {
                        // Register -> Focus xacNhanPasswordField
                        rePasswordField.requestFocus();
                    } else {
                        // Login -> Submit
                        buttonSubmit.doClick();
                    }
                }
            }
        });

        jLabelRePassword = new JLabel("Nhập Lại Mật Khẩu");
        form.add(jLabelRePassword, ViewUtils.createFormConstraints(0, 4, 1, 8, 0, 0, 0));
        rePasswordField = new JPasswordField();
        form.add(rePasswordField, ViewUtils.createFormConstraints(1, 4, 2, 8, 0, 0, 0));
        rePasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Pressed Enter -> Submit
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonSubmit.doClick();
                }
            }
        });

        buttonSubmit = new JButton("Đăng Nhập");
        form.add(buttonSubmit, ViewUtils.createFormConstraints(0, 5, 3, 8, 0, 0, 0));

        buttonSubmit.addActionListener(e -> {
            String username = textFieldUsername.getText();
            String password = String.valueOf(passwordField.getPassword());
            String rePassword = String.valueOf(rePasswordField.getPassword());

            if (username.equals("") || password.equals("") || (radioButtonRegister.isSelected() && rePassword.equals(""))) {
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (radioButtonLogin.isSelected()) {
                // Dang nhap
                login(username, password);
            } else {
                // Dang ky
                if (!password.equals(rePassword)) {
                    JOptionPane.showMessageDialog(null, "Nhập lại mật khẩu không khớp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    rePasswordField.setText("");
                    rePasswordField.requestFocus();
                    return;
                }
                register(username, password);
            }
        });

        JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        temp.setBorder(BorderFactory.createEmptyBorder(64, 64, 64, 64));
        temp.add(form);

        this.add(temp, BorderLayout.CENTER);

        JPanel panelFooter = ViewUtils.createFooter();
        this.add(panelFooter, BorderLayout.PAGE_END);

        this.pack();
        this.setLocationRelativeTo(null);

        radioButtonLogin.setSelected(true);
        setRePasswordVisible(false);

        this.setVisible(true);
    }

    private void login(String username, String password) {
        UserListPanel userListPanel = new UserListPanel(client);

        if (!client.login(username, password)) {
            return;
        }

        this.setVisible(false);

        JFrame userListFrame = new JFrame("User List");
        userListFrame.setLayout(new BorderLayout());
        userListFrame.setSize(new Dimension(500, 500));

        userListFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        userListFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();
            }
        });

        JPanel panelHeader = new JPanel();

        JButton button = new JButton("Logout");
        button.addActionListener(e -> {
            System.out.println("Click");
            client.disconnect();
            userListFrame.setVisible(false);
            setVisible(true);
        });
        panelHeader.add(button);

        userListFrame.getContentPane().add(panelHeader, BorderLayout.PAGE_START);
        userListFrame.getContentPane().add(userListPanel, BorderLayout.CENTER);

        userListFrame.setVisible(true);
    }

    private void register(String username, String password) {
        if (!client.register(username, password)) {
            JOptionPane.showMessageDialog(null, "Đăng ký không thành công!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("register ok");
        this.login(username, password);
    }

    private void resetForm() {
        passwordField.setText("");
        textFieldUsername.setText("");
        textFieldUsername.requestFocus();
    }

    private void setRePasswordVisible(boolean visible) {
        jLabelRePassword.setVisible(visible);
        rePasswordField.setVisible(visible);

        if (visible) {
            buttonSubmit.setText("Đăng Ký");
        } else {
            buttonSubmit.setText("Đăng Nhập");
        }
    }

//    public void setVisible(boolean visible) {
//        mainFrame.setVisible(visible);
//
//        if (visible) {
//            textFieldUsername.setText("");
//            passwordField.setText("");
//            rePasswordField.setText("");
//
//            textFieldUsername.requestFocus();
//        }
//    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);

        if (b) {
            textFieldUsername.setText("");
            passwordField.setText("");
            rePasswordField.setText("");

            textFieldUsername.requestFocus();
        }
    }
}
