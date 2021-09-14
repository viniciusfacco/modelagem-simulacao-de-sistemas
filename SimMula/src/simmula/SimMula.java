
package simmula;

import distributionfunction.Functions;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * SimMula
 * Classe principal que realizará todo o processo de simulação
 */
public class SimMula implements Runnable{
    
    private Team[] teams;                   //array que conterá todas as equipes    
    private ArrayList<Client>[] queues;     //filas de cada uma das equipes (última posição é a fila geral)
    private ArrayList<Client>[] clients_in; //array com listas dos clientes sendo atendidos por cada equipe
    private ArrayList<Client> clients_out;  //lista com todos que já deixaram o sistema
    private int[] team_of_product;          //array com os produtos as equipes que podem atender esse produto...cada posição do array corresponde a um produto e o valor corresponde a equipe. se o valor for menor que 0 qualquer equipe pode atender esse produto
    private Functions functions;            //objeto gerador de tempos de atendimento e chegada
    private float clock;                    //relógio do sistema
    private Random service_type_generator;  //gerador de números aleatórios para definir a o tipo dos serviços que estão chegando
    private float next_arrival;             //tempo que ocorrerá a próxima chegada
    private float next_departure;           //tempo que ocorrerá a próxima partida
    private float[] service_occurrence_rate;//array contendo a taxa de proporção de ocorrência de cada produto. o índice do array corresponde ao tipo de produto
    private float time_to_end;              //tempo de parada da simulação
    private float SLA_probability;          //valor de probabilidade de um cliente ser SLA. (valor entre 0 e 1)
    private int initial_queue;              //tamanho inicial da fila ao iniciar o sistema
    private int id_client;                  //contador de clientes
    private final int DEPARTURE_LIMIT = 999999999;  //representando o +infinito para ser definido no tempo da próxima saída quando sistema está vazio
    Report report;

    public SimMula() {
        functions = new Functions();
        clients_out = new ArrayList<>();
        service_type_generator = new Random();
        id_client = 0;
    }

    
    @Override
    public void run(){//OK
        init();                             //0. chama rotina de inicialização do sistema
        while(simulationActive()){          //enquanto simulação não terminou
            switch (future()) {             //1. avançamos o tempo até o próximo evento
                case 'A':                   //2. se o evento atual é uma chegada
                    arrivalEvent();         //2. tratamos o evento de chegada
                    break;
                case 'D':                   //2. se o evento atual é uma partida
                    departureEvent();       //2. tratamos o evento de partida
            }
        }
        reportGenerator();
        //JOptionPane.showMessageDialog(null, "Simulação Concluída. Clock: " + clock, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Report getReport(){
        return this.report;
    }
    
    //rotina de inicialização
    private void init(){//OK
        clock = 0;                                  //1. inicializa relógio
        next_arrival = functions.nextArrivalTime(); //3. gera tempo de chegada do primeiro cliente;
        next_departure = DEPARTURE_LIMIT;           //3. valor da próxima saída deve ser obrigatoriamente maior que tempo da primeira chegada. Após o tratamento da chegada esse valor será alterado
        for (int i = 0; i < initial_queue; i++){    //vamos criar os clientes iniciais que estão aguardando na fila
            arrivalEvent();
            clock = 0;
        }
    }
    
    //Rotina de gestão de tempo
    private char future(){//OK
        if (next_arrival < next_departure){ //1. se o próximo evento for uma chegada
            clock = next_arrival;           //2. então avançamos o relógio para esse tempo
            return 'A';                     //1. e informamos que o evento nesse tempo é uma chegada
        } else {                            //1. então é porque o próximo evento é uma partida
            clock = next_departure;         //2. então avançamos o relógio para esse tempo
            return 'D';                     //1. e informamos que o evento nesse tempo é uma partida
        }
    }
    
    //Rotina do evento de chegada
    private void arrivalEvent(){//OK
        Random sla = new Random();
        Client arrived_client = new Client();                       //criamos novo cliente
        arrived_client.setArriveTime(clock);                        //definimos seu tempo de chegada
        arrived_client.setID(id_client);                            //atribuímos o ID do cliente
        int servicetype = generateServiceType();                    //geramos um tipo de serviço para esse cliente
        arrived_client.setType(servicetype);                        //definimos seu tipo de serviço
        arrived_client.setSLA(sla.nextFloat() < SLA_probability);   //vamos gerar uma variável aleatória e comparar se ela está na faixa de probabilidade SLA_probability. Com o resultado setamos o SLA do cliente
        manageClient(arrived_client);                               //vamos tratar esse cliente        
        next_arrival = functions.nextArrivalTime() + clock;         //geramos o próximo tempo de chegada
        //System.out.println("Chegou um cliente " + id_client + " : " + clock);
        id_client++;
    }
    
    //Rotina do evento de partida
    private void departureEvent(){//OK
        //vamos procurar o local que está o produto que vai deixar o sistema
        byte team = -1;                                                                 //auxiliar - equipe em que cliente irá sair
        boolean findclient = false; //auxiliar
        while (!findclient && team < teams.length){                                     //vamos percorrer as listas de atendimento de cada equipe
            team++;
            if (clients_in[team].get(0).getDepartureTime() == clock){                   //se o primeiro cliente a deixar a equipe vai sair do sistema exatamente nesse momento
                findclient = true;                                                          //entao encontramos
            }
        }
        teams[team].setServerFree(clients_in[team].get(0).getServer());                 //liberamos o servidor que estava atendendo esse cliente
        clients_out.add(clients_in[team].remove(0));                                    //removemos cliente do sistema e o colocamos na lista de clientes que já deixaram o sistema
        //agora vamos verificar as filas e ver qual o próximo cliente que pode ser atendido
        //primeiro, vamos obter os tempos dos clientes da fila específica da equipe e da fila geral para ver quem chegou primeiro...vamos comparar o SLA também
        float next_general_client_time;
        float next_team_client_time;
        if (queues[queues.length -1].size() > 0){                                      //se tem clientes na fila geral
            next_general_client_time = queues[queues.length -1].get(0).getArriveTime();    //pegamos o tempo do primeiro da fila geral
        } else {                                                                    //senão
            next_general_client_time = DEPARTURE_LIMIT;                                 //setamos esse tempo para o limite
        }
        if(queues[team].size() > 0){                                                //se tem clientes na fila da equipe
            next_team_client_time = queues[team].get(0).getArriveTime();                //pegamos o tempo do primeiro da fila da equipe
        } else {                                                                    //senão
            next_team_client_time = DEPARTURE_LIMIT;                                    //setamos esse tempo para o limite
        }
        //vamos fazer agora uma série de comparações para definir quem vai ser o cliente a ser atendido entre as duas filas: da equipe e geral
        if (next_general_client_time != DEPARTURE_LIMIT && next_team_client_time != DEPARTURE_LIMIT){ //se tenho clientes nas duas filas
            if (queues[queues.length -1].get(0).isSLA()){                                      //se o cliente da fila geral é SLA
                if (queues[team].get(0).isSLA()){                                               //então, se o cliente da fila da equipe também é SLA
                    if (next_general_client_time < next_team_client_time){                          //então vamos o que chegou primeiro é quem vai ser atendido. se o cliente da fila geral chegou primeiro
                        manageClient(queues[queues.length -1].remove(0));                                  //então removemos esse cliente da fila geral e o tratamos (como liberamos um servidor na equipe, ele vai ser tratado)
                    } else {
                        manageClient(queues[team].remove(0));                                           //senão removemos cliente da fila da equipe e o tratamos (como liberamos um servidor na equipe, ele vai ser tratado)
                    }
                } else {
                    manageClient(queues[queues.length -1].remove(0));                              //se cliente da fila da equipe não é SLA, o cliente da fila geral é o escolhido por ser SLA. então removemos esse cliente da fila geral e o tratamos (como liberamos um servidor na equipe, ele vai ser tratado)
                }
            } else {                                                                        //o cliente da fila geral não sendo SLA
                if (queues[team].get(0).isSLA()){                                               //vamos verificar se o cliente da fila da equipe é SLA
                    manageClient(queues[team].remove(0));                                           //se for então removemos esse cliente da fila da equipe e o tratamos (como liberamos um servidor na equipe, ele vai ser tratado)
                } else {                                                                        //se não for
                    if (next_general_client_time < next_team_client_time){                          //então vamos o que chegou primeiro é quem vai ser atendido. se o cliente da fila geral chegou primeiro
                        manageClient(queues[queues.length -1].remove(0));                                  //então removemos esse cliente da fila geral e o tratamos (como liberamos um servidor na equipe, ele vai ser tratado)
                    } else {
                        manageClient(queues[team].remove(0));                                           //senão removemos cliente da fila da equipe e o tratamos (como liberamos um servidor na equipe, ele vai ser tratado)
                    }
                }
            }
        } else { //senão, há clientes em 0 ou 1 das filas
            if (next_general_client_time != DEPARTURE_LIMIT){   //se a fila geral possui um cliente
                manageClient(queues[queues.length -1].remove(0));      //então tratamos removemos ele da fila e tratamos ele
            } else {                                            //senão pode ser a fila da equipe
                if(next_team_client_time != DEPARTURE_LIMIT){   //se a fila da equipe possui um cliente
                    manageClient(queues[team].remove(0));           //então tratamos removemos ele da fila e tratamos ele
                }                                               //senão não tem ninguém nas filas e não preciso fazer nada
            }
        }
        next_departure = getNextDepartureTime();                                        //verifica novo próximo tempo de partida
        //System.out.println("Saiu o cliente " + clients_out.get(clients_out.size() -1).getID() + " : " + clock);
    }
    
    //rotina para configurar função que gera os tempos de chegada
    public void configArrival(String function_name, float[] parameters){//OK
        functions.setArrivalFunction(function_name, parameters);
    }
    
    //método para configurar o tamanho inicial da fila no início da simulação
    public void configInitQueue(int queue){
        initial_queue = queue;
    }
    
    //rotina para configurar todas as funções de tempos de atendimento de cada tipo de produto por tipo de servidor
    public void configDeparture(String[] functions_name, float[][] parameters, int[][] product_and_servertype){//OK - o nome de função deve vai servir para definir qual a função que será criada
        int index;
        for (int i = 0; i < functions_name.length; i++){                                    //para cada produto teremos uma função por tipo de servidor (identificado por: IDPRODUTO * 10 + IDSERVERTYPE)
            index = product_and_servertype[i][0] * 10 + product_and_servertype[i][1];       //cada linha possui dois valores: posição 0 contém o ID do produto e posição 1 contém o ID do tipo de servidor
            functions.addServiceFunction(functions_name[i], parameters[i], index);          //vamos adicionar no nosso gerador de funções passando o nome da função e os parâmetros, passando seu index
        }
    }
    
    //método será usado para realizar a configuração dos tipos de produtos e a proporção que eles ocorrem
    public void configServiceType(float[] rate){//OK - este array contém a proporção de ocorrência do produto, onde o índice do array é o ID do produto
        service_occurrence_rate = rate;
    }
    
    //método para configurar as equipes
    public void configTeams(int number, int[] teamssize, int[] servers){//OK - número total de equipes, array com tamanho das equipes e array contendo cada servidor. cada entrada do array de servidores é um servidor, onde cada valor contém um número decimal que corresponde a equipe que o servidor pertence e sua experiência
        // o número decimal é transformado em dois valores diferentes:
        // é extraído do número a divisão inteira por 10 e o resto da divisão inteira por 10
        // a divisão inteira define a equipe
        // o resto da divisão define a experiência
        // como estamos utilizando o valor 10, a limitação do sistema é de 10 tipos de experiência diferentes
        int limitation = 10;
        teams = new Team[number];                                           //criamos o array contendo todas as equipes
        queues = new ArrayList[number + 1];                                 //criamos o array contendo todas as fila + a fila geral (última posição é a fila geral)
        clients_in = new ArrayList[number];                                 //criamos o array contendo as listas dos clientes em atendimento
        for (int i = 0; i < number; i++){                                   //vamos criar todas as equipes
            teams[i] = new Team(teamssize[i]);                              //criamos a equipe i definindo seu tamanho
            queues[i] = new ArrayList();                                    //criamos a fila correspondente a equipe i
            clients_in[i] = new ArrayList();                                //criamos a lista de clientes em atendimento correspondente a equipe i
        }
        queues[number] = new ArrayList();                                   //criamos a fila correspondente a fila geral
        for (int i = 0; i < servers.length; i++){                           //vamos ler cada servidor e inserí-lo em sua equipe correspondente
            teams[servers[i]/limitation].addServer(servers[i]%limitation);  //insiro um servidor na sua equipe com sua experiência
        }
    }
    
    //método para configurar os produtos e as equipes que podem atender estes produtos
    public void configProducts(int[] productsteams){//OK - array que corresponde a cada produto e a equipe que atende o produto
        team_of_product = productsteams; //criamos o array de listas: cada registro possui a equipe que atende aquele produto. Se o valor for igual à quantidade de equipes (ou seja ID da última equipe + 1) significa que qualquer equipe pode atender aquele produto
    }

    //método para inserir o cliente "arrived_client" na fila da equipe ID "team"
    private void addClientToQueue(Client arrived_client, int team) {//OK
        if (arrived_client.isSLA()){            //se o cliente possui SLA ele tem que ser atendido primeiro
            queues[team].add(0, arrived_client);    //então inserimos este cliente no início da fila da equipe
        } else {                                //senão for SLA
            queues[team].add(arrived_client);       //então vamos inserir esse cliente no final da fila da equipe
        }
    }

    //método para adicionar  o cliente "arrived_client" para atendimento por um servidor de experiência "server_exp" na equipe "team"
    private void addClientToService(Client arrived_client, int team, int server_exp) {//OK
        int index = arrived_client.getType() * 10 + server_exp;                 //geramos o valor de index para nossa função
        arrived_client.setServiceTime(functions.nextServiceTime(index));        //setamos o tempo de atendimento que aquele cliente irá ter
        arrived_client.setIniAttendTime(clock);                                 //setamos o início do atendimento como agora
        index = 0;                                                              //vamos usar o index para achar a posição ordenada na lista de atendimento daquele cliente
        boolean insert = false;
        while (!insert                                                          //vamos procurar a posição de inserção do item
                && index < clients_in[team].size()                             //se não acharmos o index vai estourar o tamanho da lista de clientes
                && clients_in[team].size() > 0){                                //se não tiver ninguém na lista nem vamos procurar
            if (arrived_client.getDepartureTime() 
                    < clients_in[team].get(index).getDepartureTime()){          //se o tempo de saída do cliente que está entrando em atendimento agora for menor que o tempo de saída do cliente que está na posição "index" da lista
                clients_in[team].add(index, arrived_client);                        //então coloco o cliente que está chegando nessa posição, pois ele vai sair primeiro
                insert = true;
            } else {
                index++;
            }
        }
        if (insert){                                                            //se nós achamos anteriormente um servidor para atender ao cliente (é possível que esse cliente também seja o próximo a sair do sistema e devemos verificar)
            if (clients_in[team].get(index).getDepartureTime() 
                    < next_departure){                                              //se o tempo de saída deste cliente for menor do que o tempo de saída do próximo cliente que vai sair do atendimento
                next_departure = clients_in[team].get(index).getDepartureTime();        //então este será o próximo cliente a sair do sistema, desta meneira setamos o próximo tempo de saída como o deste cliente
            }
        } else {                                                                //se não achamos inserimos no final desta lista, pois este vai ser o último  a sair / assim não precisamos alterar o tempo da próxima saída
            clients_in[team].add(arrived_client);                                   //adiciona o cliente a no final da lista de atendimento daquela equipe
            if (next_departure == DEPARTURE_LIMIT){                             //se a próxima saída é o LIMITE
                next_departure = clients_in[team].get(0).getDepartureTime();    //então quer dizer que não tinha ninguém no sistema e esse cliente é o próximo a sair
            }
        }
    }
    
    //método que deve fazer o teste para saber se a condição de parada foi atingida
    private boolean simulationActive() {//OK
        if ((next_departure > time_to_end) && (next_arrival > time_to_end)){
            clock = time_to_end;
            return false;
        } 
        return clock < time_to_end;
    }    
    
    //método para configurar o tempo de parada
    void setTimeToEnd(float time) {
        this.time_to_end = time;
    }
    
    //método para geração aleatória de tipo de serviço
    private int generateServiceType(){//OK
        float service = service_type_generator.nextFloat(); //valor aleatório entre 0 e 1
        boolean findservice = false;                        //auxiliar
        byte counter = -1;                                   //auxiliar para percorrer o array
        while (!findservice){                               //vamos percorredo o array de produtos para encontrar qual a posição que o valor aleatório está inserido
            counter++;                                      //incrementamos primeiro pois de cara podemos achar o valor e temos que retornar a posição 0
            if(service_occurrence_rate[counter] > service){ //se o valor for menor que o valor acumulado dentro daquela posição, significa que o valor está entre a faixa do produto anterior e a faixa do produto atual
                findservice = true;                         //então o valor corresponde ao produto atual, indicado por counter
            }
        }
        return counter;
    }
    
    //método para tratar um cliente o colocando em atendimento ou o colocando em fila
    private boolean manageClient(Client client){
        int server;                                                                 //auxiliar
        if (team_of_product[client.getType()] == teams.length){                         //se o ID da equipe que atende o produto for igual à quantidade de equipes, significa que esse produto pode ser atendido por qualquer equipe
            int teamfree = getTeamFree();                                                       //vamos procurar por um servidor livre nas equipes
            if (teamfree >= 0){                                                                 //se encontramos um servidor então é ele que irá atender o cliente
                server = teams[teamfree].getServer();                                       //vamos obter um servidor daquela equipe que está livre
                client.setServer(server);                                                       //setamos o servidor que está atendendo o cliente
                client.setTeam(teamfree);                                                       //setamos a equipe que irá atender o cliente
                addClientToService(client, teamfree, teams[teamfree].getServerXP(server));      //adicionamos este cliente para atendimento na equipe que está livre
            } else {                                                                            //se não encontramos, então vamos incluir esse cliente na fila geral (fila de todos as equipes)
                addClientToQueue(client, teams.length);                                     //adicionamos o cliente na fila geral (a última do array)
            }
        } else {                                                                            //se o ID da equipe que atende o produto não for menor que 0, então aloca produto nesta equipe
            if (teams[team_of_product[client.getType()]].isAnyServer()){                        //Se há algum disponível
                server = teams[team_of_product[client.getType()]].getServer();                  //vamos solicitar um servidor para atender o cliente. aqui teremos como retorno seu ID. neste momento o servidor já é setado para ocupado
                client.setServer(server);                                                       //setamos o servidor que está atendendo o cliente
                client.setTeam(team_of_product[client.getType()]);                              //setamos a equipe que irá atender o cliente
                addClientToService(client, team_of_product[client.getType()],                   //adicionamos este cliente para atendimento
                        teams[team_of_product[client.getType()]].getServerXP(server));   
            } else {                                                                            //se o valor de retorno for menor que 0 significa que todos os servidores estão ocupados
                addClientToQueue(client, team_of_product[client.getType()]);                 //insere o cliente na fila da equipe 
            }
        }
        return true;
    }
    
    //método para verificar qual o próximo tempo de partida de um cliente
    private float getNextDepartureTime(){//OK
        //vamos percorrer as lsitas dos clientes que estão em atendimento para retornarmos o tempo do menor encontrado
        float time = DEPARTURE_LIMIT;
        for (ArrayList<Client> clients : clients_in) {      //para cada lista de clientes que estão em atendimento
            if (clients.size() > 0){                            //se tem alguém nessa lista
                if (clients.get(0).getDepartureTime() < time) {     //vamos ver se o tempo de saída do cliente da primeira posição (o próximo a sair) é o menor
                    time = clients.get(0).getDepartureTime();       //se sim, esse é o novo tempo
                }
            }
        }
        return time;    //retornamos o menor tempo encontrado ou o o LIMITE
    }
    
    //método para procurar uma equipe livre para atender o produto - é usado quando um produto que pode ser atendido por qualquer equipe chega
    private int getTeamFree() {//OK
        //aqui temos que implementar a política de seleção da equipe para atender um produto que qualquer equipe pode atender
        //devemos retornar o id da equipe e também retornar um número negativo para avisar que ninguém está disponível
        Random teamid = new Random();
        ArrayList<Integer> free_teams;          //vamos criar uma lista para receber os IDs das equipes que estão livres
        free_teams = new ArrayList<>();
        for (int i =0; i < teams.length; i++) { //primeiro, vamos verificar quantas equipes estão livres
            if(teams[i].isAnyServer()){             //se equipe possui servidores disponíveis
                free_teams.add(i);                      //adiciono ID da equipe para lista de candidatos
            }
        }
        if (free_teams.size() > 0){                                     //se existe alguma equipe livre
            return free_teams.get(teamid.nextInt(free_teams.size()));       //retorno o ID da equipe escolhendo na lista de candidatos aleatoriamente
        } else {                                                        //se não existe nenhuma equipe livre
            return -1;                                                      //retorno valor negativo
        }
    }
    
    //método para definir a probabilidade de um cliente ser SLA
    public void setSLAP(float prob) {
        SLA_probability = prob;
    }
    
    /////======================PENDENTES===============================
    
    public void reportGenerator(){//PENDENTE
        //vamos criar um array com o tamanho das equipes
        report = new Report(clients_out, clients_in, queues, clock, teams[0].getTotalServers());
        report.run();
    }
}
