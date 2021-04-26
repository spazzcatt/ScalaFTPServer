package StandardFTP

import java.io.{BufferedReader, File, InputStreamReader, PrintStream}
import java.net.Socket
import java.security.SecureRandom
import scala.io.StdIn.readLine
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.security.spec.KeySpec

/**
 * Encompasses all Client side code
 */
object FTPClientObject {

  val USAGE_STRING = "USAGE:\n" +
    "help ->\t\t\t\t\t\tDisplays usage and syntax\n" +
    "search [filename] ->\t\tSearches FTP server directory for files that name matches 'filename'\n" +
    "request [filename] ->\t\tRequests file from FTP server (credentials may be required)\n" +
    "add [filepath] ->\t\t\tUploads the file at the given filepath to the FTPServer\n" +
    "delete [filename] ->\t\tDeletes the file with given 'filename' (requires credentials)\n" +
    "list [client | server] ->\tLists the current directory contents of client or server respectively\n" +
    "cat [filename] ->\t\t\tOutputs file contents to terminal"

  /**
   * Gets a list of the files in the given directory
   * @param dir directory to list files in
   * @return List of all the files in the given directory
   */
  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  /**
   * Prompts user for username and password. Then hashes the password and sends it to the server.
   * Uses SHA1 algorithm for encryption
   * @param out Output Stream for Socket
   * @param in Input Stream for Socket
   */
  def getCredentials(out: PrintStream, in: BufferedReader): Unit ={
    val random = new SecureRandom()
    val salt = new Array[Byte](16)
    random.nextBytes(salt)
    println()
    println("Credentials are required for this action\n") // WARNING: Password is not secure password hash
    println("Username:\t")
    val username = readLine()
    println("Password:\t")
    val password = readLine()
    val spec = new PBEKeySpec(password.toCharArray, salt, 65536, 128)
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val hash = factory.generateSecret(spec).getEncoded
    out.print(s"credentials_request username=$username password=${hash}")
    println("Hash Generated: " + hash.toString)

  }

  /**
   * Handles arguments for Client side.
   * Reads commands on command line and responds accordingly
   * @param args [ip address] [port number]
   */
  def main(args: Array[String]): Unit = {
    Thread.sleep(500)
    val socket = new Socket(args(0), args(1).toInt)
    val out = new PrintStream(socket.getOutputStream)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    println("FTP CLIENT START\n" +
      "-" * 20 + "\n" +
      "Developed for Scala Class COMP 4210 at Otterbein University\n" +
      "Developed by Connor May\n" +
      "Release date: April 2020\n" +
      "-" * 20 + "\n" +
      USAGE_STRING)
    println()
    print("Enter Command: ")
    var input = ""
    while(input != "quit"){
      input = readLine()
      val firstArg = input.split(" ")
      firstArg(0) match {
        case "cat" => {
          val source = scala.io.Source.fromFile(firstArg(1))
          if(source == null){
            System.err.println("Problem opening file... Try again.")
          }else{
            val fileContents = source.getLines mkString "\n"
            println("File Read: \n" + "-" * 20 + "\n" + fileContents)
          }
        }
        case "add" => {
          val source = scala.io.Source.fromFile(firstArg(1))
          if(source == null){
            System.err.println("Problem opening file... Try again.")
          }else{
            val fileContents = source.getLines mkString "\n"
            println("Writing File...")
            out.println("startMessage")
            out.println("command add " + firstArg(1))
            out.println("startFile")
            out.println(fileContents)
            out.println("endFile")
            out.println("endMessage")
          }
        }
        case "list" => {
          println(s"Getting files from: ${firstArg(1)}")
          if(firstArg(1) == "client"){
            val currentFiles =  getListOfFiles(System.getProperty("user.dir"))
            println("Current Files In Client Directory:")
            currentFiles.foreach(f => println(s"${f.getName}"))
            println()
          }else{
            out.print("command: list")
          }
        }
        case "remove" => {
          out.print("startMessage")
          out.println("command remove " + firstArg(1))
          out.println("endMessage")
        }
        case "request" => {
          println()
          //TODO: read and save files from socket
        }
        case _ => println(USAGE_STRING)
      }
      println()
      print("Enter Command: ")
    }
  }




}
