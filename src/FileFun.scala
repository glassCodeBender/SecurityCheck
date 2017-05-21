package com.BigBrainSecurity

import java.io.{File, BufferedWriter, FileWriter}
import scala.io.Source
/**
	* Purpose: This program will be used in other classes for
	* common file operations
	*/
trait FileFun[T] {
	/*******Function takes a single String and writes it to a file that is generated based on the fileTreeMap***********/
	def writeToTxtFile(txt: String, file: String): Unit ={
		val fileName: String = file
		val file = new File( fileName )            // Create a file where we'll store hash values.
		val bw = new BufferedWriter(new FileWriter(file))
		bw.write(txt)
		bw.close()
	} // END writeToTxtFile()

	/*********************************Method reads txt file and converts it into a String*******************************/
	def readTxtFromFile(filename: String): String = {
		Source.fromFile(filename).getLines.mkString   // read all of the lines from the file as one String.
		// this technique does not close the file.
	} // END readTxtFromFile()

	/********************CONVERT DIRECTORY TO LIST OF SUB-ITEMS****************************
		*       Methods accept a String directory name & converts to List or Seq of Strings.      *                                                       *
		**************************************************************************************/
	// Why can't I make this code return a ListBuffer? Is it because listFiles() is an Array method?
	def getSubDirList(directoryName: String): Seq[String] = {
		return ( new File(directoryName) ).listFiles.filter(_.isDirectory).map(_.getName )
	}
	// I'm removing the filter so that this method will get a list of all directories and files.
	def getFileList(dirName: String): Seq[String] = {
		return ( new File(dirName) ).listFiles.map(_.getAbsolutePath)
	}

	def getFileArray(directoryName: String): Array[String] = {
		return ( new File(directoryName) ).listFiles.filter(_.isFile).map(_.getAbsolutePath)
	}

} // END FileFun
