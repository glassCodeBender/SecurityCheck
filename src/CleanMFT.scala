package com.BigBrainSecurity

import java.io.IOException
import java.sql.Timestamp

import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.SELECT
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._ // needed to do a lot of things (unix_timestamp)

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
		* @param importFile String  File that contains the MFT table as a CSV.
		* @param regexFile String   Text file we will use to get values to filter with.
		* @param outputFile String  Name of the csv file we want to create.
		* @return Unit
		* */
	def run(importFile: String, // File that contains the MFT table as a CSV.
	        regexFile: String,  // Text file we will use to get values to filter with.
	        outputFile: String  // Name of the csv file we want to create.
	       ): Unit = {

		/* Create DataFrame and import MFT csv file. */
		val sqlContext = new SQLContext(sc)

		// WARNING!!!
		// No concatenation to create timestamps.
		/* import csv file and convert it into a DataFrame */
		val df = sqlContext.read.format("com.databricks.spark.csv")
			.option("delimiter", "|" )
			.option("header" = true)
			.option("inferSchema", true)
			.load(importFile).persist()

		/* Filter DataFrame by index location */
		if (startIndex != None || endIndex != None)
			val indexDF = indexFilter(df, startIndex, endIndex )

		/* Filter DataFrame to only include EXEs outside System32 or Program Files */
		if(suspicious == true )
			val suspiciousDF = filterSuspicious(
				if (indexDF != None) filterSuspicious(indexDF)
				else filterSuspicious(df))

		/* Filter DataFrame by list of Strings (Regex) */
		if(!regexFile.isEmpty ){
			val regDF = df match{
				case ( suspiciousDF != None ) => filterByFilename ( suspiciousDF )
				case ( indexDF != None ) => indexDF
				case df => df
			}
		} // END if regexFile

		/**
			* Stores the current state of the DataFrame
			* @return DataFrame
			*/
		val theDF: DataFrame = df match {
			case ( regDF != None ) => regDF
				case suspiciousDF != None => suspiciousDF
				case indexDF != None => indexDF
				case df != None => df
		} // END theDF

		/* Take user input and convert it into a timestamp(s) */
		if(startDate != None || endDate != None || startTime != None || endTime != None) {

			/*Create Start and Stop Timestamps for filtering */
			val timeStamp = makeTimeStamp(startDate.mkString, endDate.mkString, startTime.mkString, endTime.mkString )

			val dateDF = filterByDate(sqlContext, theDF , startStamp, endStamp)
		} // END if statement filter by date

		/* Save the processed Data to a compressed file. */
    if (dateDF != None) dateDF.saveAsSequenceFile("Users/lupefiascoisthebestrapper/Documents/MFT")
		else theDF.saveAsSequenceFile("Users/lupefiascoisthebestrapper/Documents/MFT")

		/* Filter DataFrame by Date*/


		// if option to filter by index is true where do we get the index locations?
		// probably a method.

	} // END run()
	/********************************END OF THE DRIVER PROGRAM**********************************/
  /*******************************************************************************************/
	/*******************************************************************************************/

	/**
		* makeTimeStamp()
		* Takes data with separate time and date columns and converts them into unix_timestamps
		* @param startDate starting date
		* @param endDate end date
		* @param startTime start time
		* @param endTime end time
		* @return (unix_timestamp, unix_stamp) - Tuple with both timestamps
		*/
	def makeTimeStamp(startDate: String, // starting date
	                  endDate: String, // end date
	                  startTime: String, // start time
	                  endTime: String  // end time
	                 ) = {
		val start = ( startDate.mkString + " " + startTime.mkString )
		val end = ( endDate.mkString + " " + endTime.mkString )
		/*Create Start and Stop Timestamps for filtering */
		val startStamp: unix_timestamp(start)
		val endStamp = unix_timestamp(end)
		(startStamp, endStamp) // returns tuple with start and end timestamps
	} // END makeTimeStamp()

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
    * @throws IOException explains why certain common behaviors occurs
	  */
	def filterByFilename(df: DataFrame): DataFrame = {
    val pattern = updateReg(regexFile).r // contains regex pattern
		// this probably needs to be opposite notContains() SEE API.
		val filteredDF = df.filter(_.contains(pattern))
		filteredDF

	} // END filterByFilename()

	/**
		* filterSuspicious()
		* Filters a MFT so that only the executables that were run outside Program Files are
		* included in the table.
		* @param df DataFrame
		* @return DataFrame - Filtered to only include relevant file extensions.
    */
	def filterSuspicious(df: DataFrame): DataFrame = {
		// matches all Strings that ran in Program Files or System32
		val regexSys32 = """^.+(Program\sFiles|System32).+[.exe]$""".r
		val regexExe = """.exe$""".r  // matches all Strings that end with .exe

		val filterDF1 = df.filter(_.contains(regexSys32))
		val filteredDF = filteredDF1.filter(_.contains(regexExe))

		filteredDF
	} // END filterSuspicious()

	/**
		* filterByDate()
		* Filters a MFT csv file that was converted into a Dataframe to only include the
		* occurrences of certain dates and/or times.
		* @param df DataFrame
		* @param sDate String
		* @param eDate String
		* @return DataFrame - Filtered to only include relevant virus names.
		*/
	def filterByDate(sqlContext: SQLContext,
	                 df: DataFrame,
	                 sDate: unix_timestamp,
	                 eDate: unix_timestamp
	                ): DataFrame = {

		val dateDF = sqlContext.sql( SELECT *
			                           WHERE $Date_Time >= sDate AND $Date_Time =< $eDate )
		dateDF 
	} // END filterByDate()

	/**
		* updateReg()
		* Filters a list of words and concatenate them into a regex.
		* @param fileName String made up of words provided by users to filter table with.
		* @forExample Concatenates each line of a text file into a regular expression.
		* @return Regex
		*/
	def updateReg(fileName: String): String = {
		// import file - this can also be imported directly into a DataFrame
		val regArray = Source.fromFile(fileName).getLines.toArray.map(_.trim).par
    // concatenate each member of the array to make String
		val regexString = regArray.fold("")((first, second) => first + "|" + second )
		return regexString
	} // END updateReg()


	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*************************** THIS IS THE END OF THE PROGRAM ******************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*****************************************************************************************/
	/*

    """ This is the main method of the program. """
    def run(self):
        sdate, edate, stime, etime = self.__start_date, self.__end_date, self.__start_time, self.__end_time
        output_file = self.__output_file
        suspicious = self.__suspicious
        mft_csv = self.__file
        reg_file = self.__reg_file
        index_bool = self.__index_bool

        sindex, eindex = [x.strip() for x in self.__filter_index.split(',')]
        if sindex.contains(',') or eindex.contains(','):
            sindex.replace(',', '')
            eindex.replace(',', '')
        if not sindex.isdigit and eindex.isdigit:
            raise ValueError("ERROR: The index value you entered to filter the table by was improperly formatted. \n"
                             "Please try to run the program again with different values.")
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

    """
    Read a file line by line and return a list with items in each line.
    @Param A Filename
    @Return A list
    """
    def read_file(self, file):
        list = []
        with open(file) as f:
            for line in f:
                list.append(line)
        return list

    """
    Method to filter a list of words and concatenate them into a regex
    @Param List of words provided by user to alternative file.
    @Return String that will be concatenated to a regex.
    """
    def update_reg(self, list):
        s = '|'
        new_reg = s.join(list)
        return new_reg

    """
    Filters a MFT csv file that was converted into a DataFrame to only include relevant extensions.
    @Param: DataFrame
    @Return: DataFrame - Filtered to only include relevant file extensions.
    """
    def filter_by_filename(self, df):
        reg_file = self.__reg_file
        reg_list = self.read_file(reg_file)
        user_reg = self.update_reg(reg_list)

        if user_reg is not None:
            pattern = r'' + user_reg
        else:
            pattern = r'.exe|.dll|.rar|.sys|.jar'

        regex1 = re.compile(pattern, flags=re.IGNORECASE)
        df['mask'] = df[['Filename', 'Desc']].apply(lambda x: x.str.contains(regex1, regex=True)).any(axis=1)
        filt_df = df[df['mask'] == True]

        pattern2 = r'Create$|Entry$'
        regex2 = re.compile(pattern2, flags=re.IGNORECASE)
        filt_df['mask2'] = filt_df[['Type']].apply(lambda x: x.str.contains(regex2, regex=True)).any(axis=1)
        filtered_df = filt_df[filt_df['mask2'] == True]
        filtered_df.drop(['mask', 'mask2'], axis=1, inplace=True)

        return filtered_df

    """
    Filters a MFT so that only the executables that were run outside Program Files are
    included in the table.
    @Param: DataFrame
    @Return: DataFrame - Filtered to only include relevant file extensions.
    """
    def filter_suspicious(self, df):
        pattern = r'^.+(Program\sFiles|System32).+[.exe]$'
        regex1 = re.compile(pattern)
        df['mask'] = df[['Filename', 'Desc']].apply(lambda x: x.str.contains(regex1, regex=True)).any(axis=1)
        filt_df = df[df['mask'] == False]

        pattern2 = r'.exe$'
        regex2 = re.compile(pattern2)
        filt_df['mask2'] = filt_df[['Filename', 'Desc']].apply(lambda x: x.str.contains(regex2, regex=True)).any(axis=1)
        filtered_df = filt_df[filt_df['mask2'] == True]
        filtered_df.drop(['mask', 'mask2'], axis=1, inplace=True)
        return filtered_df

    """
    Filters a MFT csv file that was converted into a Dataframe to only include the
    occurrences of certain dates and/or times.
    @Param: DataFrame
    @Return: DataFrame - Filtered to only include relevant virus names.
    """
    def filter_by_dates(self, df):

        sdate = self.__start_date
        edate = self.__end_date
        stime = self.__start_time
        etime = self.__end_time

        if edate and sdate and etime and stime:
            s_stamp = pd.Timestamp(sdate + ' ' + stime)
            e_stamp = pd.Timestamp(edate + ' ' + etime)
            filtered_df = df[s_stamp:e_stamp]
        elif sdate and edate and etime and not stime:
            s_stamp = pd.Timestamp(sdate)
            e_stamp = pd.Timestamp(edate + ' ' + etime)
            filtered_df = df[s_stamp:e_stamp]
        elif sdate and edate and stime:
            s_stamp = pd.Timestamp(sdate + ' ' + stime)
            e_stamp = pd.Timestamp(edate)
            filtered_df = df[s_stamp:e_stamp]
        elif sdate and stime:
            s_stamp = pd.Timestamp(sdate + ' ' + stime)
            filtered_df = df[s_stamp:]
        elif edate and etime:
            e_stamp = pd.Timestamp(edate + ' ' + etime)
            filtered_df = df[:e_stamp]
        elif sdate:
            s_stamp = pd.Timestamp(sdate)
            filtered_df = df[s_stamp:]
        elif edate:
            e_stamp = pd.Timestamp(edate)
            filtered_df = df[:e_stamp]
        else:
            raise ValueError("You entered an invalid date to filter the table by or you did not include a date\n"
                             "to filter by. Please try again."
                             "\n\tExample Usage: $ python cleanMFT.py -f MFT.csv -r regex.csv -s 2015-06-08 -e 2015-06-30 -t 06:30:00 -u 06:31:20")
        return filtered_df
 */
} // END CleanMFT.scala
