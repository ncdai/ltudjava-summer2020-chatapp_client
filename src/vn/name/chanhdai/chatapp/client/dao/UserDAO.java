package vn.name.chanhdai.chatapp.client.dao;

import vn.name.chanhdai.chatapp.client.entity.User;
import vn.name.chanhdai.chatapp.client.utils.BcryptUtils;
import vn.name.chanhdai.chatapp.client.utils.HibernateUtils;

/**
 * vn.name.chanhdai.chatapp.client.dao
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/25/20 - 2:59 PM
 * @description
 */
public class UserDAO {
    public static User getSingle(String username) {
        return HibernateUtils.getRow(User.class, username);
    }

    public static boolean create(User user) {
        if (HibernateUtils.getRow(User.class, user.getUsername()) != null) {
            System.out.println("User(" + user.getUsername() + ") da ton tai!");
            return false;
        }

        user.setPassword(BcryptUtils.hashPassword(user.getPassword()));
        return HibernateUtils.insertRow(user);
    }

    public static User login(String username, String password) {
        User user = UserDAO.getSingle(username);

        if (user == null) return null;

        if (BcryptUtils.checkPassword(user.getPassword(), password)) return user;

        return null;
    }

    public static boolean updatePassword(String username, String currentPassword, String newPassword) {
        User user = UserDAO.login(username, currentPassword);
        if (user == null) return false;

        user.setPassword(BcryptUtils.hashPassword(newPassword));
        return HibernateUtils.updateRow(user);
    }
}
