package StandardFTP

import java.net.{ServerSocket, Socket}
import scala.collection.mutable.ListBuffer

/**
 * Uses Java ServerSocket to provide abstraction and give immutable messages back
 */

class ServerSocketAcceptor {
  private val port = 4545
  private val serverSocket = new ServerSocket(port)
  var readerList = new ListBuffer[ServerReader]()
  println("ServerSocketClass Created")


    def startServer(): Unit ={
      println(s"Attempting to accept connections on port: $port")
      while(true){
        val server = serverSocket.accept();
        val currentThread = new ServerReader(server)
        currentThread.run()
        readerList.addOne(currentThread)
      }
    }
  def getReaderList(): ListBuffer[ServerReader] ={
    readerList
  }
}


