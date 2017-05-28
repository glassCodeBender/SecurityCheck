import com.twitter.hashing.KeyHasher
import org.apache.commons.codec.DigestUtils
import java.nio.file.{Files, Paths}
import java.security.{DigestInputStream, MessageDigest}
import java.io.{File, FileInputStream, IOException}

import com.BigBrainSecurity.IntegrityCheck.makeHash

import scala.collection.immutable.{HashMap, TreeMap}
import scala.math.Ordering

/********************CONVERT DIRECTORY TO LIST OF SUB-ITEMS****************************
	*       Methods accept a String directory name & converts to List or Seq of Strings.      *                                                       *
	**************************************************************************************/
// Why can't I make this code return a ListBuffer? Is it because listFiles() is an Array method?

// DO NOT CHANGE!!!!
def getDirList(directoryName: String): Array[String] = {
	( new File(directoryName) ).listFiles.filter(_.isDirectory).map(_.getAbsolutePath)
}

// DO NOT CHANGE!!!!
def getFileArray(directoryName: String): Array[String] = {
	( new File(directoryName) ).listFiles.filter(_.isFile).map(_.getAbsolutePath)
}

// def fullDirList(directories: Array[String]): Array[String] = directories.flatMap( d => d +: getAllDirs( getDirList( d ) ) )

def fullFileList(directories: Array[String]) = directories.foldLeft(Array[String]()){ (x, y) => x ++: getFileArray(y) }

// def getFullDirList(directories: Array[String]): Array[String ] = directories.foldRight( Array[String]() ){
// 	(x, y) => if (y.isEmpty) y else x +: getDirList(y.head)}

val dirList = getDirList("/Users")
val fullList = getAllDirs("/Users")
// fullList.foreach(println)
fullList.length

// DO NOT CHANGE!!!
def getAllDirs(dir: String): Array[String] = {
	val dirList = getDirList(dir)
	def loop(directories: Array[String], accList: Array[String]): Array[String] = {
		if(directories.isEmpty) accList
		else loop(directories.tail, accList ++: getDirList(directories.head))
	}
	loop(dirList, Array[String]())
}
// DO NOT CHANGE!!!!
def getAllFiles(directories: Array[String]): Array[String] = {
	def loop(dir: Array[String], accArray: Array[String]): Array[String] = {
		if (dir.isEmpty) accArray
		else loop(dir.tail, accArray ++: getFileArray(directories.head))
	}
	loop(directories, Array[String]())
} // END getFullFileList
val allFiles = getAllFiles(fullList)
allFiles.length

/**************************************HASHING FUNCTIONS***********************************************************/

/******************************************STORE IN Hash Table*******************************************************/
/*
 * When this method is called, the param needs to call .toList() to convert array to list.
 *
 * FIRST MAKE THE PROGRAM WORK, THEN WORRY ABOUT OPTIMIZING EFFICIENCY!!!
 * SEE EffectiveScala by Marius Eriksen
 */

/******************************************STORE IN Hash Table*******************************************************/
/*
 * When this method is called, the param needs to call .toList() to convert array to list.
 *
 * FIRST MAKE THE PROGRAM WORK, THEN WORRY ABOUT OPTIMIZING EFFICIENCY!!!
 * SEE EffectiveScala by Marius Eriksen
 */
/*

// Consider using different algorithms based on file size.
private def makeHash( fileName: String ): String = {

	// add if statement to check the size of the file. If the file is less than x amount, use this. Else use twitter.
	try {
		val buffer = new Array[Byte](8192)
		val md5 = MessageDigest.getInstance("MD5")
		val dis = new DigestInputStream(new FileInputStream( new Files(new File(fileName).toPath()) ), md5)

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
private def makeTwitterHash( fileName: String ): Long = {
	val byteArray: Array[Byte] = Files.readAllBytes(Paths.get(fileName))
	val hashValue = KeyHasher.FNV1A_64.hashKey(byteArray) // this is a test. The algorithm was not chosen yet.
	hashValue
} // END makeTwitterHash()

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
			else loop(fileSet.tail, accMap + (fileSet.head -> HashGenerator.generate("SHA256", fileSet.head))
		} // END loop()
		loop( fileSet, new HashMap[String, String]() )
	} // END genMap()

def genTreeMap(fileSet: Seq[String])(implicit ord: Ordering[String]): TreeMap[String, String] = {
	def loop(fileSet: Seq[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
		if (fileSet.isEmpty) accTreeMap
		else loop(fileSet.tail, accTreeMap + (fileSet.head -> makeTwitterHash( fileSet.head)))
	} // END loop()
	loop( fileSet, new TreeMap[String, String]() )
} // END genMap()

// val fileTreeMap = genTreeMap(allFiles)
// fileTreeMap.foreach(println)
val fileMap = makeTwitterHash(allFiles.head).toString
*/
object HashGenerator {
	implicit class Helper(val sc: StringContext) extends AnyVal {
		def md5(): String = generate("MD5", sc.parts(0))
		def sha256(): String = generate("SHA256", sc.parts(0))
	}
	def generate(t: String, path: String): String = {
		val arr = Files.readAllBytes(Paths.get(path))
		val checksum = MessageDigest.getInstance(t) digest arr
		checksum.map("%02X" format _).mkString
	}
}

val firstHash = HashGenerator.generate("SHA256", allFiles.head)
