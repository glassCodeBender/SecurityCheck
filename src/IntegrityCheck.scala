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
import java.nio.file.{Files, Paths}
import java.security.MessageDigest

import scala.collection.immutable.{HashMap, TreeMap}
import scala.math.Ordering

object HashGenerator {
	def generate( path: String ): String = {
		val byteArray = Files.readAllBytes( Paths get path )
		val checksum = MessageDigest.getInstance( "SHA-256" ) digest byteArray
		checksum.map( "%02X" format _ ).mkString
	}
} // END HashGenerator

object IntegrityCheck extends FileFun {

	/*********************************************GLOBAL VARIABLES (Probably Unnecessary******************************/
	val inDirectory: String = "/Users" // stores root directory
	val outDirectory: String = null

	/*************************************************MAIN METHOD*****************************************************/
	def main(args: Array[String]): Unit = {

		// NOTE: Do not declare a val before you put data in it like you would in java.

		/* Prepare a list of files before hashes are generated */
		val dirArray = getAllDirs("/Users")                    // Converts array to a List
    val allFilesArray = getAllFiles(dirArray)

		/* Generate hash values and store them in a TreeMap or HashMap. Both methods are shown so I can compare time. */

		/* Import previous JSON file and store previous values in a Map */

		/* Compare the previous Map's hash values to the new Map's values */

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
			else loop(fileSet.tail, accMap + (fileSet.head -> HashGenerator.generate(fileSet.head)))
		} // END loop()
		loop( fileSet, new HashMap[String, String]() )
	} // END genMap()

	def genTreeMap(fileSet: Seq[String])(implicit ord: Ordering[String]): TreeMap[String, String] = {
		def loop(fileSet: Seq[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
			if (fileSet.isEmpty) accTreeMap
			else loop(fileSet.tail, accTreeMap + (fileSet.head -> HashGenerator.generate(fileSet.head)))
		} // END loop()
		loop( fileSet, new TreeMap[String, String]() )
	} // END genMap()

	def genTreeMapTwitter(fileSet: Seq[String])(implicit ord: Ordering[String]): TreeMap[String, String] = {
		def loop(fileSet: Seq[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
			if (fileSet.isEmpty) accTreeMap
			else loop(fileSet.tail, accTreeMap + (fileSet.head -> makeTwitterHash(fileSet.head)))
		} // END loop()
		loop( fileSet, new TreeMap[String, String]() )
	} // END genMap()

	/*****************************************CONVERTS A FILE TO A HASH VALUE*****************************************/

	// NOTE: readAllBytes() may not work for large files. If the file size is over a certain amount,
	// Use makeTwitterHash. Else Use makeHash.
	private def makeTwitterHash( fileName: String ): String = {
		// in order to do this method, the genMap method must change back
		// to (new File(*))
		val byteArray = Files.readAllBytes(Paths get fileName)
		KeyHasher.FNV1_32.hashKey(byteArray).toString } // this is a test. The algorithm was not chosen yet.
		
