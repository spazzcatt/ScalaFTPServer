package StandardFTP

import java.net.ServerSocket
import scala.collection.mutable.ListBuffer
import java.io.{BufferedReader, File, InputStreamReader, PrintWriter}
import java.net.URL

/*
NOTE: This implementation only supports one
 */

object ScalaFTPServerObject {
  private val port = 4545
  private val serverSocket = new ServerSocket(port)
  var readerList = new ListBuffer[ServerReader]()
  val whatismyip = new URL("http://checkip.amazonaws.com")
  val in = new BufferedReader(new InputStreamReader(whatismyip.openStream))
  val ip: String = in.readLine //you get the IP as a String


  def startServer(): Unit ={
    println("FTP Server Started...")
    println("Created by: Connor May")
    println("Release Date: April 2021")
    println("-" * 30)
    println("External IP address: " + ip)
    println(s"Attempting to accept connections on port: $port")
    while(true){
      val server = serverSocket.accept()
      if(server != null) {
        val currentThread = new ServerReader(server)
        currentThread.run()
        readerList.addOne(currentThread)
      }
      //TODO: call message handler on first message in message queue
      //This should loop around and call the first message on every thread giving equal opportunity to each thread
      readerList.foreach(f => messageHandler(f.messageQueue.head))

    }
  }

  //TODO: Change to using different message protocol to make parsing messages easier

  def messageHandler(input: String): Unit ={
    input.split("command: ")(0) match {
      case "add" =>
        val filename = input.split("command: add ")(0)
        println(s"adding file: $filename")
        val printWriter = new PrintWriter(new File(filename))
        val startIndex = input.indexOf("startFile")
        val endIndex = input.indexOf("endFile")
        val fileContents = input.substring(startIndex, endIndex)
        printWriter.write(fileContents)
    }
  }


  def main(args: Array[String]): Unit = {
    startServer()
  }





}
