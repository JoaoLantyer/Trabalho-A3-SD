package com.mycompany.laboratorio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket implements Runnable {
    
    private ServerSocket serverSocket;
    private Integer identificador;
    
    public ServidorSocket(Integer port, Integer identificador) throws IOException {
        serverSocket = new ServerSocket(port);
        this.identificador = identificador;
        System.out.println("Servidor iniciado na porta " + port + ". Aguardando conexões...");
    }
    
    @Override
    public void run(){
        try {
            while(true){
                // Aguarda conexões do cliente
                Socket socket = serverSocket.accept();
                // Cria uma nova thread para lidar com o cliente
                Thread thread = new Thread(new ClienteHandler(socket, this.identificador));
                // Inicia a thread
                thread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
    

