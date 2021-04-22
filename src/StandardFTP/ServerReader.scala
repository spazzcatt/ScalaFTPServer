package StandardFTP

import java.io.DataInputStream
import java.net.Socket
import scala.collection.mutable

class ServerReader(val socketInput : Socket) extends Thread{
  val socket = socketInput
  val inputStream = new DataInputStream(socket.getInputStream)
  var incomingMessage = ""
  var messageQueue = new mutable.Queue[String]()
  override def run(): Unit = {
    println(s"New Connection made!\nReady and listening to client: ${socket.getRemoteSocketAddress}")
    while(incomingMessage != "End_Connection_Message_00" || incomingMessage != "End_Connection_Message_01"){
      val temp = inputStream.readUTF()
      if(temp != ""){
        incomingMessage = temp
        messageQueue.addOne(incomingMessage)
        println(s"read: $temp")
      }
    }
    socket.close()
  }
}

