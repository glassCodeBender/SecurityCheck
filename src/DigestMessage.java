import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * Created by Xander on 12/25/2016.
 */

public class SHAHashDemo {
    public static void main(String[] args) {
        SHAHashDemo demo = new SHAHashDemo();
        demo.byteDigest();
        demo.inputStreamDigest();
        demo.stringDigest();
    }

    /**
     * Calculates SHA-1 digest from byte array.
     */
    private void byteDigest() {
        System.out.println("SHAHashDemo.byteDigest");
        try {
            byte[] data = "The quick brown fox jumps over the lazy dog.".getBytes("UTF-8");
            String digest = DigestUtils.sha1Hex(data);
            System.out.println("Digest          = " + digest);
            System.out.println("Digest.length() = " + digest.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    } // byteDigest()

    /**
     * Calculates SHA-1 digest of InputStream object.
     */
    private void inputStreamDigest() {
        System.out.println("SHAHashDemo.inputStreamDigest");
        String data = System.getProperty("user.dir") + "/target/classes/data.txt";
        File file = new File(data);
        try {
            InputStream is = new FileInputStream(file);
            String digest = DigestUtils.sha1Hex(is);
            System.out.println("Digest          = " + digest);
            System.out.println("Digest.length() = " + digest.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // inputStreamDigest()

    /**
     * Calculate SHA-1 digest of a string / text.
     */
    private void stringDigest() {
        System.out.println("SHAHashDemo.stringDigest");
        String data = "This is just a simple data message for SHA digest demo.";
        String digest = DigestUtils.sha1Hex(data);
        System.out.println("Digest          = " + digest);
        System.out.println("Digest.length() = " + digest.length());
    }
}