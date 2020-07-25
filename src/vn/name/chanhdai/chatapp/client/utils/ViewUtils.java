package vn.name.chanhdai.chatapp.client.utils;

import vn.name.chanhdai.chatapp.client.Client;

import javax.swing.*;
import java.awt.*;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 6:59 PM
 * @description
 */
public class ViewUtils {
    public static GridBagConstraints createFormConstraints(int x, int y, int width, int paddingTop, int paddingRight, int paddingBottom, int paddingLeft) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.insets = new Insets(paddingTop, paddingLeft, paddingBottom, paddingRight);

        return c;
    }

    public static GridBagConstraints createFormConstraints(int x, int y, int width) {
        return createFormConstraints(x, y, width, 0, 0, 0, 0);
    }

    public static GridBagConstraints createFormConstraints(int x, int y, int width, int paddingHorizontal, int paddingVertical) {
        return createFormConstraints(x, y, width, paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }

    public static JRadioButton createRadioButton(String label, int width, int height, int alignment) {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setPreferredSize(new Dimension(width, height));
        radioButton.setBackground(Color.decode("#f5f5f5"));
        radioButton.setHorizontalAlignment(alignment);

        return radioButton;
    }

    public static JPanel createFooter() {
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
        JLabel labelAuthor = new JLabel("Made by 18120113");
        labelAuthor.setForeground(Color.GRAY);
        panelFooter.add(labelAuthor);

        return panelFooter;
    }

    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ViewUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file : " + path);
            return null;
        }
    }
}
