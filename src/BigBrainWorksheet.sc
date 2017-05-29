
import com.twitter.hashing
import java.nio.file.{Files, Paths}
import java.security.{DigestInputStream, MessageDigest}
import java.io.{File, FileInputStream, IOException}
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

val fullList = getAllDirs("/Users")
// fullList.foreach(println)
fullList.length


// DO NOT CHANGE!!!
def getAllDirs(dir: String): Array[String] = {
	val dirList = getDirList( dir )

	def loop( directories: Array[ String ], accList: Array[ String ] ): Array[ String ] = {
		if ( directories.isEmpty ) accList
		else loop( directories.tail, accList ++: getDirList( directories.head ) )
	}

	loop( dirList, Array[ String ]( ) )
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

object HashGenerator {
	def generate(path: String): String = {
		val arr = Files.readAllBytes(Paths.get(path))
		val checksum = MessageDigest.getInstance("SHA-256") digest arr
		checksum.map("%02X" format _).mkString
	}
}

val firstHash = HashGenerator.generate(allFiles.head)

val hashedTree = genTreeMap(allFiles)
hashedTree.foreach(println)


def genTreeMap(fileSet: Seq[String])(implicit ord: Ordering[String]): TreeMap[String, String] = {
	def loop(fileSet: Seq[String], accTreeMap: TreeMap[String, String]): TreeMap[String, String] = {
		if (fileSet.isEmpty) accTreeMap
		else loop(fileSet.tail, accTreeMap + (fileSet.head -> HashGenerator.generate(fileSet.head)))
	} // END loop()
	loop( fileSet, new TreeMap[String, String]() )
} // END genMap()
