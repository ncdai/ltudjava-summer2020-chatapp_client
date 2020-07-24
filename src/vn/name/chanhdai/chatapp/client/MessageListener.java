package vn.name.chanhdai.chatapp.client;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 4:16 PM
 * @description
 */
public interface MessageListener {
    public void onReceiveMessage(String sender, String receiver, String type, String message);
}
