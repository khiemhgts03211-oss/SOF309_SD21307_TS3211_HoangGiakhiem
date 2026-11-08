package test;

import org.mindrot.jbcrypt.BCrypt;

public class GeneratePassword {

    public static void main(String[] args) {

        String password = "123456";

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println(hash);

    }

}