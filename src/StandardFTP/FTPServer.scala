package StandardFTP

/**
 * A Standard Extendable FTP Server
 * It uses Java ServerSockets since Scala has no built in networking
 */
class FTPServer {
  def main(args: Array[String]): Unit = {
      val serverSocketAcceptor = new ServerSocketAcceptor
      serverSocketAcceptor.startServer()
      while(serverSocketAcceptor.readerList.isEmpty){
        wait(1000)
      }
      val reader = serverSocketAcceptor.getReaderList()
      while(!serverSocketAcceptor.readerList.isEmpty){
        for( itm <- reader){
          if(!itm.messageQueue.isEmpty){
            messageHandler(itm.messageQueue.head, itm)
          }
        }
      }
    }

  /**
   * Handle what input you would like
   * @param input
   */
  def messageHandler(input : String, serverReader: ServerReader): Unit ={
    input match {
      case "echo" => serverReader.socket.getOutputStream.write(s"echo received ${serverReader.socket.getInetAddress.toString}".getBytes())
    }
  }

}
