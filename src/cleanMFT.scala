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
		val sc = new SQLContext(sc)
		// WARNING!!!
		// PIPE SEPARATED VALUE.
		// No concatenation to create timestamps.
		/* import csv file and convert it into a DataFrame */
		val df = sc.read.format("com.databricks.spark.csv")
			.option("header" = true)
			.option("inferSchema", true)
			.load(importFile)

		/* Filter DataFrame by index location */
		if (startIndex != None || endIndex != None)
			df = indexFilter(df, startIndex, endIndex )
    /* Filter DataFrame to only include EXEs outside System32 or Program Files */
		if(suspicious == true )
			df = filterSuspicious(df)
		/* Filter DataFrame by list of Strings (Regex) */
		if(regexFile != None )
			df = filterByFilename( df )
		if(startDate != None || endDate != None || startTime != None || endTime != None) {
			val start = ( startDate.mkString + " " + startTime.mkString )
			val end = ( endDate.mkString + " " + endTime.mkString )
			/*Create Start and Stop Timestamps for filtering */
			val startStamp: Timestamp = new Timestamp().after(start)
			val endStamp: Timestamp = new Timestamp().before(end)
			df = filterByDate(df, startStamp, endStamp)
		} // END if statement filter by date

		df.saveAsSequenceFile("Users/Documents/MFT")
		/* Filter DataFrame by Date*/

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
	                sIndex: Int,   // Integer value that represents starting index.
	                eIndex: Int    // Integer value that represents the end index.
	               ): DataFrame = {

	} // END indexFilter()

	/**
		* filterByFilename()
    * Filters a MFT csv file that was converted into a DataFrame to only include relevant extensions.
    * @param df DataFrame
    * @return DataFrame - Filter df to only include relevant file extensions.
    * @forExample Turns a list into a regular expression.
    * @throws IOException explains why certain common behaviors occurs
	  */
	def filterByFilename(df: DataFrame): DataFrame = {


	} // END filterByFilename()

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

	} // END filterSuspicious()

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

	} // END filterByDate()

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
