/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.iespuertodelacruz.ymp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 *
 * @author yared
 */
public class Lenguaje extends Thread{

    private int cantidad;
    private String filePath;
    int num;

    public Lenguaje(String filePath, int cantidad, int num) {

        this.num =num;
        this.cantidad = cantidad;
        this.filePath = filePath;

    }

    @Override
    public void run() {
        int j = 0;
        while (j < cantidad) {
            
             String palabras = "";
                palabras += generarPalabra() + "\n";
            escribirArchivo(filePath, palabras);
            j += 1;
            
        }
        System.out.println("Hilo terminado: " + num);

    }
    
    public static String generarPalabra() {

        Random rnd = new Random();
        String palabra = "";
        int tamanio = rnd.nextInt(10) + 1;

        for (int i = 0; i <= tamanio; i++) {
            palabra += (char) (rnd.nextInt(26) + 97);

        }

        return palabra;

    }

    public static void escribirArchivo(String path, String texto) {

        File archivo = new File(path);

        try ( BufferedWriter bw = Files.newBufferedWriter(archivo.toPath(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        );) {

            bw.write(texto);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}



        
    
    
    
    

