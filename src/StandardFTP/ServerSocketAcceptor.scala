package StandardFTP

import java.net.{ServerSocket, Socket}
import scala.collection.mutable.ListBuffer

/**
 * Uses Java ServerSocket to provide abstraction and give immutable messages back
 * @DEPRECIATED (was originally going to do it here but opted for different organization
 */

class ServerSocketAcceptor {
  private val port = 4545
  private val serverSocket = new ServerSocket(port)
  var readerList = new ListBuffer[ServerReader]()
  println("ServerSocketClass Created")

  import java.io.BufferedReader
  import java.io.InputStreamReader
  import java.net.URL

  val whatismyip = new URL("http://checkip.amazonaws.com")
  val in = new BufferedReader(new InputStreamReader(whatismyip.openStream))

  val ip: String = in.readLine //you get the IP as a String
  println("External IP address: " + ip)


  def startServer(): Unit ={
      println(s"Attempting to accept connections on port: $port")
      while(true){
        val server = serverSocket.accept()
        if(server != null) {
          val currentThread = new ServerReader(server)
          currentThread.run()
          readerList.addOne(currentThread)
        }
      }
    }
  def getReaderList(): ListBuffer[ServerReader] ={
    readerList
  }
}


