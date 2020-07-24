package vn.name.chanhdai.chatapp.client;

/**
 * vn.name.chanhdai.chatapp.client
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/23/20 - 1:25 PM
 * @description
 */
public interface UploadListener {
    public void onUploadSuccess(String fileName);
    public void onUploadError();
}
