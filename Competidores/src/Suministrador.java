import java.io.*;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Suministrador {

    public static void main(String[] args) {

        File archivo;
        RandomAccessFile raf;
        FileLock bloqueo;
        List<String> listaUuid = new ArrayList<>();
        List<Integer> listaNumeros = new ArrayList<>();
        Random rnd = new Random();
        boolean hayGanador = false;


        try{
            archivo = new File("/tmp/buffer.txt");
            PrintStream ps=new PrintStream(new BufferedOutputStream(new
                    FileOutputStream(new File("/tmp/logSuministrador.txt"),true)),true);
            System.setOut(ps);
            System.setErr(ps);
            raf = new RandomAccessFile(archivo, "rwd");
            while(!hayGanador){
            //for (int i = 0; i < 10; i++) {


                bloqueo = raf.getChannel().lock();
                if(raf.length() == 0){

                    raf.seek(0);
                    String uuid = generarUuid();
                    listaUuid.add(uuid);
                    int num = (rnd.nextInt(5)+1);
                    listaNumeros.add(num);
                    raf.writeUTF(uuid + " " + num);
                    System.out.println("Suministrador: Valor escrito: " + uuid + " " + num);

                }else{



                }


                bloqueo.release();
                sleep(500);

            }
            raf.close();


        }catch (Exception ex){
            System.out.println(ex.getMessage());
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
