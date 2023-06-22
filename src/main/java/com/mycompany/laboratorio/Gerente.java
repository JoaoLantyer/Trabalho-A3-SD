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

                if(Processos.getInstance().getMe() == Processos.getInstance().getLider()){
                    System.out.println("Servidor indisponível, executando servidor temporário: ");
                }

                if(!Processos.getInstance().checkServidor()){
                    Eleicao.getInstance().callEleicao();
                }

                Processo processoLider = Processos.getInstance().getLider();

                    try {

                        Thread.sleep(500);

                        ClienteSocket socket = new ClienteSocket(processoLider.getHost(), processoLider.getPort());
                        String resposta;

                        ExibirMenu();

                        int escolha;

                        try {
                            System.out.print("SUA ESCOLHA: ");
                            escolha = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("ERRO! VALOR INVALIDO!");
                            run();
                            return;
                        }

                        switch (escolha) {
                            case 1:
                                System.out.print("digite o nome do vendedor: ");
                                String nomeVendedor = scanner.nextLine();

                                socket.enviar("06|" + nomeVendedor);

                                resposta = socket.receber();
                                System.out.println("Resposta do servidor: " + resposta);
                                break;

                            case 2:
                                System.out.print("digite o nome do produto:");
                                String nomeProduto = scanner.nextLine();

                                socket.enviar("07|" + nomeProduto);
                                resposta = socket.receber();
                                System.out.println("Resposta do servidor: " + resposta);
                                break;

                            case 3:
                                System.out.print("digite a data inicial (no formato AAAA-MM-DD):");
                                String dataInicial = scanner.nextLine();
                                System.out.print("digite a data final (no formato AAAA-MM-DD):");
                                String dataFinal = scanner.nextLine();

                                socket.enviar("08|" + dataInicial + "|" + dataFinal);

                                resposta = socket.receber();
                                System.out.println("Resposta do servidor: " + resposta);
                                break;
                            case 4:
                                socket.enviar("09|");
                                resposta = socket.receber();
                                System.out.println("Resposta do servidor: " + resposta);
                                break;

                            case 5:
                                socket.enviar("10|");
                                resposta = socket.receber();
                                System.out.println("Resposta do servidor: " + resposta);
                                break;

                            default:
                                System.out.println("VALOR INVÁLIDO!");
                                break;
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com " + processoLider.getIdentificador() + ": " + ex.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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