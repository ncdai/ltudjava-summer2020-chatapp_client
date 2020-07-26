package vn.name.chanhdai.chatapp.client.view;

import vn.name.chanhdai.chatapp.client.dao.UserDAO;
import vn.name.chanhdai.chatapp.client.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * vn.name.chanhdai.chatapp.client.view
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/25/20 - 4:10 PM
 * @description
 */
public class UpdatePasswordView extends JFrame {
    JPasswordField currentPasswordField;
    JPasswordField newPasswordField;
    JButton buttonSubmit;

    public UpdatePasswordView() {
        createUI();
    }

    void createUI() {
        this.setTitle("DaiChat - Đổi Mật Khẩu");
        this.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(32, 64, 32, 64));
        form.setBackground(Color.WHITE);

        JLabel labelTitle = new JLabel("Đổi Mật Khẩu");
        labelTitle.setFont(new Font("", Font.BOLD, 20));
        form.add(labelTitle, ViewUtils.createFormConstraints(0, 0, 2));

        currentPasswordField = new JPasswordField(10);
        currentPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Pressed Enter -> Focus newPasswordField
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    newPasswordField.requestFocus();
                }
            }
        });
        form.add(new JLabel("Mật Khẩu Hiện Tại"), ViewUtils.createFormConstraints(0, 1, 1, 8, 8, 8, 0));
        form.add(currentPasswordField, ViewUtils.createFormConstraints(1, 1, 1, 8, 0));

        newPasswordField = new JPasswordField();
        newPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Pressed Enter -> Submit
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonSubmit.doClick();
                }
            }
        });

        form.add(new JLabel("Mật Khẩu Mới"), ViewUtils.createFormConstraints(0, 2, 1));
        form.add(newPasswordField, ViewUtils.createFormConstraints(1, 2, 1));

        buttonSubmit = new JButton("Đổi Mật Khẩu");
        buttonSubmit.addActionListener(e -> {
            String currentPassword = String.valueOf(currentPasswordField.getPassword());
            String newPassword = String.valueOf(newPasswordField.getPassword());

            if (currentPassword.equals("") || newPassword.equals("")) {
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            submit(currentPassword, newPassword);
        });
        form.add(buttonSubmit, ViewUtils.createFormConstraints(0, 3, 2, 8, 0, 0, 0));

        JPanel panelCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(64, 64, 64, 64));
        panelCenter.add(form);

        this.add(panelCenter, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);

        currentPasswordField.setText("");
        newPasswordField.setText("");
        currentPasswordField.requestFocus();
    }

    public void submit(String currentPassword, String newPassword) {
        boolean isSuccess = UserDAO.updatePassword("ncdai", currentPassword, newPassword);

        if (isSuccess) {
            JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);

            return;
        }

        JOptionPane.showMessageDialog(null, "Mật khẩu hiện tại không chính xác!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
