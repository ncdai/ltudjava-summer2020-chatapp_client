package vn.name.chanhdai.chatapp.client;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/21/20 - 3:55 PM
 * @description
 */
public interface UserStatusListener {
    public void onOnline(String username);
    public void onOffline(String username);
}
