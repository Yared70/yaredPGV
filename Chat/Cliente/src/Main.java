import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        try {

            String dirIp = (args.length >= 1) ? args[0] : "localhost";
            Integer port = (args.length == 2) ? Integer.parseInt(args[1]) : 6667;

            Socket socket = new Socket(dirIp, port);

            BufferedReader readerServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader readerTerminal = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);

            HiloLector hiloLector = new HiloLector(readerServidor, salidaServidor);
            hiloLector.start();
            Stream<String> linesTerminal = readerTerminal.lines();
            linesTerminal.forEach(linea -> {

                if (linea.startsWith("/ping") && !linea.startsWith("/ping on") && !linea.startsWith("/ping off")) {

                    String uuid = generarUuid();
                    salidaServidor.println(linea + " " + uuid);
                    hiloLector.tiempoInicial = System.currentTimeMillis();


                } else if (linea.startsWith("/quit")) {

                    salidaServidor.println(linea);
                    try {
                        readerTerminal.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {

                    salidaServidor.println(linea);

                }
            });


        }catch(Exception ex){
        }
    }


    public static String generarUuid(){

        String characters = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String uuid = "";
        Random rnd = new Random();

        String uuidPart1 = "";
        String uuidPart2 = "";

        while(uuidPart1.length() < 8){

            uuidPart1 += characters.charAt(rnd.nextInt((characters.length()-1)+1));

        }

        uuid += uuidPart1 + "-";
        uuidPart1 = "";

        for (int i = 0; i < 3; i++) {

            while(uuidPart2.length() < 4){

                uuidPart2 += characters.charAt(rnd.nextInt((characters.length()-1)+1));

            }

            uuid += uuidPart2 + "-";
            uuidPart2 = "";

        }

        while(uuidPart1.length() < 8){

            uuidPart1 += characters.charAt(rnd.nextInt((characters.length()-1)+1));

        }

        uuid += uuidPart1;
        uuidPart1 = "";

        return  uuid;

    }

}

class HiloLector extends Thread {

    private BufferedReader readerServidor;
    PrintWriter printServidor;

    Long tiempoInicial;
    Long tiempoFinal;

    public HiloLector(BufferedReader readerServidor, PrintWriter printServidor) throws IOException {

        this.readerServidor = readerServidor;
        this.printServidor = printServidor;

    }

    @Override
    public void run() {

        try{

            Stream<String> lines = readerServidor.lines();
            lines.forEach( linea -> {

                if (linea.startsWith("/ping")) {

                    String[] lineaSplitSpace = linea.split(" ");
                    String emisor = lineaSplitSpace[1];
                    String uuid = lineaSplitSpace[2];
                    pr rintln("/pong " + emisor + " " + uuid);

                }else if( linea.startsWith("/pong")){

                    tiempoFinal = System.currentTimeMillis();

                    Long tiempoTotal = (tiempoFinal - tiempoInicial);

                    String respuesta = linea + " tiempo de vuelo: " + tiempoTotal + " ms";
                    System.out.println(respuesta);
                }else {

                    System.out.println(linea);

                }

            });



        }catch(Exception ex){

        }



    }


}