/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.iespuertodelacruz.ymp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yared
 */
public class Colaborar {

    public static void main(String[] args) {
        
        int cantidadTotal = 10;
        String filePath = "/tmp/fichero.txt";
        Lenguaje lenguaje;
        int num = 0;
        
        while(cantidadTotal <=100){
            
            lenguaje = new Lenguaje(filePath, cantidadTotal, num);
            lenguaje.start();
            num +=1;
            cantidadTotal += 10;
            
        }
        
    }
    
    
}



