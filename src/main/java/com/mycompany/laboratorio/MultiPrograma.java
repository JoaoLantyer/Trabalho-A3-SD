package com.mycompany.laboratorio;


public class MultiPrograma {
    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Para executar o programa: MultiPrograma <tipo> <identificador> <porta>");
            System.exit(0);
        }
        
        String tipo = args[0];
        String nome = args[1];
        String porta = args[2];
        System.out.println("Olá, eu sou o programa do tipo " + tipo + " com o identificador " + nome);
        
        if (tipo.equals("produtor")){
            Produtor produtor = new Produtor(Integer.valueOf(porta), nome);
            produtor.run();
        }else if(tipo.equals("consumidor")){
            Consumidor consumidor = new Consumidor(Integer.valueOf(porta), nome);
            consumidor.run();      
        }else if(tipo.equals("servidor")){
            Servidor servidor = new Servidor(Integer.valueOf(porta), nome);
            servidor.run();
        } else {
            System.out.println("Tipo não válido!");
        }
    }
}
