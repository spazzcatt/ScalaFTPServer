package StandardFTP

object FTPClientObject {
  def main(args: Array[String]): Unit = {
    Thread.sleep(5000)
    println("Starting now...")
    val clientInstance = new FTPClient
    clientInstance.main(args)
  }
}
