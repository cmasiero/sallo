import it.cristiano.sallo.dao.FileDao

/**
  * Created by cristiano on 11/21/16.
  */


object Sallo {

//  val EncryptRE = """^\s*([a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+\s*){3}$""".r

  /**
    * <b>command :<b/> usage
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val command = args.mkString(" ")

    println(s"command:'$command'")

    val scanner = new java.util.Scanner(System.in)

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




    //
//    command match {
//      case ""     => help
//      case "help" => help
//      case EncryptRE(command) => encrypt(args.mkString(" "))
//      case _ => commandNotFound
//    }


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

  def commandNotFound: Unit = {
    println("Command not found")
    help
  }

  def encrypt (command: String) : Unit = {
    println("-> " + command)
    //val fdao = new FileDao()
  }


}
