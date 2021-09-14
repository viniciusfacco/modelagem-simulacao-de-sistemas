/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simmula;

import java.util.*;


/**
 *
 * @author Dell
 */
public class Report {
    
    ArrayList<Client> clients_out;
    ArrayList<Client> clients_in;
    ArrayList<Client> queues;
    ReportInterface reportScreen;
    float clock;
    
    float avgQueueTime = 0;         // tempo médio de fila
    float avgQueueTimeSLA = 0;      // tempo médio de fila para os clientes SLA
    float avgServiceTime = 0;       // tempo médio de atendimento
    float avgSystemTime = 0;        // tempo médio os clientes ficaram no sistema
    float countSLA = 0;             // número de clientes SLA
    float pctSLA = 0;               // percentual de clientes SLA, em relação a todos clientes que foram atendidos
    float attendRatio = 0;          // taxa de atendimento
    float countClients1HourQueue = 0; // número de clientes que ficaram mais de uma hora na fila
    float pctClients1HourQueue = 0; // percentual de clientes que ficaram mais de uma hora na fila
    float countClientsNextDayAttend = 0; // número de clientes não atendidos no mesmo dia
    float pctClientsNextDayAttend = 0; // percentual de clientes não atendidos no mesmo dia
    float countAttendTime20 = 0;    // número de atendimentos que levaram mais de 20 minutos
    float pctAttendTime20 = 0;      // percentual de atendimentos que levaram mais de 20 minutos
    float countSLAQueueTime30 = 0; // número de chamados SLA com mais de 30 minutos de fila
    float pctSLAQueueTime30 = 0;   // percentual de chamados SLA com mais de 30 minutos de fila (em relação aos outros SLA)
    HashMap<Integer, ReportServer> servers = new HashMap <Integer, ReportServer>();
    private int totalClientsOut;
    private int totalClientsIn;
    private int totalClientsQueue;
    private int totalClients;
    private float[][] serverBusyTime;

    public Report(ArrayList<Client> clients_out, ArrayList<Client>[] clients_in, ArrayList<Client>[] queues, float clock, int servers){
        this.clients_out = clients_out;
        this.clock = clock;
        //reportScreen = new ReportInterface();
        //reportScreen.setTitle("Parcial");
        ajustData(clients_in, queues);
        serverBusyTime = new float[servers][4];
    }
    
    //método para remover todos os clientes dos arrays de fila e de clientes em atendimento e colocá-los nas listas corrematente
    private void ajustData(ArrayList<Client>[] clients_in, ArrayList<Client>[] queues) {
        this.queues = new ArrayList<>();//lista de clientes que ficaram na fila ao fim da simulação
        for (int i = 0; i < queues.length; i++){            //na fila de cada produto
            for (int j = 0; j < queues[i].size(); j++){         //cada produto da fila
                this.queues.add(queues[i].get(j));                //coloco na lista de produtos que estão na fila ao fim da simulação
            }
        }
        this.clients_in = new ArrayList<>();//lista de clientes que ficaram em atendimento
        for (int i = 0; i < clients_in.length; i++){            //na lista de clientes sendo atendidos
            for (int j = 0; j < clients_in[i].size(); j++){         //cada cliente em atendimento
                if (clients_in[i].get(j).getIniAttendTime() < clock){//se o atendimento começou antes do fim da simulação
                    this.clients_in.add(clients_in[i].get(j));              //coloco na lista de clientes em atendimento
                }
            }
        }
    }
    
    public void run(){
        
        //reportScreen.appendText("<<<<<Clientes>>>>>");
        totalClientsOut = clients_out.size();
        //reportScreen.appendText("Clientes que deixaram o sistema: " + totalClientsOut);
        for (int i = 0; i < totalClientsOut; i++){ //contabilização dos clientes que deixaram o sistema
            Client client = clients_out.get(i);

            /*reportScreen.appendText("Cliente " + client.getID() + 
                    " | Produto : " + client.getType() +
                    " | SLA : " +  (client.isSLA() ? "Sim" : "Não") + 
                    " | Chegada : " +  client.getArriveTime() + 
                    " | Partida : " + client.getDepartureTime() + 
                    " | Tempo de fila: " + client.getQueueTime()+ 
                    " | Tempo Inicio Atendimento: " + client.getIniAttendTime() +
                    " | Tempo de atendimento: " + client.getServiceTime() + 
                    " | Tempo de sistema: " + client.getSystemTime() + 
                    " | Servidor: " + client.getServer());*/
            
            avgQueueTime += client.getQueueTime();
            avgServiceTime += client.getServiceTime();
            avgSystemTime += client.getSystemTime();
            
            // números referentes a clientes SLA
            if (client.isSLA()){
                countSLA++;
                avgQueueTimeSLA += client.getQueueTime();
                
                // número de chamados SLA com mais de 30 minutos de fila
                if (client.getQueueTime() > 30){
                    countSLAQueueTime30++;
                }
            }
            
            // número de clientes que ficaram mais de uma hora na fila
            if (client.getQueueTime() > 60){
                countClients1HourQueue++;
            }
            
            // número de clientes não atendidos no mesmo dia
            //if (client.getDayDeparture() > client.getDayArrival()){
            //    countClientsNextDayAttend++;
            //}
            
            // número de atendimentos que levaram mais de 20 minutos
            if (client.getServiceTime() > 20){
                countAttendTime20++;
            }
            
            if (servers.containsKey(client.getServer())){
                servers.get(client.getServer()).addClient(client);
            } else {
                ReportServer server = new ReportServer(client.getServer());
                server.addClient(client);
                servers.put(client.getServer(), server);
            }
        }
        
        totalClientsIn = clients_in.size();
        //reportScreen.appendText("Clientes que ficaram em atendimento: " + totalClientsIn);
        for (int i = 0; i < totalClientsIn; i++){ //contabilização dos clientes que estão no sistema
            Client client = clients_in.get(i);
            
           /*reportScreen.appendText("Cliente " + client.getID() + 
                    " | Produto : " + client.getType() +
                    " | SLA : " +  (client.isSLA() ? "Sim" : "Não") + 
                    " | Chegada : " +  client.getArriveTime() + 
                    " | Partida : " + client.getDepartureTime() + 
                    " | Tempo de fila: " + client.getQueueTime()+ 
                    " | Tempo Inicio Atendimento: " + client.getIniAttendTime() +
                    " | Tempo de atendimento: " + client.getServiceTime() + 
                    " | Tempo de sistema: " + client.getSystemTime() + 
                    " | Servidor: " + client.getServer());*/
            
            avgQueueTime += client.getQueueTime();
            //avgServiceTime += client.getServiceTime();
            //avgSystemTime += client.getSystemTime();
            
            // números referentes a clientes SLA
            if (client.isSLA()){
                countSLA++;
                avgQueueTimeSLA += client.getQueueTime();
                
                // número de chamados SLA com mais de 30 minutos de fila
                if (client.getQueueTime() > 30){
                    countSLAQueueTime30++;
                }
            }
            
            // número de clientes que ficaram mais de uma hora na fila
            if (client.getQueueTime() > 60){
                countClients1HourQueue++;
            }
            
            // número de clientes não atendidos no mesmo dia
            countClientsNextDayAttend++;
            
            // número de atendimentos que levaram mais de 20 minutos
            if (client.getServiceTime() > 20){
                countAttendTime20++;
            }
            
        }
        
        totalClientsQueue = queues.size();
        //reportScreen.appendText("Clientes que ficaram na fila: " + totalClientsQueue);        
        for (int i = 0; i < totalClientsQueue; i++){ //contabilização dos clientes que estão na fila
            Client client = queues.get(i);
            
            /*reportScreen.appendText("Cliente " + client.getID() + 
                    " | Produto : " + client.getType() +
                    " | SLA : " +  (client.isSLA() ? "Sim" : "Não") + 
                    " | Chegada : " +  client.getArriveTime() + 
                    " | Partida : " + client.getDepartureTime() + 
                    " | Tempo de fila: " + client.getQueueTime()+ 
                    " | Tempo Inicio Atendimento: " + client.getIniAttendTime() +
                    " | Tempo de atendimento: " + client.getServiceTime() + 
                    " | Tempo de sistema: " + client.getSystemTime() + 
                    " | Servidor: " + client.getServer());*/
            
            avgQueueTime += clock - client.getArriveTime();
            //avgServiceTime += client.getServiceTime();
            //avgSystemTime += clock - client.getArriveTime();
            
            // números referentes a clientes SLA
            if (client.isSLA()){
                countSLA++;
                avgQueueTimeSLA += clock - client.getArriveTime();
                
                // número de chamados SLA com mais de 30 minutos de fila
                if ((clock - client.getArriveTime()) > 30){
                    countSLAQueueTime30++;
                }
            }
            
            // número de clientes que ficaram mais de uma hora na fila
            if ((clock - client.getArriveTime()) > 60){
                countClients1HourQueue++;
            }
            
            // número de clientes não atendidos no mesmo dia
            countClientsNextDayAttend++;
            
            // número de atendimentos que levaram mais de 20 minutos
            if ((clock - client.getArriveTime()) > 20){
                countAttendTime20++;
            }
            
        }
            
        totalClients = totalClientsOut + totalClientsIn + totalClientsQueue;
        avgQueueTime /= totalClients;
        avgServiceTime /= totalClientsOut;
        avgSystemTime /= totalClientsOut;
        pctSLA = (countSLA / totalClients) * 100;
        attendRatio = totalClientsOut / (clients_out.get(totalClientsOut - 1).getDepartureTime() / 60);
        pctClients1HourQueue = (countClients1HourQueue / totalClients) * 100;
        pctClientsNextDayAttend = (countClientsNextDayAttend / totalClients) * 100;
        pctAttendTime20 = (countAttendTime20 / totalClients) * 100;
        if (countSLA == 0) {
            pctSLAQueueTime30 = 0;
            avgQueueTimeSLA = 0;
        } else {
            pctSLAQueueTime30 = (countSLAQueueTime30 / countSLA) * 100;
            avgQueueTimeSLA /= countSLA;
        }
        
        //System.out.println("Total de clientes que entraram no sistema: " + clients_in.size());
        /*reportScreen.appendText("\nEstatísticas");
        reportScreen.appendText("Total de clientes que saíram do sistema: " + totalClientsOut);
        reportScreen.appendText("Total de clientes que estão em atendimento: " + totalClientsIn);
        reportScreen.appendText("Total de clientes que estão na fila: " + totalClientsQueue);
        reportScreen.appendText("Total de clientes que entraram no sistema: " + totalClients);
        reportScreen.appendText("\n");
        reportScreen.appendText("Taxa de atendimento (por hora): " + attendRatio);
        reportScreen.appendText("Tempo médio de fila: " + avgQueueTime);
        reportScreen.appendText("Tempo médio de atendimento: " + avgServiceTime);
        reportScreen.appendText("Tempo médio de sistema: " + avgSystemTime);
        reportScreen.appendText("Número de chamados SLA: " + countSLA + " (" + pctSLA + "%)");
        reportScreen.appendText("Tempo médio de fila para os SLA: " + avgQueueTimeSLA);
        reportScreen.appendText("\n");
        reportScreen.appendText("Nº de clientes que ficaram mais de uma hora na fila: " + countClients1HourQueue + " (" + pctClients1HourQueue + "%)");
        reportScreen.appendText("Nº de clientes não atendidos no mesmo dia: " + countClientsNextDayAttend + " (" + pctClientsNextDayAttend + "%)");
        reportScreen.appendText("Nº de atendimentos com mais de 20 minutos: " + countAttendTime20 + " (" + pctAttendTime20 + "%)");
        reportScreen.appendText("Nº de chamados SLA com mais de 30 minutos na fila: " + countSLAQueueTime30 + " (" + pctSLAQueueTime30 + "%)");
        reportScreen.appendText("\n");*/
        Iterator it = servers.entrySet().iterator();
        int counterTeam = 0;
        while (it.hasNext()) {
            Map.Entry serverPair = (Map.Entry)it.next();
            //reportScreen.appendText("Servidor " + serverPair.getKey() + ":");
            
            ReportServer server = (ReportServer)serverPair.getValue();            
            //reportScreen.appendText("\tAtendimentos: " + server.getClients().size());
            serverBusyTime[counterTeam][0] = server.getClients().size();
            //reportScreen.appendText("\tTempo médio de atendimento: " + server.getAverageServiceTime());
            serverBusyTime[counterTeam][1] = server.getAverageServiceTime();
            
            //obter tempos de clientes que estão em atendimento
            float serviceIn = 0;
            for (int i = 0; i < clients_in.size(); i++){
                if (clients_in.get(i).getServer() == server.getID()){
                    serviceIn += clock - clients_in.get(i).getIniAttendTime();
                }
            }
            
            float serviceAll = server.getBusyTime() + serviceIn;
            //reportScreen.appendText("\tTempo ocupado: " + server.getBusyTime() + " + " + serviceIn + " = " + serviceAll);
            serverBusyTime[counterTeam][2] = serviceAll;
            //reportScreen.appendText("\tTaxa de ocupação: " + ((server.getBusyTime() + serviceIn) / clock) * 100 + "%");
            serverBusyTime[counterTeam][3] = (server.getBusyTime() + serviceIn) / clock;
            
            counterTeam++;
            it.remove(); // avoids a ConcurrentModificationException
        }
        
    }
    
    public int getTotalClientsOut(){
        return this.totalClientsOut;
    }
    
    public int getTotalClientsIn(){
        return this.totalClientsIn;
    }
    
    public int getTotalClientsQueue(){
        return this.totalClientsQueue;
    }
    
    public int getTotalClients(){
        return this.totalClients;
    }
    
    public float getAttendRatio(){
        return this.attendRatio;
    }
    
    public float getAvgQueueTime(){
        return this.avgQueueTime;
    }
    
    public float getAvgServiceTime(){
        return this.avgServiceTime;
    }
    
    public float getAvgSystemTime(){
        return this.avgSystemTime;
    }
    
    public float getAvgQueueTimeSLA(){
        return this.avgQueueTimeSLA;
    }
    
    public float getCountClients1HourQueue(){
        return this.countClients1HourQueue;
    }
    
    public float getPctClients1HourQueue(){
        return this.pctClients1HourQueue;
    }
    
    public float getCountClientsNextDayAttend(){
        return this.countClientsNextDayAttend;
    }
    
    public float getPctClientsNextDayAttend(){
        return this.pctClientsNextDayAttend;
    }
    
    public float getCountAttendTime20(){
        return this.countAttendTime20;
    }
    
    public float getPctAttendTime20(){
        return this.pctAttendTime20;
    }
    
    public float getCountSLAQueueTime30(){
        return this.countSLAQueueTime30;
    }
    
    public float getPctSLAQueueTime30(){
        return this.pctSLAQueueTime30;
    }
    
    public float[][] getServerBusyTime(){
        return this.serverBusyTime;
    }
    
    public float getCountSLA(){
        return this.countSLA;
    }
    
    public float getPctSLA(){
        return this.pctSLA;
    }  
    
}
