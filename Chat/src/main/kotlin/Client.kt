import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main() {
    val serverAddress = "localhost" // здесь указываем адрес сервера
    val serverPort = 8000 // здесь указываем порт сервера

    val socket = Socket(serverAddress, serverPort)
    println("Успешно подключились к серверу")

    val input = BufferedReader(InputStreamReader(System.`in`))
    val output = PrintWriter(socket.getOutputStream(), true)
    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

    println(reader.readLine())

    val readThread = ReadThread(reader)
    readThread.start()

    var message: String
    while (true) {
        message = input.readLine() ?: break
        output.println(message)
    }

    socket.close()
}