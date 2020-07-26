package vn.name.chanhdai.chatapp.client.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * vn.name.chanhdai.chatapp.client.utils
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/25/20 - 2:54 PM
 * @description
 */
public class BcryptUtils {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(8));
    }

    public static boolean checkPassword(String hashed, String password) {
        return BCrypt.checkpw(password, hashed);
    }
}
