package com.mycompany.laboratorio;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Gerente extends Tipo {

    Scanner scanner = new Scanner(System.in);
    public Gerente(String porta, String nome){
        super(porta, nome);
    }
    
    @Override
    public void run(){
        while(true){
            if(!Eleicao.getInstance().isEleicaoIniciada()){
                Processo processo = Processos.getInstance().getRandomProcesso();
                if(!processo.isLider()){
                    try {
                        ClienteSocket socket = new ClienteSocket(processo.getHost(), processo.getPort());

                        ExibirMenu();

                        int escolha;

                        try{
                            escolha = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e){
                            System.out.println("ERRO! VALOR INVALIDO!");
                            run();
                            return;
                        }

                        switch(escolha) {
                            case 1:
                                System.out.print("digite o nome do vendedor: ");
                                String nomeVendedor = scanner.nextLine();

                                socket.enviar("06|" + nomeVendedor);

                                String resposta = socket.receber();
                                System.out.println("Resposta do servidor: " + resposta);
                                break;

                            case 2:
                                System.out.print("digite o nome do produto:");
                                String nomeProduto = scanner.nextLine();

                                socket.enviar("07|" + nomeProduto);
                                break;

                            case 3:
                                System.out.print("digite a data inicial:");
                                String dataInicial = scanner.nextLine();
                                System.out.print("digite a data final:");
                                String dataFinal = scanner.nextLine();

                                socket.enviar("08|" + dataInicial + "|" + dataFinal);
                                break;
                            case 4:
                                System.out.print("digite o nome do vendedor:");
                                String nomeMelhorVendedor = scanner.nextLine();

                                socket.enviar("09|" + nomeMelhorVendedor);
                                break;

                            case 5:
                                System.out.print("digite o nome do produto:");
                                String nomeMelhorProduto = scanner.nextLine();

                                socket.enviar("10|" + nomeMelhorProduto);
                                break;

                            default:
                                 System.out.println("VALOR INVÁLIDO!");
                                break;
                        }

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
        }
    }

    public void ExibirMenu(){
        System.out.println("-----------------------------------------------------------");
        System.out.println("|                    REALIZAR CONSULTA                    |");
        System.out.println("|                                                         |");
        System.out.println("|   [1] Total de vendas de um vendedor                    |");
        System.out.println("|   [2] Total de vendas de um produto                     |");
        System.out.println("|   [3] Total de vendas dos produtos em um período        |");
        System.out.println("|   [4] Melhor vendedor                                   |");
        System.out.println("|   [5] Melhor produto                                    |");
        System.out.println("|                                                         |");
        System.out.println("-----------------------------------------------------------\n");
    }


}