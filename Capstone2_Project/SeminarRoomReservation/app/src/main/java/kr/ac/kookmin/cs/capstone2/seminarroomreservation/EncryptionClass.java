package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ehye on 2015-09-14.
 */
public class EncryptionClass {
    public  String testSHA256(String str) {
        System.out.println("str : "+str);
        System.out.println("str : "+str.getBytes());

        String SHA = "";

        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
            System.out.println("testing . . .");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }
}
