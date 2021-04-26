package StandardFTP

import java.net.ServerSocket
import scala.collection.mutable.ListBuffer
import java.io.{BufferedReader, File, InputStreamReader, PrintWriter}
import java.net.URL

/**
 * A FTP serverside. It accepts new connections and handles arguments
 */

object ScalaFTPServerObject {
  private val port = 4545
  private val serverSocket = new ServerSocket(port)
  var readerList = new ListBuffer[ServerReader]()
  val whatismyip = new URL("http://checkip.amazonaws.com")
  val in = new BufferedReader(new InputStreamReader(whatismyip.openStream))
  val ip: String = in.readLine //you get the IP as a String

  /**
   * Starts server and gets external IP address
   * Infinitely Accepts connections and gives them their own thread
   */
  def startServer(): Unit ={
    println("FTP Server Started...")
    println("Created by: Connor May")
    println("Release Date: April 2021")
    println("-" * 30)
    println("External IP address: " + ip)
    println(s"Attempting to accept connections on port: $port")
    while(true){
      val clientSocket = serverSocket.accept()
      if(clientSocket != null) {
        val currentThread = new ServerReader(clientSocket)
        currentThread.run()
        readerList.addOne(currentThread)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    startServer()
  }





}
