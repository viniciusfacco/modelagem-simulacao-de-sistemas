/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modsim;

import java.util.Random;

/**
 *
 * @author Vinicius
 */
public class ModSim {

    /**
     * @param args the command line arguments
     */
    
    private static Random tipo_serviço;          //gerador de números aleatórios para definir a o tipo dos serviços que estão chegando
    
    public static void main(String[] args) {
        // TODO code application logic here
        int tipo = 0;
        tipo_serviço = new Random();
        for (int i = 0; i < 10; i++){
            tipo = tipo_serviço.nextInt(100);
            System.out.println(tipo + " - " + tipo/10);
        }
    }
    
}
