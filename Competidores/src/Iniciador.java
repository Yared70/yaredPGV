import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Iniciador {

    public static void main(String args[]){

        Process process;
        Process suministrador;
        boolean terminado = false;

        List<Process> competidores = new ArrayList<>();
        try {
            suministrador = Runtime.getRuntime().exec("java Suministrador");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 10; i++) {

            try {
                process = Runtime.getRuntime().exec("java Competidor");
                competidores.add(process);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        while(!terminado){

            for (Process proceso: competidores) {
                if(!proceso.isAlive()){
                    terminado = true;
                    System.out.println("El proceso ganador es el: " + proceso.pid());
                }
            }

        }

        if(terminado){

            for (Process proceso: competidores) {
               proceso.destroy();
            }
            suministrador.destroy();

        }

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Process proceso: competidores) {
            proceso.destroy();
        }


    }

}
