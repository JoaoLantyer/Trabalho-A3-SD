package com.mycompany.laboratorio;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor extends Tipo {
    public Servidor(String porta, String nome){
        super(porta, nome);
    }

    @Override
    public void run(){

        while(true){
            if(!Eleicao.getInstance().isEleicaoIniciada()){
                Processo processo = Processos.getInstance().getRandomProcesso();

                    try {
                        ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());

                        String resposta = socket.receber();
                        System.out.println("Resposta: " + resposta);
                    } catch (IOException ex) {
                            Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE,"Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
                    }

            }
        }
    }
}