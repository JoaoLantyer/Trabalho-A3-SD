package com.mycompany.laboratorio;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor extends Tipo {
    public Servidor(String porta, String nome){
        super(porta, nome);
    }

    @Override
    public void run(){
        boolean vendaRealizada = false;
        while(true){
            if(!Eleicao.getInstance().isEleicaoIniciada()){
                Processo processo = Processos.getInstance().getRandomProcesso();
                if(!processo.isLider()){
                    try {
                        ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());

                        String resposta = socket.receber();
                        System.out.println("Resposta: " + resposta);

                            System.out.println("Dormi");
                            Thread.sleep(1000*60);
                            System.out.println("Acordei");
                            Thread.sleep(1000*60);

                    } catch (IOException ex) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE,"Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
                    } catch (InterruptedException e) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, null, e);
                    }
                } else {
                    this.checkLider();
                    try {
                        Thread.sleep(1000*5);
                    } catch (InterruptedException e) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        }
    }
}