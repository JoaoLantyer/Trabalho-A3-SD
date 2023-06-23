package com.mycompany.trabalhoa3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket implements Runnable {

    private ServerSocket serverSocket;
    private Integer identificador;
    private Processo me;

    public ServidorSocket(Integer port, Integer identificador, Processo me) throws IOException {
        serverSocket = new ServerSocket(port);
        this.identificador = identificador;
        this.me = me;
        System.out.println("Servidor iniciado na porta " + port + ". Aguardando conexões...");
    }

    @Override
    public void run(){
        try {
            while(true){
                // Aguarda conexões do cliente
                Socket socket = serverSocket.accept();
                // Cria uma nova thread para lidar com o cliente
                Thread thread = new Thread(new ClienteHandler(socket, this.identificador, me));
                // Inicia a thread
                thread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
