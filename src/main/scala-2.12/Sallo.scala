import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

import it.cristiano.sallo.dao.FileDao
import it.cristiano.sallo.util.CryptoUtils

import scala.io.Source

/**
  * Created by cristiano on 11/21/16.
  */


object Sallo {

  val DEFAULT_PASS                    = "DEFAULT_PASS"
  val DECRYPT_FILE                    = "file.csv"
  val ENCRYPT_FILE                    = "file.csv.enc"

  val scanner = new java.util.Scanner(System.in)

  /**
    * <b>command :<b/> usage
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val password = Files.exists(Paths.get(ENCRYPT_FILE)) match {
      case true  => login
      case false => initializeFile
    }

    /*
        println(s"command:'$command'")


        //  prompt for the user's name
        print("Enter your name: ")

        // get their input as a String
        val username = scanner.next()

        // prompt for their age
        System.out.print("Enter your age: ")

        // get the age as an int
        val age = scanner.nextInt()

    //    println(String.format("%s, your age is %d", username, age));
        println(s"$username, your age is $age")
    */

  }

  def help: Unit = {
    println("*********************************** HELP ***********************************")
    println("1) Encrypt your csv file : java -jar sallo.jar encrypt yourpassword path/yourfilename.csv")
    println("")
    println("")
    println("")
    println("")
    println("")
    println("")
  }

  def login: String = {
    ""
  }

  def initializeFile: String = {
    val exampleRecord = "sallo=first-line,attribute=first-attribute"
    println("*********************************** INITIALIZE ***********************************")
    println("Sallo needs create your encrypted file, you must insert your pass!")
    System.out.print("Enter your password: ")
    val password = scanner.next()
    val pw = new PrintWriter(new File(DECRYPT_FILE))
    pw.write(exampleRecord)
    pw.close
    CryptoUtils.encrypt(password,DECRYPT_FILE)
    new File(DECRYPT_FILE).delete()
    println(s"Your encrypted file has been created, it contains an example records : '$exampleRecord'")
    password
  }

  def commandNotFound: Unit = {
    println("Command not found")
    help
  }

  def encrypt (command: String) : Unit = {
    println("-> " + command)
    //val fdao = new FileDao()
  }


}
