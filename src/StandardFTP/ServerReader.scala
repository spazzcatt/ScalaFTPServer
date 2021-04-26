package StandardFTP

import java.io.{BufferedReader, DataInputStream, File, FileNotFoundException, InputStreamReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

class ServerReader(val socketInput : Socket) extends Thread{
  val socket = socketInput
  var incomingMessage = ""
  var messageQueue = new mutable.Queue[String]()
  val inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream))
  var currentString = ""
  var buildingString = false
  val outputWriter = new PrintWriter(socket.getOutputStream, true)
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
              val ishandled = messageHandler(currentString)
              if(ishandled){
                outputWriter.write("command complete")
              }else{
                outputWriter.write("command incomplete ERROR")
              }
              messageQueue.addOne(currentString)
              currentString = ""
            }
            case _ => currentString += temp + "\n"
          }
          println(s"Received new message from ${socket.getInetAddress}")
        }
      }
      Thread.sleep(100)
    }
    socket.close()
  }
  def getMessageQueue (): mutable.Queue[String] ={
    messageQueue
  }

  def messageHandler(input: String): Boolean ={
    var messageHandled = false
    val command = input.split("command ")(1).split(" ")
    command(0) match {
      case "add" =>
        val filename = input.split("command: add ")(0)
        if(new java.io.File(filename).exists){
          println("Cannot add file already exists")
        }
        println(s"adding file: $filename")
        var printWriter : Any = None
        try{
          printWriter = new PrintWriter(new File(filename))
        }catch {
          case e: FileNotFoundException => println("File not found exception thrown")
        }
        val startIndex = input.indexOf("startFile")
        val endIndex = input.indexOf("endFile")
        val fileContents = input.substring(startIndex, endIndex)
        if(printWriter != None ){
          printWriter.asInstanceOf[PrintWriter].write(fileContents) //How to do type casting in Scala
        }
        println("message handled add")
        messageHandled = true
      case _ =>
        println("message not handled: " + command )
    }
    if(messageHandled){
      println("message handled")
    }
    messageHandled
  }
}

