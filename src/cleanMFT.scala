package com.BigBrainSecurity

import java.io.IOException
import java.sql.Timestamp

import org.apache.spark.sql.SQLContext

import scala.io.Source
import scala.util.matching.Regex

/**
	* @author: glassCodeBender
	* @date: June 7, 2017
	* @version: 1.0
	*
	*          Program Purpose: This program takes the cleanMFT.py
	*          project I wrote with pandas DataFrames and applies
	*          the program's principals to large scale big data
	*          environments with Apache Spark.
	*/

object CleanMFT {

	val importFile: String = "/Users/mrsqueaksharem/Downloads/supers.csv"
	val regexFile: String = "/Users/mrsqueaksharem/Downloads/files-to-filter.txt"
	val filterIndex = false
	val startIndex = None
	val endIndex = None
	val outputFile = ""
	val indexBool = true
	val suspicious = false
	val startDate = None
	val endDate = None
	val startTime = None
	val endTime = None

	/** main()
		* Actual main method.
		* @return Unit
		*/
	def main(args:Array[String]): Unit = run(importFile, regexFile, outputFile)

	/**
		* run()
		* This method does all the work.
		* @return Unit
		* */
	def run(importFile: String, // File that contains the MFT table as a CSV.
	        regexFile: String,  // Text file we will use to get values to filter with.
	        outputFile: String  // Name of the csv file we want to create.
	       ): Unit = {

		/* Create DataFrame and import MFT csv file. */
		val pd = new SQLContext(sc)
		// WARNING!!!
		// PIPE SEPARATED VALUE.
		// No concatenation to create timestamps.
		/* import csv file and convert it into a DataFrame */
		val df = pd.read.format("com.databricks.spark.csv")
			.option("header" = true)
			.option("inferSchema", true)
			.load(importFile)
		// if option to filter by index is true where do we get the index locations?
		// probably a method.
		/*
        df = pd.DataFrame()
        df = df.from_csv(mft_csv, sep='|', parse_dates=[[0, 1]])
        # df = df.from_csv("MftDump_2015-10-29_01-27-48.csv", sep='|')
        # df_attack_date = df[df.index == '2013-12-03'] # Creates an extra df for the sake of reference
        if index_bool:
            df.reset_index(level=0, inplace=True)
            if sindex and eindex:
                df = df[sindex : eindex]
        if reg_file:
            df = self.filter_by_filename(df)
        if suspicious:
            df = self.filter_suspicious(df)
        if sdate or edate or stime or etime:
            df = self.filter_by_dates(df)
        df.to_csv(output_file, index=True)
		 */

	} // END run()

	/**
		* indexFilter()
		* Filters a DataFrame based on start and ending index locations.
		* @param df     DataFrame
		* @param sIndex Start Index
		* @param eIndex End Index
		* @return DataFrame
		*/
	def indexFilter(df: DataFrame, // Accepts a DataFrame.
	                sIndex: Int,   // Contains Integer value that represents starting index.
	                eIndex: Int    // Contains Integer value that represents the end index.
	               ): DataFrame = {

	}

	/**
		* filterByFilename()
    * Filters a MFT csv file that was converted into a DataFrame to only include relevant extensions.
    * @param df DataFrame
    * @return DataFrame - Filter df to only include relevant file extensions.
    * @forExample Turns a list into a regular expression.
    * @throws IOException explains why certain common behaviors occurs
	  */
	def filterByFilename(df: DataFrame): DataFrame = {



	}

	/**
		* filterSuspicious()
		* Filters a MFT so that only the executables that were run outside Program Files are
		* included in the table.
		* @param df DataFrame
		* @return DataFrame - Filtered to only include relevant file extensions.
    */
	def filterSuspicious(df: DataFrame): DataFrame = {
		// matches all Stringds that ran in Program Files or System32
		val regexSys32 = """^.+(Program\sFiles|System32).+[.exe]$""".r
		// matches all Strings that end with .exe
		val regexExe = """.exe$""".r


	}

	/**
		* Filters a MFT csv file that was converted into a Dataframe to only include the
		* occurrences of certain dates and/or times.
		* @param df DataFrame
		* @param sDate Timestamp
		* @param eDate Timestamp
		* @return DataFrame - Filtered to only include relevant virus names.
		*/
	def filterByDate(df: DataFrame,
	                 sDate: Timestamp,
	                 eDate: Timestamp
	                ): DataFrame = {

	}
	/**
		* readFile()
		* This method was included in updateReg()
		* Read a file line by line and return a list with items in each line.
		* @param fileName: String
		* @return List[String]
		*/
	def readFile(fileName: String): List[String] = {} // END readFile()

	/**
		* updateReg()
		* Filters a list of words and concatenate them into a regex.
		* @param fileName made up of words provided by users to filter table with.
		* @return Regex.
		*/
	def updateReg(fileName: String): Regex = {
		// import file - this can also be imported directly into a DataFrame
		val regArray = Source.fromFile(fileName).getLines.toArray.map(_.trim).par
    // concatenate each member of the array to make String
		val regexString = regArray.fold("")((first, second) => first + "|" + second )
		return regexString.r
	} // END updateReg()
    
} // END CleanMFT.scala
