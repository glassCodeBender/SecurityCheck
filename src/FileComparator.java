import java.io.File;
import java.util.Comparator
/**
 * Program compares two files.
 *
 *  *** If the first file is a file and the second is a directory,
 *  the program returns -1 indicating that the file is largest than the directory.
 *  *** If there are two files or directly that are equal, program returns 0.
 *  *** If the two files are of the same type, the program orders the program
 *      based on their lexicographical value.
 */
public class FileComparator implements Comparator<File> {

    @Override
    public int compare(File a, File b){
        if(a.isFile() && b.isDirectory())
            return -1;
                else if( a.toString().equals(b.toString()) )
                    return 0;
                else
                    return a.toString().compareTo( b.toString() );

    } // END compare()
}
