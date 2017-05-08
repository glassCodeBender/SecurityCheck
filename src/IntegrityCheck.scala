/**
	* (@)Author: glassCodeBender
	* (#)Version: 1.0
	* (#)Date: 5/8/2017
	*
	* PROGRAM PURPOSE: To test critical files and see if changes have been made.
	*
	* IntegrityCheck.scala is a super class of BigBrainSecurity.scala. This program will be the workhorse behind
	* BigBrainSecurity's IDS system. BigBrainSecurity.scala, along with the forensic program I'm writing in python
	* that BigBrainSecurity will call, will do most of the "thinking".
	*
	*/

import java.nio.file.Path
import java.nio.file.Files
import java.time.LocalDate
import java.util.Scanner
import java.io._
import java.util

import scala.collection.mutable
import scala.io.Source
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer
import scala.collection.parallel.immutable
class IntegrityCheck {

	/***********************************************CLASS CONSTRUCTOR******************************************************/
	private class IntegrityCheck( val inDirectory: String = "C:\\Users\\", val outDirectory: String = null ) {
		def rootDirectory: String = inDirectory
		def destDirectory: String = outDirectory

	} // END constructor

	/*************************************************MAIN METHOD********************************************************/
	def main(args: Array[String]): Unit = {

		val rootFile: String = rootDirectory                        // stores root directory

		// NOTE: Do not declare a val before you put data in it like you would in java.

		/* Prepare a list of files before hashes are generated */
		val dirList = getSubDirList( rootFile )                     // Converts array to a List

		/* There's a good chance I need to recursively go through each subdirectory to get each level of subdirectory
			 and then loop through all the directories collection to accumulate a list of all files. */

		val fileList = getFileList( fileList ).toList               // Gets an array of String filenames & converts them into set.

		val newFileList: List[String] = dirList.flatMap( x => getFileList(x).toList )   // For each directory in file system, get the files in it.

		val newHashMap = genHashMap(fileList)

		fileList.foreach( println ) // THIS IS A TEST

		/* Generate hash values and store them in a HashMap */

		/* Import previous JSON file and store previous values in a HashMap */

		/* Compare the previous HashMap's hash values to the new HashMap's values */

		/* Import BigBrainSecurity config file and check the file's checksum to ensure integrity. */

		/* Import BigBrainSecurity Log File and date for previous log based on data in the config file. */

		/* Export new HashMap and concatenate the result of Integrity Check. */

	} /*******************************************END MAIN METHOD********************************************************/

	/******************************************STORE IN Hash Table*******************************************************/
	/*
	 * When this method is called, the param needs to call .toList() to convert array to list.
	 */
	def genHashMap(fileSet: List[String]): mutable.HashMap[String, String] = {
		def loop(fileSet: List[String], accHashList: mutable.HashMap[String, String]): mutable.HashMap[String, String] = {
			if (fileSet.isEmpty) accHashList
			else loop(fileSet.tail, accHashList += (fileSet.head -> makeHash( new File(fileSet.head) )))
		} // END loop()
		loop( fileSet, new mutable.HashMap[String, String]() )
	} // END genHashMap()

	/********************CONVERT DIRECTORY TO LIST OF SUB-ITEMS****************************
		*       Methods accept a String directory name & converts to Array of Strings.      *                                                       *
		**************************************************************************************/
	// Why can't I make this code return a ListBuffer? Is it because listFiles() is an Array method?
	def getSubDirList(directoryName: String): List[String] = {
		return (new File(directoryName)).listFiles.filter(_.isDirectory).map(_.getName ).toList
	}
  // I'm removing the filter so that this method will get a list of all directories and files.
	def getFileList(dirName: String): List[String] = {
		return (new File(dirName)).listFiles.map(_.getAbsolutePath).toList
	}

	def getFileList(directoryName: String): Array[String] = {
		return (new File(directoryName)).listFiles.filter(_.isFile).map(_.getAbsolutePath)
	}

	/*****************************************CONVERTS A FILE TO A HASH VALUE**********************************************/
	private def makeHash( fileName: File ): String = {
		/**
			* Calculates SHA-1 digest of InputStream object.
			*/
		private def inputStreamDigest() { /*Method below was changed from getAbsoluteFile() */
			val data = System.getProperty( fileName.getAbsolutePath )   // See System API, method requires 2 params.
		val file = new File(data)

			try {
				val inputStream = new FileInputStream( fileName )
				val digest = DigestUtils.sha1Hex(inputStream)             // this should not be sha1Hex()
				System.out.println("Digest          = " + digest)
				System.out.println("Digest.length() = " + digest.length)
			}
			catch {
				case e: IOException => {
					// print message
					e.printStackTrace()
				}
			}
		} // END inputStreamDigest()
		/* NEED TO RETURN A STRING*/
	} // END makeHashes()

} // END IntegrityCheck
