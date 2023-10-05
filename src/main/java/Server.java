import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
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
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                // just close socket
                return;
            }

            Request request = new Request(parts[0], parts[1], lines);


            if (handlers.get(parts[0]).containsKey(parts[1])) {
                Handler handler = handlers.get(parts[0]).get(parts[1]);
                handler.handle(request, out);
                return;
            }

            out.write((
                    "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
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
