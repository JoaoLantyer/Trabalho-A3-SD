package com.mycompany.laboratorio;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Tipo {
    
    protected String porta;
    protected Integer nome; 
    protected Processo me;   
    protected ServidorSocket serverSocket;
    
    public Tipo(String porta, String nome){
        this.porta = porta;
        this.nome = Integer.valueOf(nome);
        
        Properties prop = new Properties();
        try(FileInputStream fis = new FileInputStream("app.config")){
            prop.load(fis);
        } catch (FileNotFoundException ex){
            System.err.println("Arquivo de configuração não encontrado");
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
        
        System.out.println(prop.getProperty("app.name"));
        
        Integer totalProcesso = Integer.valueOf(prop.getProperty("app.processo.total"));
        Map<Integer, Processo> processos = new HashMap<>();
        for(int i = 1; i <= totalProcesso; i++){
            Integer identificador = Integer.valueOf(prop.getProperty("app.processo." + i + ".identificador"));
            String host = prop.getProperty("app.processo." + i + ".host");
            Integer port = Integer.valueOf(prop.getProperty("app.processo." + i + ".port"));
            Processo processo = new Processo(identificador, host, port);
            if (identificador.equals(this.nome)){
                me = processo;
            }
            processos.put(identificador, processo);
        }
        
        Processo processoLider = processos.get(processos.size());
        processoLider.setIsLider(Boolean.TRUE);
        
        this.iniciarConexao();
        Processos gerenciador = Processos.getInstance();
        gerenciador.config(processos, me, processoLider, totalProcesso);
    }
    
    public abstract void run();
    
    protected void iniciarConexao(){
        try {
            serverSocket = new ServidorSocket(Integer.valueOf(this.porta), me.getIdentificador(), me);
            Thread thread = new Thread(serverSocket);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void checkLider(){
        Processo processoLider = Processos.getInstance().getLider();
        try{
            ClienteSocket socket = new ClienteSocket(processoLider.getHost(), processoLider.getPort());
            socket.enviar("02|Você é o líder!");
            String resposta = socket.receber();
            System.out.println("Resposta: " + resposta);
        } catch (IOException ex){
            Eleicao.getInstance().callEleicao();
            Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com {0}: {1}", new Object[]{processoLider.getIdentificador(), ex.getMessage()});
        }
    }
}
