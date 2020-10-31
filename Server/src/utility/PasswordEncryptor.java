package Encryptor;

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
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the piece of text that you want to hash : ");
        String password = input.nextLine();
        String encryptedpassword = null;
        try {
            encryptedpassword = encryptText(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Entered Text : "+password);
        System.out.println("Hashed password : "+encryptedpassword);
    }
}
