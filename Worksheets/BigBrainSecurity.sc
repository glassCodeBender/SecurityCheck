
import scala.collection.immutable.{TreeMap, TreeSet}
import java.io.File
import java.io.EOFException
import java.io.IOException

// need class for txt file.
// need class that uses the other two classes.
// BigBrainSecurity is a super class of SerializedSecurity and TXT security.**/

object BigBrainSecurity {

  /******************CONVERT DIRECTORY TO LIST OF SUB-ITEMS*****************************
    *                                                                                  *
    * Purpose: Methods accept a String directory name & converts it an Array           *
    *   of Strings.                                                                    *
    *                                                                                  *
    * @param directoryName stores the root directory.                                  *
    * @return a Set[String] for files. A Stack[String] for sub directories             *
    *                                                                                  *
    *************************************************************************************/

  def getListOfSubDirectories(directoryName: String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(_.isDirectory).map(_.getName)
  }

  def getListOfSubFiles(directoryName: String): TreeSet[String] = {
    return (new File(directoryName)).listFiles.filter(_.isFile).map(_.getName)
  }

  /*******************************CONCATENATE TWO TREESETS*******************************
    * Purpose: Concatenates two TreeSets into one.                                      *
    *                                                                                   *
    * @param xlist is combined w/ accList                                               *
    * @param accList used as the accumulator during recursion.                          *
    * @return TreeSet[String]                                                           *
    *                                                                                   *
    **************************************************************************************/

  def concatSet( xList: TreeSet[String], accList: TreeSet[String]): TreeSet[String] = {

    if (xList.isEmpty) return accList
     else if (xList.size > accList.size) concatSet( accList, accList.head ::: xList )
     else concatSet( accList.tail, xList.head ::: accList )
  } // END concatList()

  /**
    * Test concatList w/ a file object.
    */

} // END BigBrainSecurity object

protected class BigBrainSecurity(encryptStyle: String){

  def encryptionStyle = encryptStyle

  /***************************METHODS TO TRANSFER DATA TO TXT FILES**************************
    *                                                                                       *
    *    PURPOSE: These are multi-purpose methods that allow the program to save data when  *
    *         not running.                                                                  *
    *                                                                                       *
    * @param Set                                                                            *
    *                                                                                       *
    ******************************************************************************************/

  def prepTxtFile(): Unit = {

  } // END prepTxtFile()

  def sendToTxtFile(setOfStrings: Set[String] ): Unit = {
    /*
        Method to convert a given set to
     */
  }   // END of sendToTxtFile()

  def closeTxtFile(): Unit: {

  } // END closeTxtFile()

  /*************************DEPRECATED FUNCTIONS**********************************/

  // It doesn't matter how the files get pulled out, all the matters is that the directory list
  // is empty before files List is gone through.
  /* Extending Ordered[File] makes it possible to store files in ordered Set. */
  private def createListOfFiles( dir: String ) extends Ordered[File]: List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]() // maybe should have warning about not being a directory.
    }
  } // END getListOfFiles()


  /**
    * This method filters out directories inside a directory and returns a list
    * made up of directories.
    *
    * This method should be called before the createListofFiles() is called.
    *
    * This method will continue being called until the List made up of directories isEmpty().
    *
    * @param dir is a String that should be a fully qualified directory location.
    * @return List[File]
    */

  // Need to filter out all directories before touching files.
  private def createListOfDirectories( dir: String ): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      // Find the directories inside the directory and add them to directory list
      d.listFiles.filter(_.isDirectory).toList
    } else {
      List[File]() // maybe should have warning about not being a directory.
    }
  } // END getListOfFiles()


} // END BigBrainSecurity class