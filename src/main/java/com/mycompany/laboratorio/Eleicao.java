package com.mycompany.laboratorio;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Eleicao {
    private static Eleicao uniqueInstance;
    
    private Boolean eleicaoIniciada;
    
    private Eleicao(){
        eleicaoIniciada = false;
    }
    
    public static synchronized Eleicao getInstance(){
        if (uniqueInstance == null) {
            uniqueInstance = new Eleicao();
        }
        return uniqueInstance;
    }
    
    public void callEleicao() {
        Processo me = Processos.getInstance().getMe();
        this.sendMsg("03|"+ me.getIdentificador());
        this.eleicaoIniciada = true;
}
    public void checkEleicao(String msg){
        this.eleicaoIniciada = true;
        Processo me = Processos.getInstance().getMe();
        List<String> candidatos = Arrays.asList(msg.split("-"));
        // verifica se estou na lista
        if (!candidatos.contains(me.getIdentificador().toString())){
            //Me adiciona na lista
            this.sendMsg("03|"+msg + "-" + me.getIdentificador());
        }else{
            //Eleicao finalizada
            String lider = Collections.max(candidatos);
            System.out.println("Lider eleito: " + lider);
            this.informeEleito(lider);
            this.atualizarLider(Integer.valueOf(lider));
        }    
    }
    
    public void atualizarLider(Integer lider){
        Processo processo = Processos.getInstance().getProcessos().get(lider);
        processo.setIsLider(true);
        Processos.getInstance().setLider(processo);
        this.eleicaoIniciada = false;
    }
    
    private void sendMsg(String msg){
        boolean stop = false;
        int idProcesso = Processos.getInstance().getMe().getIdentificador() + 1;
        while(!stop){
            if (idProcesso > Processos.getInstance().getTotalProcesso()){
                idProcesso = 1;
            }
            
            Processo processo = Processos.getInstance().getProcessos().get(idProcesso);
            try{
             ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());
             socket.enviar(msg);
             String resposta = socket.receber();
             System.out.println("Resposta: " + resposta);
             stop = true;
            } catch (IOException ex) { 
                idProcesso++;
                Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
            }
        }
    }
    
    private void informeEleito(String lider){
        for(Processo processo : Processos.getInstance().getProcessos().values()){
            try {
                ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());
                socket.enviar("04|"+lider);
                String resposta = socket.receber();
                System.out.println("Resposta: " + resposta);
            } catch (IOException ex){
                Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com " + processo.getIdentificador() + ": " + ex.getMessage());
            }
        }
    }
    
    public Boolean isEleicaoIniciada(){
        return eleicaoIniciada;
    }
}