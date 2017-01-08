/**
  * (@)Author: Alex Moorman
  * (#)Version: 1.0
  * (#)Date: 12/24/2016
  *
  * PROGRAM PURPOSE: To test critical files and ensure no changes have been made.
  */

import java.nio.file.Paths
import java.security.MessageDigest
import java.io.File

class CompareHashes {

  def main( String[]args ){

    // List of directories to use in program.
    val fileArray = ["Filename", "OtherFilename", "Another_File"]

    /**  Use each directory to call the get to Files program.
         The getListOfFiles() takes a directory and returns a list of files
         in the program.*/
    for(int i = 0 ; ) // how do you do it the Scala way?
      getListOfFiles(fileArray(i))

  } // END main()

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  } // END getListOfFiles()


} // END CompareHashes class

/**
  *************************CHECKING CHECKSUM**************************
  *
  * MessageDigest md = MessageDigest.getInstance("MD5");
    try (InputStream is = Files.newInputStream(Paths.get("file.txt"));
         DigestInputStream dis = new DigestInputStream(is, md))
    {
      /* Read decorated stream (dis) to EOF as normal... */
    }
    byte[] digest = md.digest();
  *
  *
  *
  *
  * import java.io.*;
    import java.security.MessageDigest;

    public class MD5Checksum {

       public static byte[] createChecksum(String filename) throws Exception {
           InputStream fis =  new FileInputStream(filename);

           byte[] buffer = new byte[1024];
           MessageDigest complete = MessageDigest.getInstance("MD5");
           int numRead;

           do {
               numRead = fis.read(buffer);
               if (numRead > 0) {
                   complete.update(buffer, 0, numRead);
               }
           } while (numRead != -1);

           fis.close();
           return complete.digest();
       }

       // see this How-to for a faster way to convert
       // a byte array to a HEX string
       public static String getMD5Checksum(String filename) throws Exception {
           byte[] b = createChecksum(filename);
           String result = "";

           for (int i=0; i < b.length; i++) {
               result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
           }
           return result;
       }

       public static void main(String args[]) {
           try {
               System.out.println(getMD5Checksum("apache-tomcat-5.5.17.exe"));
               // output :
               //  0bb2827c5eacf570b6064e24e0e6653b
               // ref :
               //  http://www.apache.org/dist/
               //          tomcat/tomcat-5/v5.5.17/bin
               //              /apache-tomcat-5.5.17.exe.MD5
               //  0bb2827c5eacf570b6064e24e0e6653b *apache-tomcat-5.5.17.exe
           }
           catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
  *
  *
  */