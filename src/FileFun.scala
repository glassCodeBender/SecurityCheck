package com.BigBrainSecurity

import java.io.{File, BufferedWriter, FileWriter}
import scala.io.Source
/**
	* Purpose: This program will be used in other classes for
	* common file operations
	*/
trait FileFun[T] {
	/*******Function takes a single String and writes it to a file that is generated based on the fileTreeMap***********/
	def writeToTxtFile(txt: String): Unit ={
		val file = new File( "filename.txt" ) // Create a file where we'll store hash values.
		val bw = new BufferedWriter(new FileWriter(file))
		bw.write(txt)
		bw.close()
	} // END writeToTxtFile()

	/*********************************Method reads txt file and converts it into a String**********************************/
	def readTxtFromFile(filename: String): String = {
		Source.fromFile(filename).getLines.mkString   // read all of the lines from the file as one String.
		// this technique does not close the file.
	} // END readTxtFromFile()


} // END FileFun
