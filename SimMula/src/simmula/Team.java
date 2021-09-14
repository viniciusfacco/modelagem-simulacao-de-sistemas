
package simmula;

import java.util.Random;

/**
 * Team
 * Classe que representa uma equipe
 */
public class Team {
    
    private int[][] servers;            //matriz de servidores onde cada linha possui a experiencia do servidor e a sua ocupação. o tamanho da matriz corresponde a quantidade de servidores
    private int amount_servers;         //quantidade total de servers da equipe
    private Random serverid;            //gerador de variáveis aleatórias para gerar id para escolher um servidor que irá atender o cliente que está entrando
    private int avaiable_servers;       //contador de servidores disponíveis
    
    public Team(int size){
        servers = new int[size][2];     //cria a matriz de servidores
        amount_servers = 0;
        avaiable_servers = 0;
        serverid = new Random();
    }
    
    //método que adiciona servidor
    public void addServer(int experience){      // experiencia e posição do servidor na matriz
        servers[amount_servers][0] = experience;// ao criar servidor defino sua experiência
        servers[amount_servers][1] = 0;         // e coloco 0 em sua ocupação, representando desocupado
        amount_servers++;                       //incrementamos a quantidade de servidores
        avaiable_servers++;                     //incrementamos os servidores disponíveis pois ganhamos mais um
    }
    
    //método que escolhe um servidor para atender e retorna seu ID
    public int getServer() {
        boolean selected = false;
        int selected_server = 0;
        while (!selected){                                      //enquanto não selecionarmos um servidor livre
            selected_server = serverid.nextInt(amount_servers); //geramos uma posição aleatória no array de servidores
            if (servers[selected_server][1] == 0){              //se o servidor dessa posição estiver livre, ele é o escolhido
                servers[selected_server][1] = 1;                    //definimos o servidor como ocupado
                selected = true;                                    //encontramos :)
            }
        }
        avaiable_servers--;                                     //decrementamos contador de servidores disponíveis
        return selected_server;                                 //retornamos o ID do servidor selecionado
    }

    //método que retorna a experiência do server
    public int getServerXP(int server) {
        return servers[server][0];
    }

    //método para liberar o servidor server
    public void setServerFree(int server) {
        servers[server][1] = 0;
        avaiable_servers++;     //incrementamos contador de servidores disponíveis
    }
    
    //método para retornar se existem servidores disponíveis
    public boolean isAnyServer(){
        return avaiable_servers > 0;
    }

    public int getTotalServers() {
        return amount_servers;
    }
    
}
