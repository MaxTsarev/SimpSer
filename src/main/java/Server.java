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
        executorService.submit(() -> {
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

            String requestLine;
            String[] headers;
            String requestMethod;
            String path;
            String queryString = null;
            String body = null;


            requestLine = in.readLine();
            if (requestLine == null) return;

            headers = headersParse(in);

            if (ifContentTypeIsXWwwFormUrlencoded(headers)) {
                body = xWwwFormUrlencodedBodyParser(in);
            }

            System.out.println("Соединение установлено!");


            var parts = requestLine.split(" ");
            if (parts.length != 3) {
                return;
            }

            requestMethod = parts[0];


            int value = parts[1].indexOf("?");
            if (value != -1) {
                queryString = parts[1].substring(value);
                parts[1] = parts[1].substring(0, value);
            }

            path = parts[1];

            Request request = new Request(requestMethod, path, queryString, headers, body);
            request.getQueryParams();
            request.getQueryParam("image");
            request.getPostParams();
            request.getPostParam("value");

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

    private static String xWwwFormUrlencodedBodyParser(BufferedReader in) throws IOException {
        StringBuilder body = new StringBuilder();
        char w;
        char[] ch = new char[200];
        if (!in.ready()) {
            return null;
        }
        int len = in.read(ch);
        for (int i = 0; i < len; i++) {
            w = ch[i];
            body.append(w);
        }
        return body.toString();
    }

    private static boolean ifContentTypeIsXWwwFormUrlencoded(String[] lines) {
        for (String result : lines) {
            String[] header = result.split(":");
            if (header[0].equals("Content-Type") && header[1].equals(" application/x-www-form-urlencoded")) {
                return true;
            }
        }
        return false;
    }

    private static String[] headersParse(BufferedReader in) throws IOException {
        String[] result;
        StringBuilder requestHeaders = new StringBuilder();
        String s;
        while (!(s = in.readLine()).equals("")) {
            requestHeaders.append(s).append("\n");
        }
        return result = requestHeaders.toString().split("\n");
    }
}
