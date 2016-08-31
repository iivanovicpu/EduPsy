package hr.iivanovic.psyedu.user;

import org.mindrot.jbcrypt.BCrypt;
import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.util.DbUtil;

public class UserController {
    static Sql2o sql2o = DbUtil.getH2DataSource();
    static Model dbProvider = new Sql2oModel(sql2o);

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        hr.iivanovic.psyedu.db.User user = dbProvider.getUserByUsername(username);
        if (user == null) {
            return false;
        }

        // todo: create hashed password with salt and compare with hashed password from db (yes, init.sql must create users with hashed password
        // todo: create backdoor login (hardcoded user/password)
        return password.equals(user.getPassword());
//        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
//        return hashedPassword.equals(user.getHashedPassword());
    }

    // This method doesn't do anything, it's just included as an example
    public static void setPassword(String username, String oldPassword, String newPassword) {
        if (authenticate(username, oldPassword)) {
            String newSalt = BCrypt.gensalt();
            String newHashedPassword = BCrypt.hashpw(newSalt, newPassword);
            // Update the user salt and password
        }
    }
}
