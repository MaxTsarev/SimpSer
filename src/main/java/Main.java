import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        final var server = new Server();
//         код инициализации сервера (из вашего предыдущего ДЗ)
//
//         добавление хендлеров (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
                try {
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + 0 + "\r\n" +
                                    "Content-Length: " + 0 + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
                try {
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + 0 + "\r\n" +
                                    "Content-Length: " + 0 + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        server.start(9999);
    }
}

