import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.UnknownHostException

fun main() {
    print("Введите IP-адрес сервера: ")
    val serverAddress = readlnOrNull() ?: ""
    print("Введите порт сервера: ")
    val serverPort = readlnOrNull()?.toIntOrNull()
    if (serverPort != null && serverPort >= 0 && serverPort <= 65535) {
        try {
            val socket = Socket(serverAddress, serverPort)
            println("Успешно подключились к серверу")

            val input = BufferedReader(InputStreamReader(System.`in`))
            val output = PrintWriter(socket.getOutputStream(), true)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            Thread(
                Runnable {
                    try {
                        while (true) {
                            val message = reader.readLine() ?: break
                            println(message)
                        }
                    } catch (e: Exception) {
                        println("Ошибка при чтении сообщения от сервера: ${e.message}")
                    } finally {
                        reader.close()
                    }
                }
            ).apply { start() }

            var message: String
            while (true) {
                message = input.readLine() ?: break
                output.println(message)
            }

            output.close()
            input.close()
            socket.close()
        } catch (e: UnknownHostException) {
            println("Неизвестный адрес сервера.")
        }
    } else println("Некорректное значение порта.")
}
