package StandardFTP

import java.io.{BufferedReader, DataInputStream, InputStreamReader}
import java.net.Socket
import scala.collection.mutable

class ServerReader(val socketInput : Socket) extends Thread{
  val socket = socketInput
  var incomingMessage = ""
  var messageQueue = new mutable.Queue[String]()
  val inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream))
  var currentString = ""
  var buildingString = false
  override def run(): Unit = {
    println(s"New Connection made!\nReady and listening to client: ${socket.getRemoteSocketAddress}")
    println(s"Socket ${socket.getRemoteSocketAddress} connection status: ${socket.isConnected}")
    while(incomingMessage != "End_Connection_Message_00" || incomingMessage != "End_Connection_Message_01"){
      if(inputReader.ready()){
        val temp = inputReader.readLine()
        if(temp != ""){
          incomingMessage = temp
          //messageQueue.addOne(incomingMessage)
          temp match {
            case "startMessage" => buildingString = true
            case "endMessage" => {
              buildingString = false
              println("Command built: " + currentString)
              messageQueue.addOne(currentString)
              currentString = ""
            }
            case _ => currentString += temp + "\n"
          }
          println(s"Received new message from ${socket.getInetAddress}")
        }
        println(s"read: $temp")
      }
      Thread.sleep(100)
    }
    socket.close()
  }
}

