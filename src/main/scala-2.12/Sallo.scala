import java.io.{File, PrintWriter}
import java.nio.file.{Files, NoSuchFileException, Paths}
import javax.crypto.BadPaddingException

import it.cristiano.sallo.dao.{AttributeValidation, ElementValidation, FileDao, LineValidation}
import it.cristiano.sallo.dao.message.DaoReturnMessage
import it.cristiano.sallo.util.CryptoUtils

import scala.annotation.tailrec

/**
  * Created by cristiano on 11/21/16.
  */

object Sallo {

  val DEFAULT_PASS = "DEFAULT_PASS"
  val archivePath = new File(Sallo.getClass.getProtectionDomain.getCodeSource.getLocation.getPath).getParentFile.getPath
  val DECRYPT_FILE = archivePath.concat(File.separator).concat("file.csv")
  val ENCRYPT_FILE = archivePath.concat(File.separator).concat("file.csv.enc")

  val scanner = new java.util.Scanner(System.in)

  /**
    * <b>command :<b/> usage
    *
    * @param args arguments
    */
  def main(args: Array[String]): Unit = {

    println(s"-> $DECRYPT_FILE")
    println(s"-> $ENCRYPT_FILE")

    val password = access()
    help()
    selection(fixLine = true, password)
  }

  def access(password: String = DEFAULT_PASS): String = {
    @tailrec
    def login(password: String): String = {
      print("password:")
      val password = scanner.next()
      try {
        CryptoUtils.decrypt(password, ENCRYPT_FILE)
        password
      } catch {
        case _: NoSuchFileException =>
          initializeFile(password)
        case _: BadPaddingException =>
          println(s"Login Failed!")
          login(password)
      }
    }
    login(password)
  }

  def initializeFile(password: String): String = {
    val exampleRecord = "sallo=first-line,attribute=first-attribute"
    println("*********************************  INITIALIZE  *********************************")
    println(s"Archive's file does not exist!")
    println(s"Sallo is creating your encrypted file, password: $password")
    println(s"Your file: ${ENCRYPT_FILE}")
    val pw = new PrintWriter(new File(DECRYPT_FILE))
    pw.write(exampleRecord)
    pw.close()
    CryptoUtils.encrypt(password, DECRYPT_FILE)
    new File(DECRYPT_FILE).delete()
    println(s"Your encrypted file has been created, it contains an example records : '$exampleRecord'")
    password
  }

  def help(): Unit = {
    println("***********************************  HELP  *********************************")
    println("Add a csv file                  : add file path/yourfilename.csv")
    println("Add line                        : add line attribute1=attribute1,attribute2=attribute2,attributeEtc=attributeEtc")
    println("Remove line at index            : rem line [index]")
    println("Get all line                    : get all")
    println("Get line at index               : get line [index]")
    println("Match string                    : match [String]")
    println("Add attribute at line index     : add attr [index] attributeName=attributeValue")
    println("Update attribute at line index  : upd attr [index] attributeName=attributeValue")
    println("Remove attribute at line index  : rem attr [index] attributeName")
    println("Show Help                       : help")
    println("Quit sallo                      : exit")
  }

  def selection(fixLine: Boolean, password: String = DEFAULT_PASS): Unit = {

    if (!Files.exists(Paths.get(ENCRYPT_FILE))) {
      println("Archive file has been removed, FATAL ERROR!")
      System.exit(0)
    }

    val fdao = new FileDao(password, ENCRYPT_FILE)

    print("Command:")
    if (fixLine) scanner.nextLine
    val command = scanner.nextLine

    val ar = command.split(" ")

    ar match {
      case Array("add", "file", _) =>
        val filePath = ar.lift(2).get
        fdao.addFile(filePath) match {
          case DaoReturnMessage.FILE_NOT_EXIST => println(s"File '$filePath' does not exist, RETRY!")
          case _ => println(s"File: '$filePath' ADDED!")
        }
      case Array("add", "line", _) =>
        val line = ar.lift(2).get
        if (DaoReturnMessage.KEY_INDEX_IS_RESERVED == fdao.addLine(line))
          println(s"Line: '$line' There is a key named index, it is a reserved key, ERROR!")
        else
          println(s"Line: '$line' ADDED!")
      case Array("rem", "line", _) =>
        val index = ar.lift(2).get
        val lineBackup = fdao.getLine(index)._1
        fdao.removeLine(index) match {
          case DaoReturnMessage.SUCCESS => println(s"Line '$lineBackup' REMOVED!")
          case _ => println(s"Line index:'$index' does not exist, RETRY!")
        }
      case Array("get", "all") =>
        for (l <- fdao.getAll) println(s"-> $l")
      case Array("get", "line", _) =>
        val index = ar.lift(2).get
        val line = fdao.getLine(index)
        if (line._2 == DaoReturnMessage.NO_LINE)
          println(s"Line index:'$index' does not exist, RETRY!")
        else
          println(s"-> ${line._1}")
      case Array("match", _) =>
        val lines = fdao.getByMatch(ar.lift(1).get)
        fdao.getByMatch(ar.lift(1).get) match {
          case _ if lines.isEmpty => println(s"No lines match string:'${ar.lift(1).get}', RETRY!")
          case _ => for (l <- lines) println(s"-> $l")
        }
      case Array("add", "attr", _, _) =>
        val index = ar.lift(2).get
        val keyInsert = ar.lift(3).get.split("=").lift(0).get
        val valueInsert = ar.lift(3).get.split("=").lift(1) match {
          case None =>
            print(s"Insert attribute for key $keyInsert:")
            scanner.nextLine()
          case _ => ar.lift(3).get.split("=").lift(1).get
        }
        fdao.insertAttribute(index, keyInsert, valueInsert) match {
          case DaoReturnMessage.SUCCESS =>
            println(s"Line $index, attribute: ${ar.lift(3).get} ADDED!")
            println(s"New line $index: ${fdao.getLine(index)._1}")
          case DaoReturnMessage.KEY_INDEX_IS_RESERVED =>
            println(s"Attribute: ${ar.lift(3).get}, key named index, it is a reserved key, ERROR!")
          case _ => println(s"Line index:'$index' does not exist, RETRY!")
        }
      case Array("upd", "attr", _, _) =>
        val index = ar.lift(2).get
        val keyUpdate = ar.lift(3).get.split("=").lift(0).get
        val valueUpdate = ar.lift(3).get.split("=").lift(1) match {
          case None =>
            print(s"Insert attribute for key $keyUpdate:")
            scanner.nextLine()
          case _ => ar.lift(3).get.split("=").lift(1).get
        }
        fdao.updateAttribute(index, keyUpdate, valueUpdate) match {
          case DaoReturnMessage.KEY_INDEX_IS_RESERVED =>
            println(s"Line '$index' key named index, it is a reserved key, ERROR!")
          case DaoReturnMessage.NO_LINE => println(s"Line '$index' does not exist, RETRY!")
          case DaoReturnMessage.NO_ATTRIBUTE_CHANGED => println(s"In line '$index' '$keyUpdate' does not exist, RETRY!")
          case DaoReturnMessage.SUCCESS =>
            println(s"Line $index, attribute: $keyUpdate UPDATE!")
            println(s"New line $index: ${fdao.getLine(index)._1}")
        }
      case Array("rem", "attr", _, _) =>
        val index = ar.lift(2).get
        val keyName = ar.lift(3).get
        fdao.removeAttribute(index, keyName) match {
          case DaoReturnMessage.KEY_INDEX_IS_RESERVED =>
            println(s"Line '$index' key named index, it is a reserved key, ERROR!")
          case DaoReturnMessage.NO_LINE => println(s"Line '$index' does not exist, RETRY!")
          case DaoReturnMessage.NO_ATTRIBUTE_CHANGED => println(s"In line '$index' '$keyName' does not exist, RETRY!")
          case DaoReturnMessage.SUCCESS => println(s"Line $index, attribute: $keyName REMOVED!")
        }
      case Array("help") =>
        help()
      case Array("exit") =>
        println("Goodbye!")
        System.exit(0)
      case _ =>
        println("Command not found, RETRY!")
        help()
    }

    selection(fixLine = false, password)
  }
}
