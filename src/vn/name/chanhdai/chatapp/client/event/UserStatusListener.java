package vn.name.chanhdai.chatapp.client.event;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 3:55 PM
 * @description
 */
public interface UserStatusListener {
    void onOnline(String username);
    void onOffline(String username);
}
