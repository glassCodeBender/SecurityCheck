import java.io.File
import java.nio.file.{Files,Paths}


/********************CONVERT DIRECTORY TO LIST OF SUB-ITEMS****************************
	*       Methods accept a String directory name & converts to List or Seq of Strings.      *                                                       *
	**************************************************************************************/
// Why can't I make this code return a ListBuffer? Is it because listFiles() is an Array method?

def getDirList(directoryName: String): Array[String] = {
	( new File(directoryName) ).listFiles.filter(_.isDirectory).map(_.getAbsolutePath)
}

// I'm removing the filter so that this method will get a list of all directories and files.
def getFileList(dirName: String): List[String] = {
	( new File(dirName) ).listFiles.map(_.getAbsolutePath).toList
}

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

def getAllDirs(dir: String): Array[String] = {
	val dirList = getDirList(dir)
	def loop(directories: Array[String], accList: Array[String]): Array[String] = {
		if(directories.isEmpty) accList
		else loop(directories.tail, accList ++: getDirList(directories.head))
	}
	loop(dirList, Array[String]())
}

def getAllFiles(directories: Array[String]): Array[String] = {
	def loop(dir: Array[String], accArray: Array[String]): Array[String] = {
		if (dir.isEmpty) accArray
		else loop(dir.tail, accArray ++: getFileArray(directories.head))
	}
	loop(directories, Array[String]())
} // END getFullFileList
val allFiles = getAllFiles(fullList)
allFiles.length
