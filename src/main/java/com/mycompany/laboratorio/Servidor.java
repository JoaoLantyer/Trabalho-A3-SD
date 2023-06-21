package com.mycompany.laboratorio;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor extends Tipo {
    public Servidor(String porta, String nome) {
        super(porta, nome);
    }

    @Override
    public void run() {
        while (true) {
            if (!Eleicao.getInstance().isEleicaoIniciada()) {
                Processo processo = Processos.getInstance().getRandomProcesso();
                if (!processo.isLider()) {
                } else {
                    this.checkLider();
                    try {
                        ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());
                        if(socket.receber().equals("PING")){
                            socket.enviar("11| OK");
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}