package com.BigBrainSecurity

/**
	* (@)Author: glassCodeBender
	* (#)Version: 1.0
	* (#)Date: 5/8/2017
	*
	* Author's Note: This is going to be an extremely long term project because I still have a lot to learn about
	* the Volatility Framework in Python and I haven't even started studying file forensics yet. The reason I am using
	* Scala is because I intend to make this program run with parallel processing. Scala is an extremely difficult
	* language to learn. If I wasn't already so deep into Scala, I probably would have stuck with Python. Nevertheless,
	* I'm glad I'm learning Scala because Scala has made me a million times better programmer in general. Nevertheless,
	* it hurts my brain everyday.
  *
	* PROGRAM PURPOSE: To test critical files and see if changes have been made.
	*
	* IntegrityCheck.scala is a super class of BigBrainSecurity.scala. This program will be the workhorse behind
	* BigBrainSecurity's IDS system. BigBrainSecurity.scala, along with the forensic program I'm writing in python
	* that BigBrainSecurity will call, will do most of the "thinking".
	*
	*/

import com.twitter.hashing.KeyHasher
import org.apache.commons.codec.DigestUtils
import java.nio.file.{Files, Paths}
import java.security.{MessageDigest, DigestInputStream}
import java.io.{File, FileInputStream, IOException}
import scala.collection.immutable.{HashMap, TreeMap}
import scala.math.Ordering

object IntegrityCheck extends FileFun[String] {

	/*********************************************GLOBAL VARIABLES (Probably Unnecessary******************************/
	val inDirectory: String = "/Users" // stores root directory
	val outDirectory: String = null

	/*************************************************MAIN METHOD*****************************************************/
	def main(args: Array[String]): Unit = {

		// NOTE: Do not declare a val before you put data in it like you would in java.

		/* Prepare a list of files before hashes are generated */
		val dirArray = getAllDirs(inDirectory)                    // Converts array to a List
    val allFilesArray = getAllFiles(dirArray)

		/* Generate hash values and store them in a Map or Map. Both methods are shown so I can compare time. */
		val newMap: HashMap[String, String] = genMap(allFilesArray)

		/* Import previous JSON file and store previous values in a Map */

		/* Compare the previous Map's hash values to the new Map's values */
		// Going to iterate over each value based on the key and then compare it to the other map
		// using the same key. Will result in a boolean new map which maps the filename to a boolean
		// value. The new map will be passed to a different function that will analyze the results and determine
		// which file changes matter. The analysis program should be a completely different class.

		val oldMap:HashMap[String, String] = new HashMap[String, String]   // this will be replaced by the Map generated by JSON.
		// MAYBE USE .filter() to filter new collection down to only files without a match!!!! (p. 290)
		val fileChanges = newMap.filter( x => x._ != oldMap._) // this value will be passed to BigBrainSecurity.Scala

		/* REMEMBER: If you have to loop through a collection, you are probably doing something wrong. */
		// var boolMap = new mutable.Map[String, Boolean]
		// newMap.keys.foreach( (fileName) =>
		// if ( oldMap.contains(fileName) ) boolMap += (newMap.keys -> oldMap._ == newMap._ )

		/* Import BigBrainSecurity config file and check the file's checksum to ensure integrity. */

		/* Import BigBrainSecurity Log File and date for previous log based on data in the config file. */

		/* Export new Map and concatenate the result of Integrity Check. */

	} /*******************************************END MAIN METHOD********************************************************/

	/******************************************STORE IN Hash Table*******************************************************/
	/*
	 * When this method is called, the param needs to call .toList() to convert array to list.
	 *
	 * FIRST MAKE THE PROGRAM WORK, THEN WORRY ABOUT OPTIMIZING EFFICIENCY!!!
	 * SEE EffectiveScala by Marius Eriksen
	 */
	def genMap(fileSet: Seq[String]): HashMap[String, String] = {
		def loop(fileSet: Seq[String], accMap: HashMap[String, String]): HashMap[String, String] = {
			// val hashMapAcc = new HashMap(fileSet.head -> makeHash(fileSet.head))
			if (fileSet.isEmpty) accMap
			else loop(fileSet.tail, accMap + (fileSet.head -> makeHash(fileSet.head)))
		} // END loop()
		loop( fileSet, new HashMap[String, String]() )
	} // END genMap()

	def genTreeMap(fileSet: Seq[String])(implicit ord: Ordering[String]): TreeMap[String, String] = {
		def loop(fileSet: Seq[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
			if (fileSet.isEmpty) accTreeMap
			else loop(fileSet.tail, accTreeMap + (fileSet.head -> makeHash( fileSet.head)))
		} // END loop()
		loop( fileSet, new TreeMap[String, String]() )
	} // END genMap()

	def genTreeMap(fileSet: Seq[String])(implicit ord: Ordering[String]): TreeMap[String, String] = {
		def loop(fileSet: Seq[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
			if (fileSet.isEmpty) accTreeMap
			else loop(fileSet.tail, accTreeMap + (fileSet.head -> makeHash( fileSet.head)))
		} // END loop()
		loop( fileSet, new TreeMap[String, String]() )
	} // END genMap()
/*
	// Some hashing methods require File objects, others use Strings.
	def genFileTreeMap(fileSet: Seq[String]): TreeMap[String, File] = {
		def loop(fileSet: Seq[String], accMap: TreeMap[String, File]): Map[String, File] = {
			if (fileSet.isEmpty) accMap
			else loop( fileSet.tail, accMap + ( fileSet.head -> makeTwitterHash(new File(fileSet.head))) )
		} // END loop()
		loop( fileSet, new TreeMap[String, File]() )
	} // END genFileMap()
*/
	/*****************************************CONVERTS A FILE TO A HASH VALUE*****************************************/
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

	// Consider using different algorithms based on file size.
	private def makeHash( fileName: String ): String = {

		// add if statement to check the size of the file. If the file is less than x amount, use this. Else use twitter.
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

	// NOTE: readAllBytes() may not work for large files. If the file size is over a certain amount,
	// Use makeTwitterHash. Else Use makeHash.
	private def makeTwitterHash( fileName: String ): String = {
		// in order to do this method, the genMap method must change back
		// to (new File(*))
		val pathName = Paths.get(fileName) // convert File to Path
		val fileBytes = new Files()
		val byteArray = fileBytes.readAllBytes(pathName)
		KeyHasher.FNV1_32.hashKey(byteArray) // this is a test. The algorithm was not chosen yet.
	} // END makeTwitterHash()

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
*/
		private def makeHash2(fileName: File): String = {
			/**
				* Need to add sbt dependency for apache commons.
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
} // END IntegrityCheck class
