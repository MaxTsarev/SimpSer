import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static Map<String, Map<String, Handler>> handlers = new HashMap<>();

    private final static ExecutorService executorService = Executors.newFixedThreadPool(64);

    public void start(int port) {
        executorService.execute(() -> {
            try (final ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Server.connectProcess(serverSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
        });
    }

    private static void connectProcess(ServerSocket serverSocket) {

        try (
                final var socket = serverSocket.accept();
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {

            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final var requestLine = in.readLine();
            if (requestLine == null) return;

            StringBuilder requests = new StringBuilder();
            String s;
            while (!Objects.equals(s = in.readLine(), "")) {
                requests.append(s).append("\n");
            }

            final String[] lines = requests.toString().split("\n");

            System.out.println("Соединение установлено!");
            var parts = requestLine.split(" ");

            if (parts.length != 3) {
                // just close socket
                return;
            }

            int value = parts[1].indexOf("?");
            String withoutQueryStr = null;
            if (value != -1) {
                withoutQueryStr = parts[1].substring(value);
                parts[1] = parts[1].substring(0, value);
            }

            Request request = new Request(parts[0], parts[1], withoutQueryStr, lines);
            request.getQueryParams();
            request.getQueryParam("image");

            if (handlers.get(parts[0]).containsKey(parts[1])) {
                Handler handler = handlers.get(parts[0]).get(parts[1]);
                handler.handle(request, out);

                return;
            }

            final var filePath = Path.of(".", "public", "/notFound.html");
            final var mimeType = Files.probeContentType(filePath);
            final var length = Files.size(filePath);
            out.write((
                    "HTTP/1.1 300 Redirection\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, out);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addHandler(String request, String messages, Handler handler) {
        if (!handlers.containsKey(request)) {
            Map<String, Handler> value = new HashMap<>();
            value.put(messages, handler);
            handlers.put(request, value);
            return;
        }
        handlers.get(request).put(messages, handler);
    }

}
