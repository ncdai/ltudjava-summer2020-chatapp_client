package vn.name.chanhdai.chatapp.client.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * vn.name.chanhdai.chatapp.client.utils
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/25/20 - 4:41 PM
 * @description
 */
public class MessageUtils {
    // [type]=[text]
    public static String[] getTypeAndText(String message) {
        String[] messageTokens = StringUtils.split(message, "=", 2);

        String type = messageTokens.length == 2 ? messageTokens[0] : "text";
        String text = messageTokens.length == 2 ? messageTokens[1] : messageTokens[0];

        String[] res = new String[2];
        res[0] = type;
        res[1] = text;

        return res;
    }

    // [sender]:[receiver]
    public static String[] getSenderAndReceiver(String str) {
        String[] senderAndReceiver = StringUtils.split(str, ":");
        String sender = senderAndReceiver[0];
        String receiver = senderAndReceiver[1];

        String[] res = new String[2];
        res[0] = sender;
        res[1] = receiver;

        return res;
    }
}
