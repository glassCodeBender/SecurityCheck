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
import java.io._              // I can probably delete all other io imports.

import scala.io.Source
import scala.collection.immutable.HashMap
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
		val newHashMap = HashMap[ String, String ]                  // Create container for String filenames and hash values.

		/* Prepare a list of files before hashes are generated */
		val dirList = getSubDirList( rootFile ).toList              // Converts array to a List
		val fileList = getFileList( fileList ).toList               // Gets an array of String filenames & converts them into set.
		val newFileList: List[String] = dirList.flatMap( x => getFileList(x).toList )   // For each directory in file system, get the files in it.

		newHashMap = genHashMap(newFileList) // flatten converts list of lists to a single list. Not sure why this won't work. 

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
	def genHashMap(fileSet: List[String]): HashMap[String, String] = {
		def loop(fileSet: List[String], accHashList: HashMap[String, String]): List[String] = {
			if (fileSet.isEmpty) accHashList
			else loop(fileSet.tail, accHashList.insert(fileSet.head, makeHash(new File(fileSet.head) )))
		} // END loop()
		loop( fileSet, new HashMap[String, String]() )
	} // END genHashMap()

	/********************CONVERT DIRECTORY TO LIST OF SUB-ITEMS****************************
		*       Methods accept a String directory name & converts to Array of Strings.      *                                                       *
		**************************************************************************************/
	// Why can't I make this code return a ListBuffer? Is it because listFiles() is an Array method?
	def getSubDirList(directoryName: String): Array[String] = {
		return (new File(directoryName)).listFiles.filter(_.isDirectory).map(_.getName )
	}

	def getFileList(directoryName: String): Array[String] = {
		return (new File(directoryName)).listFiles.filter(_.isFile).map(_.getAbsolutePath)
	}

} // END IntegrityCheck
