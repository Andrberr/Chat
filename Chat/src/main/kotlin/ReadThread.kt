import java.io.BufferedReader

class ReadThread(private val reader: BufferedReader) : Thread() {
    override fun run() {
        while (true) {
            val message = reader.readLine() ?: break
            println("Ответ от сервера: $message")
        }
    }
}