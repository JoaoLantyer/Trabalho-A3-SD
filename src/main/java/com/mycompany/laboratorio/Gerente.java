package com.mycompany.laboratorio;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Gerente extends Tipo {
    public Gerente(String porta, String nome){
        super(porta, nome);
    }
    
    @Override
    public void run(){
        while(true){
           try{
               Thread.sleep(1000*5);
                if(!Eleicao.getInstance().isEleicaoIniciada()){
                    Processo processo = Processos.getInstance().getRandomProcesso();
                    if(!processo.isLider()){
                        try {
                            ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());
                            
                            String resposta = socket.receber();
                            System.out.println("Resposta: " + resposta);
                        } catch (IOException ex) {
                            Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE,"Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
                        }
                    } else {
                        this.checkLider();
                        try {
                            Thread.sleep(1000*5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (InterruptedException ex){
                Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "ThreadSleep", ex);
            }
        }
    }
}