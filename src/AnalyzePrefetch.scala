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

object AnalyzePrefetch extends FileFun {

	def main(Array[String]): Unit = {

		val prefetchDirectory = "/Users/username/Documents/prefetchdir"   // stores prefetch directory location

		/** Generate an Array made up of legitimate prefetch filenames.
		  * An array is used because arrays are good for parallel processing. */
		val prefFileName = {
			Source.fromFile("/Users/username/Documents/security/prefetch_hashes_lookup.txt").getLines.toArray.par }
		val reg = """[A-Z0-9]+.\w[-A-Z0-9]+.pf""".r
		val safePrefetchArray = prefFileName.map(reg.findFirstIn(_).mkString).par

		/* Create an Array of made up of common system filenames. */
		val otherReg = """[A-Z0-9.]+""".r
		val commonFiles = safePrefetchArray.map(otherReg.findFirstIn(_).mkString).toSet.toArray.par

		/*  import all of the prefetch files from a directory. */
		val dirArray = Array(prefetchDirectory)
		val systemPrefetchFiles = getAllFiles(dirArray) // how can we do this in parallel?

		/* filter out the prefetch files that we have hash values for. */
		val matchArray = systemPrefetchFiles.filter(x => safePrefetchArray.exists(y => x.contains(y)))

		/* filter out the prefetch files that are not in the safePrefetchList */
		val scaryFiles = matchArray.filter(x => safePrefetchArray.exists(y => x.contains(y)))

		scaryFiles.foreach(println)

		// Should probably first filter out systemPrefetchFiles that match the commonFiles and put them in new Array.
		// Then we should compare those files to the safePrefetchList array.
		// NEXT we need to import all of the prefetches from directory and compare their names to Strings in the safePrefetchList.
		// We also need to write a regex to extract the prefetch fileNames without the hash values appended to them to use as
		// a comparison.
		// If one of the comparisons with the filenames with hashes extracted matches, the program should alert the user.

	} // END main()
} // END AnalyzePrefetch
