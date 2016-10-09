package hr.iivanovic.psyedu.controllers;

import org.mindrot.jbcrypt.BCrypt;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.db.User;

public class UserController {
    static Model dbProvider = Sql2oModel.getInstance();

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static User authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }
        hr.iivanovic.psyedu.db.User user = dbProvider.getUserByUsername(username);
        if (user == null) {
            return null;
        }

        // todo: create hashed password with salt and compare with hashed password from db (yes, init.sql must create users with hashed password
        // todo: create backdoor login (hardcoded user/password)
        return password.equals(user.getPassword()) ? user : null;
//        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
//        return hashedPassword.equals(user.getHashedPassword());
    }

    // This method doesn't do anything, it's just included as an example
    public static void setPassword(String username, String oldPassword, String newPassword) {
        if (authenticate(username, oldPassword) != null) {
            String newSalt = BCrypt.gensalt();
            String newHashedPassword = BCrypt.hashpw(newSalt, newPassword);
            // Update the user salt and password
        }
    }

}
