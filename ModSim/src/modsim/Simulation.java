
package modsim;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * 19/09/2014 - viniciusfacco
 *              Criada primeira versão do simulador.
 *              Modelo baseado no SLIDE 202 do segundo arquivo do professor.
 *              Variáveis seguindo modelo do SLIDE 35 do segundo arquivo do professor.
 *              Alguns dos comentários iniiam com um número seguido de um "." para fazer referência ao passo do modelo do SLIDE 202 que está sendo executado.
 */
public class Simulation {
    
    //System State
    private boolean serverBusy;                 //Server status
    private int queueSize;                      //Number in queue
    private ArrayList<Float> arrivalTimes;      //Times of arrival
    private ArrayList[] queues;                 //Todas as filas do sistema
    private float lastEventTime;                //Time of last event
    //Statistical counters
    private int delayedNumber;                  //Number Delayed
    private float totalDelay;                   //Total delay
    private float queueTotalTime;                //Area under Q(t)
    private float serverBusyTime;               //Area under B(t)
    //Controls
    private float clock;                        //Clock
    private float[] eventList;                  //Event List
    private Random tipo_serviço;          //gerador de números aleatórios para definir a o tipo dos serviços que estão chegando

    public Simulation(int quantidade_filas){
        //vamos utilizar aqui futuramente para a entrada de parâmetros externos
        queues = new ArrayList[quantidade_filas];
        tipo_serviço = new Random();
    }
    
    //Programa principal
    public void mula(){
        init();                            //0.
        while(continuaMula()){             //enquant simulação não terminou
            switch (future()) {            //1. avançamos o tempo até o próximo evento
                case 'A':                  //2. se o evento atual é uma chegada
                    arrivalEvent();        //2. tratamos o evento de chegada
                    break;
                case 'D':                  //2. se o evento atual é uma partida
                    departureEvent();      //2. tratamos o evento de partida
            }
        }
        reportGenerator();
    }
    
    //Rotina de inicialização
    private void init(){
        clock = 0;                          //1.
        serverBusy = false;                 //2.
        queueSize = 0;                      //2.
        arrivalTimes = new ArrayList();     //2.
        lastEventTime = 0;                  //2.
        delayedNumber = 0;                  //2.
        totalDelay = 0;                     //2.
        queueTotalTime = 0;                 //2.
        serverBusyTime = 0;                 //2.
        eventList = new float[2];           //3.
        eventList[0] = arrivalScheduler();  //3.
        eventList[1] = 10000000;            //3.
    }
    
    //Rotina de gestão de tempo
    private char future(){
        if (eventList[0] < eventList[1]){   //1. se o próximo evento for uma chegada
            clock = eventList[0];           //2. então avançamos o relógio para esse tempo
            return 'A';                     //1. e informamos que o evento nesse tempo é uma chegada
        } else {                            //1. então é porque o próximo evento é uma partida
            clock = eventList[1];           //2. então avançamos o relógio para esse tempo
            return 'D';                     //1. e informamos que o evento nesse tempo é uma partida
        }
    }
    
    //Rotina do evento de chegada
    private void arrivalEvent(){//PENDENTE
        //implementar fluxo do slide 50
    }
    
    //Rotina do evento de partida
    private void departureEvent(){//PENDENTE
        //implementar fluxo do slide 51
    }
    
    //retorna um novo tempo de chegada
    private float arrivalScheduler(){//PENDENTE
        //aqui vamos utilizar uma função de distribuição para gerar os tempos de chegada com parâmetros baseados nos dados do sistema real
        float time;
        int tipo = tipo_serviço.nextInt(100) + 1;
        switch (tipo){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
        }
        return 0;
    }
    
    //retorna um novo tempo de partida
    private float departureScheduler(){//PENDENTE
        //aqui vamos utilizar uma função de distribuição para gerar os tempos de atendimento com parâmetros baseados nos dados do sistema real
        return 0;
    }
    
    //retorna se chegamos ao fim da simulação ou não
    private boolean continuaMula(){//PENDENTE
        //aqui vamos implementar o código com a condição de parada
        return true;
    }
    
    public void reportGenerator(){
        //aqui vamos implementar o código que cálcula as medidas de desempenho e apresenta para o usuário
    }
}
