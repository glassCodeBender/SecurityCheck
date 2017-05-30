package com.BigBrainSecurity

import scala.io.Source

/**
	* @Author: glassCodeBender
	* @Date 5/30/2017
	* @Version 1.0
	*         
	* This program will look at a directory of prefetch files and determine inconsistencies.
	*
	* Note: The list of common prefetch hashes was obtained from: 
	* http://www.hexacorn.com/blog/2012/06/13/prefetch-hash-calculator-a-hash-lookup-table-xpvistaw7w2k3w2k8/
	*/
  
object AnalyzePrefetch {

	def main(Array[String]): Unit = {
		val prefFileName = Source.fromFile("/Users/MYUSERNAME/Documents/security/prefetch_hashes_lookup.txt").getLines.toList
		val reg = """[A-Z0-9]+.\w[-A-Z0-9]+.pf""".r
		val safePrefetchList = prefFileName.map(reg.findFirstIn(_).mkString
			
		// NEXT we need to import all of the prefetches from directory and compare their names to Strings in the safePrefetchList.
		// We also need to write a regex to extract the prefetch fileNames without the hash values appended to them to use as 
		// a comparison. 
		// If one of the comparisons with the filenames with hashes extracted matches, the program should alert the user.  
		
	} // END main()
	
} // END AnalyzePrefetch
