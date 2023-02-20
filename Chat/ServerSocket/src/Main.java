import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String dirIp = "";
        dirIp = (args.length >= 1) ? args[0] : "localhost";
        Integer port = (args.length == 2) ? Integer.parseInt(args[1]) : 6667;

        Server server = new Server(dirIp, port);
        server.ejecutar();

    }
}

