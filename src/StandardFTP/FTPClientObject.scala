package StandardFTP

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.net.Socket
import scala.io.StdIn.readLine

object FTPClientObject {
  def main(args: Array[String]): Unit = {
    Thread.sleep(5000)
    val socket = new Socket(args(0), args(1).toInt)
    val out = new PrintStream(socket.getOutputStream)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    println("FTP CLIENT START\n" +
      "-" * 20 + "\n" +
      "Developed for Scala Class COMP 4210 at Otterbein University\n" +
      "Developed by Connor May\n" +
      "Release date: April 2020\n" +
      "-" * 20 + "\n" +
      "USAGE:\n" +
      "help ->\t\tDisplays usage and syntax\n" +
      "search [filename] ->\t\tSearches FTP server directory for files that name matches 'filename'\n" +
      "request [filename] ->\t\tRequests file from FTP server (credentials may be required)\n" +
      "add [filepath] ->\t\tUploads the file at the given filepath to the FTPServer\n" +
      "delete [filename] ->\t\tDeletes the file with given 'filename' (requires credentials)")
    var input = ""
    while(input != "quit"){
      input = readLine()
      input match {
        case "help" => println(
          "USAGE:\n" +
          "help ->\t\tDisplays usage and syntax\n" +
          "search [filename] ->\t\tSearches FTP server directory for files that name matches 'filename'\n" +
          "request [filename] ->\t\tRequests file from FTP server (credentials may be required)\n" +
          "add [filepath] ->\t\tUploads the file at the given filepath to the FTPServer\n" +
          "delete [filename] ->\t\tDeletes the file with given 'filename' (requires credentials)")

      }
      println("Enter Command: ")
    }
  }

  def getCredentials(out: PrintStream, in: BufferedReader): Unit ={
    println()
    println("Credentials are required for this action\n" +
      "WARNING: non-secure")
    println("Username:\t")
    val username = readLine()
    println("Password:\t")
    val password = readLine()
    out.print(s"credentials_request username=$username password=${password.hashCode}")

  }
}
