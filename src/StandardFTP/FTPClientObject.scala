package StandardFTP

object FTPClientObject {
  def main(args: Array[String]): Unit = {
    val clientInstance = new FTPClient
    clientInstance.main(args)
  }
}
