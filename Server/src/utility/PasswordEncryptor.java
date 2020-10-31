package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordEncryptor {

    public static String encryptText(String password) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] b = md.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte byteobj : b) {
                stringBuffer.append(Integer.toHexString(byteobj & 0xff).toString());
            }
            return stringBuffer.toString();
    }

}
