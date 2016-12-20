import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

import it.cristiano.sallo.dao.{AttributeValidation, ElementValidation, FileDao, LineValidation}
import it.cristiano.sallo.dao.message.DaoReturnMessage
import it.cristiano.sallo.util.CryptoUtils

import scala.io.Source

/**
  * Created by cristiano on 11/21/16.
  */


object Sallo {

  val DEFAULT_PASS = "DEFAULT_PASS"
  val DECRYPT_FILE = "file.csv"
  val ENCRYPT_FILE = "file.csv.enc"

  val scanner = new java.util.Scanner(System.in)

  /**
    * <b>command :<b/> usage
    *
    * @param args arguments
    */
  def main(args: Array[String]): Unit = {
    val password = if (Files.exists(Paths.get(ENCRYPT_FILE))) {
      login
    } else {
      initializeFile
    }
    help()
    selection(fixLine = true, password)
  }

  def login: String = {
    print("password:")
    val password = scanner.next()
    CryptoUtils.decrypt(password, ENCRYPT_FILE)
    password
  }

  def initializeFile: String = {
    val exampleRecord = "sallo=first-line,attribute=first-attribute"
    println("*********************************  INITIALIZE  *********************************")

    println("Sallo needs create your encrypted file, you must insert your pass!")
    println("Enter your password: ")
    val password = scanner.next()
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
    println("Remove line at index            : remove line [index]")
    println("Get all line                    : get all")
    println("Get line at index               : get line [index]")
    println("Match string                    : match [String]")
    println("Add attribute at line index     : add attribute [index] attributeName=attributeValue")
    println("Update attribute at line index  : update attribute [index] attributeName=attributeValue")
    println("Remove attribute at line index  : remove attribute [index] attributeName")

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
        fdao.addLine(line)
        println(s"Line: '$line' ADDED!")
      case Array("remove", "line", _) =>
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
      case Array("add", "attribute", _, _) =>
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
          case _ => println(s"Line index:'$index' does not exist, RETRY!")
        }
      case Array("update", "attribute", _, _) =>
        val index = ar.lift(2).get
        val keyUpdate = ar.lift(3).get.split("=").lift(0).get
        val valueUpdate = ar.lift(3).get.split("=").lift(1) match {
          case None =>
            print(s"Insert attribute for key $keyUpdate:")
            scanner.nextLine()
          case _ => ar.lift(3).get.split("=").lift(1).get
        }
        fdao.updateAttribute(index, keyUpdate, valueUpdate) match {
          case DaoReturnMessage.NO_LINE => println(s"Line '$index' does not exist, RETRY!")
          case DaoReturnMessage.NO_ATTRIBUTE_CHANGED => println(s"In line '$index' '$keyUpdate' does not exist, RETRY!")
          case DaoReturnMessage.SUCCESS =>
            println(s"Line $index, attribute: $keyUpdate UPDATE!")
            println(s"New line $index: ${fdao.getLine(index)._1}")
        }
      case Array("remove", "attribute", _, _) =>
        val index = ar.lift(2).get
        val keyName = ar.lift(3).get
        fdao.removeAttribute(index, keyName) match {
          case DaoReturnMessage.NO_LINE => println(s"Line '$index' does not exist, RETRY!")
          case DaoReturnMessage.NO_ATTRIBUTE_CHANGED => println(s"In line '$index' '$keyName' does not exist, RETRY!")
          case DaoReturnMessage.SUCCESS => println(s"Line $index, attribute: $keyName REMOVED!")
        }
      case Array("exit") =>
        println("Goodbye!")
        System.exit(1)
      case _ =>
        println("Command not found, RETRY!")
        help()
    }

    selection(fixLine = false, password)
  }
}
