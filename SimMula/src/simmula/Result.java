/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simmula;

/**
 *
 * @author Vinicius
 */
public class Result {
    
    ReportInterface reportScreen;
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
     int totalClientsOut = 0;
     int totalClientsIn = 0;
     int totalClientsQueue = 0;
     int totalClients = 0;
     float[][] serverBusyTime; 
    
    public Result(int servers){
        serverBusyTime = new float[servers][4];
        for (int i = 0; i < servers; i++){
            serverBusyTime[i][0] = 0;
            serverBusyTime[i][1] = 0;
            serverBusyTime[i][2] = 0;
            serverBusyTime[i][3] = 0;
        }
    }
    
    public void setTotalClientsOut(int value){
        this.totalClientsOut += value;
    }
    
    public void setTotalClientsIn(int value){
        this.totalClientsIn += value;
    }
    
    public void setTotalClientsQueue(int value){
        this.totalClientsQueue += value;
    }
    
    public void setTotalClients(int value){
        this.totalClients += value;
    }
    
    public void setAttendRatio(float value){
        this.attendRatio += value;
    }
    
    public void setAvgQueueTime(float value){
        this.avgQueueTime += value;
    }
    
    public void setAvgServiceTime(float value){
        this.avgServiceTime += value;
    }
    
    public void setAvgSystemTime(float value){
        this.avgSystemTime += value;
    }
    
    public void setAvgQueueTimeSLA(float value){
        this.avgQueueTimeSLA += value;
    }
    
    public void setCountClients1HourQueue(float value){
        this.countClients1HourQueue += value;
    }
    
    public void setPctClients1HourQueue(float value){
        this.pctClients1HourQueue += value;
    }
    
    public void setCountClientsNextDayAttend(float value){
        this.countClientsNextDayAttend += value;
    }
    
    public void setPctClientsNextDayAttend(float value){
        this.pctClientsNextDayAttend += value;
    }
    
    public void setCountAttendTime20(float value){
        this.countAttendTime20 += value;
    }
    
    public void setPctAttendTime20(float value){
        this.pctAttendTime20 += value;
    }
    
    public void setCountSLAQueueTime30(float value){
        this.countSLAQueueTime30 += value;
    }
    
    public void setPctSLAQueueTime30(float value){
        this.pctSLAQueueTime30 += value;
    }
    
    public void setServerBusyTime(float[][] value){
        for (int i = 0; i < value.length; i++){
            this.serverBusyTime[i][0] += value[i][0];
            this.serverBusyTime[i][1] += value[i][1];
            this.serverBusyTime[i][2] += value[i][2];
            this.serverBusyTime[i][3] += value[i][3];
        }
    }
    
    public void setCountSLA(float value){
        this.countSLA += value;
    }
    
    public void setPctSLA(float value){
        this.pctSLA += value;
    }  
    
    public void update(int count){
        avgQueueTime /= count;
        avgQueueTimeSLA /= count;
        avgServiceTime /= count;
        avgSystemTime /= count;
        countSLA /= count;
        pctSLA /= count;
        attendRatio /= count;
        countClients1HourQueue /= count;
        pctClients1HourQueue /= count;
        countClientsNextDayAttend /= count;
        pctClientsNextDayAttend /= count;
        countAttendTime20 /= count;
        pctAttendTime20 /= count;
        countSLAQueueTime30 /= count;
        pctSLAQueueTime30 /= count;
        totalClientsOut /= count;
        totalClientsIn /= count;
        totalClientsQueue /= count;
        totalClients /= count;
        for (int i = 0; i < serverBusyTime.length; i++){
            serverBusyTime[i][0] /= count;
            serverBusyTime[i][1] /= count;
            serverBusyTime[i][2] /= count;
            serverBusyTime[i][3] /= count;
        }
    }
    
    public void report(){
        reportScreen = new ReportInterface();
        reportScreen.setTitle("Final");
        reportScreen.appendText("\nEstatísticas");
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
        reportScreen.appendText("\n");
        for (int i = 0; i < serverBusyTime.length; i++){
            reportScreen.appendText("Servidor " + i + ":");
            reportScreen.appendText("\tAtendimentos: " + serverBusyTime[i][0]);
            reportScreen.appendText("\tTempo médio de atendimento: " + serverBusyTime[i][1]);
            reportScreen.appendText("\tTempo ocupado: " + serverBusyTime[i][2]);
            reportScreen.appendText("\tTaxa de ocupação: " + serverBusyTime[i][3] * 100 + "%");
        }
    }
    
}
