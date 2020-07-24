package vn.name.chanhdai.chatapp.client.utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * vn.name.chanhdai.chatapp.client.utils
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/24/20 - 5:41 PM
 * @description
 */
public class EmojiUtils {
    public static String SMALL = "small";
    public static String LARGE = "large";

    public static List<String> getAllEmoji() {
        List<String> list = new ArrayList<>();
        list.add("face-with-tears-of-joy");
        list.add("beaming-face-with-smiling-eyes");
        list.add("face-blowing-a-kiss");
        list.add("face-with-lightbulb");
        list.add("face-with-laptop");
        list.add("face-with-medical-mask");
        list.add("anxious-face-with-sweat");
        list.add("face-with-symbols-on-mouth");

        return list;
    }

    public static ImageIcon getImage(String code, String size) {
        switch (code) {
            case "face-with-tears-of-joy": {
                return ViewUtils.createImageIcon("emojis/face-with-tears-of-joy@" + size + ".png");
            }
            case "beaming-face-with-smiling-eyes": {
                return ViewUtils.createImageIcon("emojis/beaming-face-with-smiling-eyes@" + size + ".png");
            }
            case "face-blowing-a-kiss": {
                return ViewUtils.createImageIcon("emojis/face-blowing-a-kiss@" + size + ".png");
            }
            case "face-with-lightbulb": {
                return ViewUtils.createImageIcon("emojis/face-with-lightbulb@" + size + ".png");
            }
            case "face-with-laptop": {
                return ViewUtils.createImageIcon("emojis/face-with-laptop@" + size + ".png");
            }
            case "face-with-medical-mask": {
                return ViewUtils.createImageIcon("emojis/face-with-medical-mask@" + size + ".png");
            }
            case "anxious-face-with-sweat": {
                return ViewUtils.createImageIcon("emojis/anxious-face-with-sweat@" + size + ".png");
            }
            case "face-with-symbols-on-mouth": {
                return ViewUtils.createImageIcon("emojis/face-with-symbols-on-mouth@" + size + ".png");
            }
            default: {
                return null;
            }
        }
    }
}
