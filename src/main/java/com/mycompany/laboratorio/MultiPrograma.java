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
        
        switch (tipo) {
            case "produtor":
                Produtor produtor = new Produtor(porta, nome);
                produtor.run();
                break;
            case "consumidor":
                Consumidor consumidor = new Consumidor(porta, nome);
                consumidor.run();
                break;
            case "servidor":
                Servidor servidor = new Servidor(porta, nome);
                servidor.run();
                break;
            default:
                System.out.println("Tipo não válido!");
                break;
        }
    }
}
