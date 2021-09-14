/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simmula;

import java.util.ArrayList;

/**
 *
 * @author Dell
 */
public class ReportServer {
    int id_server;
    public ArrayList<Client> clients;
    
    public ReportServer(int id_server){
        this.id_server = id_server;
        clients = new ArrayList<Client>();
    }
    
    public void addClient(Client client){
        clients.add(client);
    }
    
    public ArrayList<Client> getClients(){
        return clients;
    }
    
    public float getAverageServiceTime(){
        float avgServiceTime = 0;
        for (int i = 0; i < clients.size(); i++){
            avgServiceTime += clients.get(i).getServiceTime();
        }
        return avgServiceTime / clients.size();
    }
    
    public float getBusyTime(){
        float busyTime = 0;
        for (int i = 0; i < clients.size(); i++){
            busyTime += clients.get(i).getServiceTime();
        }
        return busyTime;
    }
    
    public int getID(){
        return this.id_server;
    }
}
