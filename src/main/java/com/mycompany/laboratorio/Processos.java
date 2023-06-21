package com.mycompany.laboratorio;

import java.io.IOException;
import java.util.Map;
import java.util.Random;


public class Processos {
    private static Processos uniqueInstance;
    
    private Map<Integer, Processo> processos;    
    private Processo me;
    private Processo lider;

    private Integer totalProcesso;

    public Map<Integer, Processo> getProcessos() {
        return processos;
    }

    public void setProcessos(Map<Integer, Processo> processos) {
        this.processos = processos;
    }

    public Processo getMe() {
        return me;
    }

    public void setMe(Processo me) {
        this.me = me;
    }

    public Processo getLider() {
        return lider;
    }

    public void setLider(Processo lider) {
        this.lider = lider;
    }

    public Integer getTotalProcesso() {
        return totalProcesso;
    }

    public void setTotalProcesso(Integer totalProcesso) {
        this.totalProcesso = totalProcesso;
    }
    
    
    
    private Processos(){
    }
    
    public static synchronized Processos getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new Processos();
        }
        return uniqueInstance;   
    }
    
    public void config(Map processos, Processo me, Processo lider, Integer totalProcesso) {
        this.processos = processos;
        this.me = me;
        this.lider = lider;
        this.totalProcesso = totalProcesso;
    }
    
        public Processo getProcessoByIdentificador(Integer identificador) {
        return processos.get(identificador);
    }
    
    public Processo getRandomProcesso(){
        Random rand = new Random();
        int index = rand.nextInt(processos.size());
        index = (index == 0) ? processos.size() : index;
        return processos.get(index);
    }

    public String checkServidor() {
        try {
            ClienteSocket socket = new ClienteSocket(this.lider.getHost(), this.lider.getPort());
            socket.enviar("11| PING");
            String resposta = socket.receber();

            return resposta;
        } catch (IOException ex) {
            return "ERRO";
        }
    }
}
