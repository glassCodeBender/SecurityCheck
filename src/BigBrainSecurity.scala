/**
  * (@)Author: Alex Moorman
  * (#)Version: 1.0
  * (#)Date: 12/25/2016
  *
  * PROGRAM PURPOSE: To test critical files and ensure no changes have been made.
  */

import java.nio.file.Path
import java.nio.file.Files
import java.time.LocalDate
import java.util.Scanner
import java.io._              // I can probably delete all other io imports.

import scala.io.Source
import scala.collection.immutable.{TreeMap, TreeSet}
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.immutable

                        /** Removed serializable in favor of JSON or XML. **/
class BigBrainSecurity {

/***********************************************CLASS CONSTRUCTOR******************************************************/
  private class BigBrainsSecurity( val inDirectory: String = "C:\\Users\\xande\\OneDrive\\Documents\\Git\\PKTest", val outDirectory: String = null ) {
    def rootDirectory = inDirectory
    def destDirectory = outDirectory

  } // END constructor

  /*
  def firstDomino( originalList: List[String] ): TreeSet[String] = {
    /**
      * Must recursively go through and extract all files and
      * directories.
      *
      * Maybe this method calls the methods that do the work
      */
    if(originalList == null)
    else {
      // This might be too much for one method. Us
      createListOfDirectories(originalList.head)
      createListOfFiles(originalList.head)
      firstDomino(originalList.tail)
    } // END if/else
  } // END firstDomino()
  */

/*************************************************MAIN METHOD**********************************************************/
  def main(args: Array[String]): Unit = {

    val fileList = "C:\\Users\\xande\\OneDrive\\Documents\\Git\\PKTest" // import directory

    val fileSet = getNewArrayOfFiles(fileList).toSet      // Gets an array of String filenames & converts them into set.
    val dirSet = getNewArrayOfDirectories(fileList).toSet // Converts array to a set

    val fileTreeSet = collection.immutable.TreeSet[String]() ++ fileSet  // Converts the set of files into a TreeSet.
    val fileTreeMap = TreeMap[String, String]

    fileTreeSet.foreach(println) // THIS IS A TEST

    /***********************CALL METHOD TO POPULATE TreeSet WITH SecureFile OBJECT******************************
     * Call a method that creates a TreeSet or TreeMap made up of SecureFile object values.                    *
     *     Possibly an array of Strings would be better for fileSet and then fileSet can be passed as keys     *
     *     to a TreeSet/TreeMap that is mapped to the SecureFile object created each time the tree is called.  *
     ************************************************************************************************************/

    // 3 calls: One to import old file names, another to import hashes into the TreeSet, and last to add both to tree.
    // Call hashToTree() >> Each recursive call to hashToTree() calls makeHash() and then populates tree.
    // Call populateTree() >> With each recursive call, a new TreeSet node is created.
    // This method calls recursive method and creates a TreeSet made of SecureFile objects.
    // Method to send
  }
/*******************************************END MAIN METHOD************************************************************/

  /******************************************STORE IN TREE*************************************************************/
  /** I have a feeling it will be a lot faster to take care of File i/o w/ a stream */
  // probably need to set up TreeSet using Ordering.fromLessThan[String](_>_). Example page 22.
  /*
   * When this method is called, the param needs to call .toList() to convert array to list.
   * Lists are fast when calling the .head()
   *
   * Here's an answer on stack overflow for converting a List to a map:
   *   SortedMap( list: _*)
   * So you can do this:
   *   val map =  SortedMap( List((1, "Fred"), (2, "Barney")): _*)
   * The _* means you take the Seqs elements instead of the Seq itself as parameter.
   */
  def storeHashValuesInTreeSet(fileSet: List[String]): TreeSet[String] = {
    def loop(fileSet: List[String], accTreeSet: TreeSet[String]): TreeSet[String] = {
      if (fileSet.isEmpty) accTreeSet
      else loop(fileSet.tail, accTreeSet.insert( makeHash(new File(fileSet.head) )))
    } // END loop()
  loop( fileSet, new TreeSet[String]() )
  } // END storeHashValuesInTree()

  def storeHashValuesInTreeMap(fileSet: List[String]): TreeMap[String, String] = {
    def loop(fileSet: List[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
      if (fileSet.isEmpty) accTreeMap
      else loop(fileSet.tail, accTreeMap.insert( fileSet.head, makeHash(new File(fileSet.head) )))
    } // END loop()
    loop( fileSet, new TreeMap[String, String]() )
  } // END storeHashValuesInTree()

  /*
   *  The function below is used to time a method to see how long it takes to run.
   *  Use the method on the method and inside a worksheet and see how efficient it runs.
   *
   *  Example:
   *
   *  //approach # 1
   *  1 to 1000 by 1 toList
   *  //approach #2
   *  List.range(1,1000, 1)
   *
   *  Instead of writing the code below:
   *  val list = List.range(1,1000, 1)
   *
   *  Write:
   *  var list = time {List.range(1,1000, 1)} // it will show you : Elapsed time: 104000ns
   *
   *  Compare to:
   *  var list = time {1 to 1000 by 1 toList} // it will show you : Elapsed time: 93000ns
   */
  /**********************METHOD TO TEST RUNTIME OF A METHOD OR PROCESS******************************
    *                     See collapsed code above for more details.                               *
    *          http://biercoff.com/easily-measuring-code-execution-time-in-scala/                  *
    *************************************************************************************************/
  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    result
  } // END time()

            /***************GENERATE STRING TO USE FOR FILENAMES***************************
              *   Each time a method calls one of the methods below, they should also     *
              *   increment a counter and add that number to the beginning of the String. *
              ******************************************************************************/
  private def generateJSONFileName(str: String): String = {
      // This filename generation technique makes it difficult to compare imported files.
    val dateGen = new LocalDate()
    return String.format("JSON%s", dateGen.toString)
  } // END generateFileName()

  private def generateTxtFileName(str: String): String = {
    // This filename generation technique makes it difficult to compare imported files.
    val dateGen = new LocalDate()
    return String.format("Txt%s", dateGen.toString)
  } // END generateFileName()

  /**
    * This method filters out files inside a directory and returns a list
    * made up of files.
    *
    * This method will continue being called until the List made up of files isEmpty().
    *
    * @param dir is a String that should be a fully qualified directory location.
    * @return List[File]
    */

        /********************CONVERT DIRECTORY TO LIST OF SUB-ITEMS****************************
          *       Methods accept a String directory name & converts to Array of Strings.      *                                                       *
          **************************************************************************************/
  // Why can't I make this code return a ListBuffer? Is it because listFiles() is an Array method?
  def getNewArrayOfDirectories(directoryName: String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(_.isDirectory).map(_.getName )
  }

  def getNewArrayOfFiles(directoryName: String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(_.isFile).map(_.getAbsolutePath)
  }

          /*******************************CONCATENATE TWO TREESETS*******************************
            *                                                                                   *
            * @param xlist is combined w/ accList                                               *
            * @param accList used as the accumulator during recursion.                          *
            * @return TreeSet[String]                                                           *
            *                                                                                   *
            **************************************************************************************/

  def concatSet( xList: TreeSet[String], accList: TreeSet[String]): TreeSet[String] = {
    // It is very likely that this can be done w/ chained method call.
    if (xList.isEmpty) accList
    else if (xList.size > accList.size) concatSet( accList,  xList + accList.head )
    else concatSet( accList.tail, accList + xList.head )
  } // END concatList()

/*****************************************CONVERTS A FILE TO A HASH VALUE**********************************************/
  private def makeHash( fileName: File ): String = {
    /**
      * Calculates SHA-1 digest of InputStream object.
      */
    private def inputStreamDigest() { /*Method below was changed from getAbsoluteFile() */
      val data = System.getProperty( fileName.getAbsolutePath ) // See System API, method requires 2 params.
      val file = new File(data)

      try {
        val inputStream = new FileInputStream( fileName )
        val digest = DigestUtils.sha1Hex(inputStream)
        System.out.println("Digest          = " + digest)
        System.out.println("Digest.length() = " + digest.length)
      }
      catch {
        case e: IOException => {
          e.printStackTrace()
        }
      }
    } // END inputStreamDigest()
    /* NEED TO RETURN A STRING*/
  } // END makeHashes()

        /***************************METHODS TO TRANSFER DATA TO FILES******************************
          *    PURPOSE: These are multi-purpose methods that allow the program to save data when  *
          *         not running. They will all be overriden by sub classes.                       *
          ******************************************************************************************/

  /*******Function takes a single String and writes it to a file that is generated based on the fileTreeMap***********/
  def writeToTxtFile(txt: String): Unit ={
    val file = new File( generateTxtFileName(generateTxtFileName(fileTreeMap)) ) // Create a file where we'll store hash values.
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(txt)
    bw.close()
  } // END writeToTxtFile()

/*********************************Method reads txt file and converts it into a String**********************************/
    def readTxtFromFile(filename: String): String = {
      Source.fromFile(filename).getLines.mkString   // read all of the lines from the file as one String.
      // this technique does not close the file.
    } // END readTxtFromFile()

} // END BigBrainSecurity class
