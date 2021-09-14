
package simmula;

/**
 * Client
 * Classe que representa um cliente
 */
public class Client {
    
    private int ID;                 //numero de identificação do cliente
    private int type;               //tipo de produto do cliente
    private float arrive_time;      //tempo do relógio que o cliente chegou no sistema
    private float ini_attend_time;  //tempo que atendimento do cliente foi iniciado
    private float departure_time;   //tempo do relógio que o cliente saiu do sistema
    private float service_time;     //tempo de duração do atendimento do cliente
    private boolean SLA;            //cliente tem ou não prioridade no atendimento
    private int id_team;            //id da equipe que atendeu este cliente
    private int id_server;          //id do servidor que atendeu/está atendendo este cliente

    public Client(){
        
    }
    
    public void setID(int idclient){
        ID = idclient;
    }
    
    public void setTeam(int idteam){
        id_team = idteam;
    }
    
    public void setType(int typeclient){
        type = typeclient;
    }
    
    public void setArriveTime(float time){
        arrive_time = time;
    }
    
    public void setIniAttendTime(float time){
        ini_attend_time = time;
        departure_time = ini_attend_time + service_time;
    }
       
    public void setServiceTime(float time){
        service_time = time;
    }
    
    public void setSLA(boolean sla){
        SLA = sla;
    }
    
    public void setServer(int server){
        id_server = server;
    }
    
    public int getID(){
        return ID;
    }
    
    public int getType(){
        return type;
    }
    
    public float getArriveTime(){
        return arrive_time;
    }
    
    public float getIniAttendTime(){
        return ini_attend_time;
    }
    
    public float getDepartureTime(){
        return departure_time;
    }
    
    public float getServiceTime(){
        return service_time;
    }
    
    public boolean isSLA(){
        return SLA;
    }
    
    public int getServer(){
        return id_server;
    }
    
    public int getTeam(){
        return id_team;
    }
    
    public float getQueueTime(){
        return ini_attend_time - arrive_time;
    }
    
    public float getSystemTime(){
        return departure_time - arrive_time;
    }
    
    public float getDayArrival(){
        // considerando um dia como 570 minutos
        return arrive_time % 570;
    }
    
    public float getDayDeparture(){
        // considerando um dia como 570 minutos
        return departure_time % 570;
    }
    
}
