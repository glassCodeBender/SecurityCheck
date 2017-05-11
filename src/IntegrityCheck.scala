package com.BigBrainSecurity

/**
	* (@)Author: glassCodeBender
	* (#)Version: 1.0
	* (#)Date: 5/11/2017
	*
	* PROGRAM PURPOSE: To test critical files and see if changes have been made.
	*
	* IntegrityCheck.scala is a super class of BigBrainSecurity.scala. This program will be the workhorse behind the file
	* swap section of BigBrainSecurity's IDS system. BigBrainSecurity.scala, along with the forensic program I'm writing 
	in python will do most of the "thinking".
	*
	*/

import java.time.LocalDate
import java.nio.file.Files
import java.util.Scanner

import scala.collection.mutable
import scala.io.Source
import java.org.apache.commons.codec.digest

import com.twitter.hashing
import java.security.MessageDigest

import com.twitter.hashing.KeyHasher
import com.google.common.hash

import java.security.{MessageDigest, DigestInputStream}
import java.io.{File, FileInputStream, IOException}

object IntegrityCheck {

	/***********************************************CLASS CONSTRUCTOR******************************************************/
	val inDirectory: String = "C:\\Users\\" // stores root directory
	val outDirectory: String = null

	/*************************************************MAIN METHOD********************************************************/
	def main(args: Array[String]): Unit = {

		// NOTE: Do not declare a val before you put data in it like you would in java.

		/* Prepare a list of files before hashes are generated */
		val dirList = getSubDirList( inDirectory )                     // Converts array to a List

		/* There's a good chance I need to recursively go through each subdirectory to get each level of subdirectory
			 and then loop through all the directories collection to accumulate a list of all files. */

		val fileList = getFileList( fileList ).toList               // Gets an array of String filenames & converts them into set.

		val newFileList: List[String] = dirList.flatMap( x => getFileList(x).toList )   // For each directory in file system, get the files in it.

		/* Generate hash values and store them in a HashMap or TreeMap. Both methods are shown so I can compare time. */
		val newHashMap = genHashMap(newFileList)
		val newTreeMap = genTreeMap(newFileList)

		fileList.foreach( println ) // THIS IS A TEST

		/* Import previous JSON file and store previous values in a HashMap */

		/* Compare the previous HashMap's hash values to the new HashMap's values */
		// Going to iterate over each value based on the key and then compare it to the other map
		// using the same key. Will result in a boolean new map which maps the filename to a boolean
		// value. The new map will be passed to a different function that will analyze the results and determine
		// which file changes matter. The analysis program should be a completely different class.

		val oldHashMap = new mutable.HashMap[String, String]   // this will be replaced by the HashMap generated by JSON.
		// MAYBE USE .filter() to filter new collection down to only files without a match!!!! (p. 290)
		val fileChanges = newHashMap.filter( _ != oldHashMap._) // this value will be passed to BigBrainSecurity.Scala

		/* REMEMBER: If you have to loop through a collection, you are probably doing something wrong. */
		// var boolHashMap = new mutable.HashMap[String, Boolean]
		// newTreeMap.keys.foreach( (fileName) =>
		// if ( oldHashMap.contains(fileName) ) boolHashMap += (newTreeMap.keys -> oldHashMap._ == newHashMap._ )

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
			else loop(fileSet.tail, accHashList += (fileSet.head -> makeHash( fileSet.head ) )))
		} // END loop()
		loop( fileSet, new mutable.HashMap[String, String]() )
	} // END genHashMap()

	/*Test to see which method is faster when interating over large number of files using timeIt function
	  I'm pretty sure this method will have to use Ordering.fromLessThan[String](_>_). Example on pg. 22*/
	def genTreeMap(fileSet: List[String]): mutable.TreeMap[String, String] = {
		def loop(fileSet: List[String], accHashList: mutable.TreeMap[String, String]): mutable.TreeMap[String, String] = {
			if (fileSet.isEmpty) accHashList
			else loop(fileSet.tail, accHashList += (fileSet.head -> makeHash( fileSet.head ) )))
		} // END loop()
		loop( fileSet, new mutable.TreeMap[String, String]() )
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

	def getFileArray(directoryName: String): Array[String] = {
		return (new File(directoryName)).listFiles.filter(_.isFile).map(_.getAbsolutePath)
	}

	/*****************************************CONVERTS A FILE TO A HASH VALUE**********************************************/
	/*Building

	Use sbt (simple-build-tool) to build:

		$ sbt clean update package-dist
	The finished jar will be in dist/.

	Using

	To use hash functions:

	KeyHasher.FNV1_32.hashKey("string".getBytes)

	Available hash functions are:

	FNV1_32
	FNV1A_32
	FNV1_64
	FNV1A_64
	KETAMA
	CRC32_ITU
	HSIEH
	To use ketama distributor:

	val nodes = List(KetamaNode("host:port", weight, client))
	val distributor = new KetamaDistributor(nodes, KeyHasher.FNV1_32)
	distributor.nodeForKey("abc") // => client
	*/

	private def makeHash( fileName: File ): String = {

		try {
			val buffer = new Array[Byte](8192)
			val md5 = MessageDigest.getInstance("MD5")

			val dis = new DigestInputStream(new FileInputStream( new Files(fileName) ), md5)
			try { while (dis.read(buffer) != -1) { } } finally { dis.close() }

			md5.digest.map("%02x".format(_)).mkString
		}
		catch {
			case e: IOException => {
				// print message
				e.printStackTrace()
			}
		} // END try/catch
	} // END makeHash

	private def makeTwitterHash( fileName: File ): String = {
		// in order to do this method, the genTreeMap method must change back
		// to (new File(*))
		val pathName = fileName.toPath() // convert File to Path
		val fileBytes = new Files
		val byteArray = fileBytes.readAllBytes(pathName)
		KeyHasher.FNV1_32.hashKey(byteArray) // this is a test. The algorithm was not chosen yet.
	}

	/*
		private def makeGoogleHash( fileName: File): String = {
			try {
				return new com.google.common.hash.Hasher.putObject(fileName)
			}
			catch {
				case e: IOException => {
					// print message
					e.printStackTrace()
				}
			} // END try/catch
		} // END makeGoogleHash()

		// KeyHasher.FNV1_32.hashKey(Byte[])

		private def makeHash2(fileName: File): String = {
			/**
				* Using Twitter's util API to hash functions.
				*/

			private def inputStreamDigest() { /*Method below was changed from getAbsoluteFile() */
				val data = System.getProperty( fileName.getAbsolutePath )   // See System API, method requires 2 params.
				val file = new File(data)

				try {
					val inputStream = new FileInputStream( fileName )
					val digest = {
						DigestUtils.sha256Hex( inputStream )
					} // this should not be sha1Hex()
					// System.out.println("Digest          = " + digest)
					// System.out.println("Digest.length() = " + digest.length)
					return digest.toString()
				}
				catch {
					case e: IOException => {
						// print message
						e.printStackTrace()
					}
				} // END try/catch
			} // END inputStreamDigest()
			inputStreamDigest()
		} // END makeHash()
		*/

} // END IntegrityCheck class
