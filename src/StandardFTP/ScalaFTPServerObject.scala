package StandardFTP

object ScalaFTPServerObject {
  def main(args: Array[String]): Unit = {
    val serverInstance = new FTPServer
    serverInstance.main()
  }
}
