package StandardFTP

import java.io.{BufferedReader, DataInputStream, File, FileNotFoundException, InputStreamReader, PrintWriter}
import java.net.Socket
import scala.collection.mutable

/**
 * A thread to handle incoming server requests
 * @param socketInput the socket connection the thread is listening to
 */
class ServerReader(val socketInput : Socket) extends Thread{
  val socket = socketInput
  var incomingMessage = ""
  var messageQueue = new mutable.Queue[String]()
  val inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream))
  var currentString = ""
  var buildingString = false
  val outputWriter = new PrintWriter(socket.getOutputStream, true)

  /**
   * Thread's "main" function
   * handles commands and responds accordingly
   */
  override def run(): Unit = {
    println(s"New Connection made!\nReady and listening to client: ${socket.getRemoteSocketAddress}")
    println(s"Socket ${socket.getRemoteSocketAddress} connection status: ${socket.isConnected}")
    while(incomingMessage != "End_Connection_Message_00" || incomingMessage != "End_Connection_Message_01"){
      if(inputReader.ready()){
        val temp = inputReader.readLine()
        if(temp != ""){
          incomingMessage = temp
          temp match {
            case "startMessage" => buildingString = true
            case "endMessage" => {
              buildingString = false
              println("Command built: " + currentString)
              val ishandled = messageHandler(currentString)
              if(ishandled){
                outputWriter.write("commandStatus complete")
                println("wrote: commandStatus complete")
              }else{
                println("wrote: commandStatus incomplete ERROR")
                outputWriter.write("commandStatus incomplete ERROR")
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

  /**
   * A getter for the messageQueue in case the message handling needs to be done somewhere else
   * @return
   */
  def getMessageQueue (): mutable.Queue[String] ={
    messageQueue
  }

  /**
   * Handles messages and does the commands it parsed
   * @param input message string
   * @return true if it successfully parsed a command and false if there was an error or the command wasn't recognized
   */
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
        var exceptionThrown = false
        try{
          printWriter = new PrintWriter(new File(filename))
        }catch {
          case e: FileNotFoundException => {
            println("File not found exception thrown")
            exceptionThrown = true
          }
        }
        val startIndex = input.indexOf("startFile")
        val endIndex = input.indexOf("endFile")
        val fileContents = input.substring(startIndex, endIndex)
        if(printWriter != None ){
          printWriter.asInstanceOf[PrintWriter].write(fileContents)
        }
        if(exceptionThrown){
          messageHandled = false
        }else{
          messageHandled = true
          println("message handled add")
        }
      case "remove" => {
        val filename = input.split("command: add ")(0)
        deleteFile(filename)
      }
      case _ =>
        println("message not handled: " + command )
    }
    messageHandled
  }

  /**
   * For use in the remove method
   * @param path path and filename
   * @return None
   */
  private def deleteFile(path: String) = {
    val fileTemp = new File(path)
    if (fileTemp.exists) {
      fileTemp.delete()
    }
  }
}

