import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Handler {
    default void handle(Request request, BufferedOutputStream responseStream) {
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
}
