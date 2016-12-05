import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

import it.cristiano.sallo.dao.FileDao
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
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val password = Files.exists(Paths.get(ENCRYPT_FILE)) match {
      case true => login
      case false => initializeFile
    }

    help
    selection(true, password)


  }

  def login: String = {
    print("password:")
    val password = scanner.next()
    CryptoUtils.decrypt(password, ENCRYPT_FILE)
    password
  }

  def initializeFile: String = {
    val exampleRecord = "sallo=first-line,attribute=first-attribute"
    println("*********************************** INITIALIZE ***********************************")
    println("Sallo needs create your encrypted file, you must insert your pass!")
    println("Enter your password: ")
    val password = scanner.next()
    val pw = new PrintWriter(new File(DECRYPT_FILE))
    pw.write(exampleRecord)
    pw.close
    CryptoUtils.encrypt(password, DECRYPT_FILE)
    new File(DECRYPT_FILE).delete()
    println(s"Your encrypted file has been created, it contains an example records : '$exampleRecord'")
    password
  }

  def help: Unit = {
    println("*********************************** HELP ***********************************")
    println("1) Add a csv file: add file path/yourfilename.csv")
    println("2) Add line      : add line attribute1=attribute1,attribute2=attribute2,attributeEtc=attributeEtc")
    //    println("")
    //    println("")
    //    println("")
    //    println("")
    //    println("")
  }

  def selection(fixLine: Boolean, password: String = DEFAULT_PASS): Unit = {

    if (Files.exists(Paths.get(ENCRYPT_FILE)) == false) {
      println("Archive file has been removed, FATAL ERROR!")
      System.exit(0)
    }

    val fdao = new FileDao(password, ENCRYPT_FILE)

    print("Command:")
    if (fixLine) scanner.nextLine
    val command = scanner.nextLine

    val ar = command.split(" ")
    ar match {
      case Array("add", "file", _) => {
        val filePath = ar.lift(2).get
        if (Files.exists(Paths.get(filePath)) == false)
          println(s"File '$filePath' does not exist, RETRY!")
        else {
          fdao.addFile(ar.lift(2).get)
          println(s"File: '$filePath' ADDED!")
        }
      }
      case Array("add", "line", _) => {
        fdao.addLine(ar.lift(2).get)
        println(s"Line: '${ar.lift(2).get}' ADDED!")
      }
      case Array("exit", _*) => {
        println("Goodbye")
        System.exit(1)
      }
      case _ => {
        println("Command inserted not found, RETRY!")
        help
      }
    }

    selection(false, password)

  }
}
