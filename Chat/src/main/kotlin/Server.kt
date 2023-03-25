import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*

fun main() {
    val port = 8000
    val serverSocket = ServerSocket(port)
    val clients = Collections.synchronizedList(mutableListOf<Socket>())
    println("Сервер запущен на порту $port")

    while (true) {
        val clientSocket = serverSocket.accept()
        println("Новое соединение: ${clientSocket.inetAddress.hostAddress}")
        clients.add(clientSocket)

        Thread(
            Runnable {
                try {
                    val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

                    while (true) {
                        val message = reader.readLine() ?: break
                        println("Получено сообщение от ${clientSocket.inetAddress.hostAddress}: $message")
                        sendToAllClients(message, clients, clientSocket)
                    }
                    reader.close()
                } catch (e: Exception) {
                    println("Ошибка при обработке соединения: ${e.message}")
                } finally {
                    clientSocket.close()
                    clients.remove(clientSocket)
                    println("Соединение закрыто: ${clientSocket.inetAddress.hostAddress}")
                }
            }
        ).apply { start() }
    }
}

fun sendToAllClients(message: String, clients: List<Socket>, currSocket: Socket) {
    clients.forEach { clientSocket ->
        if (clientSocket != currSocket) {
            try {
                val outputStream = PrintWriter(clientSocket.getOutputStream(), true)
                outputStream.println("${currSocket.inetAddress.hostAddress}: $message")
            } catch (e: Exception) {
                println("Ошибка при отправке сообщения клиенту: ${e.message}")
            }
        }
    }
}
