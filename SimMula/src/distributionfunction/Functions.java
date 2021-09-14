
package distributionfunction;

import java.util.ArrayList;

/**
 * Functions
 * Classe geradora de valores das distribuições dos eventos
 */
public class Functions {
    
    private DistributionFunction arrival_function;
    private ArrayList<DistributionFunction> departure_functions;
    private ArrayList<Integer> function_table;  //array para fazer tradução da posição de cada função de serviço na lista
    
    public Functions(){
        departure_functions = new ArrayList<>();
        function_table = new ArrayList<>();
    }
    
    //método para a criação da função de distribuição que irá gerar os tempos de chegada
    public boolean setArrivalFunction(String type, float[] parameters){
        switch(type){
            case "Exponencial": 
                arrival_function = new Exponential();
                break;
        }
        arrival_function.setParameters(parameters);
        return true;
    }
    
    //método para adicionar uma função de distribuição que irá gerar tempos de atendimento...o retorno é o id da função
    public int addServiceFunction(String type, float[] parameters, int index){
        DistributionFunction function = null;
        switch(type){//vamos instanciar a função correta
            case "Uniforme":
                function = new Uniform();
                break;
            case "Exponencial":
                function = new Exponential();
                break;
            case "Gamma":
                function = new Gamma();
                break;
            default:
                System.out.println("Função não implementada.");
        }
        function.setParameters(parameters); //adicionamos os parâmetros...aqui podemos colocar um tratamento em caso função não existir
        departure_functions.add(function);
        function_table.add(index);
        return departure_functions.size();
    }
    
    //rotina para retornar um novo tempo de chegada
    public float nextArrivalTime(){
        return arrival_function.nextValue();
    }
    
    //rotina para retornar um novo tempo de atendimento recebendo como parâmetro qual o tipo de produto e o tipo de servidor
    public float nextServiceTime(int index){
        byte counter = 0;
        boolean find = false;
        while(!find && counter < function_table.size()){    //vamos percorrer nossa tabela em busca do index
            if(function_table.get(counter) == index){         //se acharmos o index
                find = true;                                //dizemos que achamos
            } else {
                counter++;                                  //se não vamos olhar na próxima posição
            }                                      
        }
        return departure_functions.get(counter).nextValue();//o contador vai guardar a posição em que está a função correspondente aquele index
    }
    
}
