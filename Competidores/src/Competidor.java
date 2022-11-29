import java.io.*;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Competidor {

    public static void main(String args[]) {

        File archivo;
        RandomAccessFile raf;
        FileLock bloqueo;
        int valor = 0;
        int dato = 0;
        String uuid = "";
        String linea = "";
        List<String> puntuacion = new ArrayList<>();
        try {

            archivo = new File("/tmp/buffer.txt");
            PrintStream ps=new PrintStream(new BufferedOutputStream(new
                    FileOutputStream(new File("/tmp/logGanador.txt"),true)),true);
            System.setOut(ps);
            System.setErr(ps);
            raf = new RandomAccessFile(archivo, "rwd");
            while (valor < 50) {
                bloqueo = raf.getChannel().lock();
                if (raf.length() != 0) {
                    raf.seek(0);
                    linea = raf.readUTF();
                    raf.setLength(0);
                    String[] split = linea.split(" ");
                    uuid = split[0];
                    dato = Integer.parseInt(split[1]);
                    valor += dato;
                    puntuacion.add(new String(uuid + " " + dato));
                } else {

                }
                bloqueo.release();
                sleep(500);
            }

            raf.close();

            for (String puntos: puntuacion
                 ) {
                System.out.println(puntos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
