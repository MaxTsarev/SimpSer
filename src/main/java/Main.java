import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        final var server = new Server();

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

        server.addHandler("GET", "/classic.html", new Handler() {
            @Override
            public void handle(Request request, BufferedOutputStream responseStream) {
                try {
                    final var filePath = Path.of(".", "public", request.getPath());
                    final var mimeType = Files.probeContentType(filePath);
                    final var length = Files.size(filePath);
                    final var template = Files.readString(filePath);
                    final var content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + content.length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    responseStream.write(content);
                    responseStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        server.addHandler("GET", "/events.html", new Handler() {
        });
        server.addHandler("GET", "/form.html", new Handler() {
        });
        server.addHandler("GET", "/forms.html", new Handler() {
        });
        server.addHandler("GET", "/index.html", new Handler() {
        });
        server.addHandler("GET", "/links.html", new Handler() {
        });
        server.addHandler("GET", "/resources.html", new Handler() {
        });
        server.addHandler("GET", "/spring.png", new Handler() {
        });
        server.addHandler("GET", "/spring.svg", new Handler() {
        });
        server.addHandler("GET", "/app.js", new Handler() {
        });
        server.addHandler("GET", "/events.js", new Handler() {
        });
        server.addHandler("GET", "/styles.css", new Handler() {
        });


        server.addHandler("GET", "/default-get.html", new Handler() {
            @Override
            public void handle(Request request, BufferedOutputStream responseStream) {
                try {

                    final var filePath = Path.of(".", "public", request.getPath());
                    final var mimeType = Files.probeContentType(filePath);
                    final var length = Files.size(filePath);
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    Files.copy(filePath, responseStream);
                    responseStream.flush();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        server.start(9999);
    }
}

