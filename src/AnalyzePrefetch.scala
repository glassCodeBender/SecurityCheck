package com.BigBrainSecurity

import scala.io.Source

/**
	* @Author: glassCodeBender
	* @Date 5/30/2017
	* @Version 1.0
	*
	* This program will look at a directory of prefetch files and determine inconsistencies.
	* Note: The list of safe prefetch filenames was obtained here:
	* http://www.hexacorn.com/blog/2012/06/13/prefetch-hash-calculator-a-hash-lookup-table-xpvistaw7w2k3w2k8/
	*/

object AnalyzePrefetch {

	def main(Array[String]): Unit = {
		val prefFileName = Source.fromFile("/Users/xan0/Documents/security/prefetch_hashes_lookup.txt").getLines.toArray
		val reg = """[A-Z0-9]+.\w[-A-Z0-9]+.pf""".r
		val safePrefetchList = prefFileName.map(reg.findFirstIn(_).mkString)
		val otherReg = """[A-Z0-9.]+""".r
		val commonFiles = safePrefetchList.map(otherReg.findFirstIn(_).mkString).toSet.toArray

		/*
				// NEXT we need to import all of the prefetches from directory and compare their names to Strings in the safePrefetchList.
				// If one of the comparisons with the filenames with hashes extracted matches, the program should alert the user.
		*/
	} // END main()

} // END AnalyzePrefetch
