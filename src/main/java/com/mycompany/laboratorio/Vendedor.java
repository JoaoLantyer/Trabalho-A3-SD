package com.mycompany.laboratorio;

import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Vendedor extends Tipo {
    Scanner scanner = new Scanner(System.in);
    public Vendedor(String porta, String nome){
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

                        ExibirTabelas();

                        try {

                            System.out.println("\nREALIZAR UMA VENDA:");

                            System.out.print("Digite o nome do vendedor: ");
                            String nomeVendedor = scanner.nextLine();

                            System.out.print("Digite o nome do produto: ");
                            String nomeProduto = scanner.nextLine();

                            System.out.print("Digite a quantidade de " + nomeProduto + "s vendidos: ");
                            int quantidade = scanner.nextInt();
                            scanner.nextLine();

                            System.out.print("Digite a data da venda (no formato AAAA-MM-DD): ");
                            String dataVenda = scanner.nextLine();


                            socket.enviar("05|" + quantidade + "|" + nomeProduto + "|" + nomeVendedor + "|" + dataVenda);

                        } catch (InputMismatchException e) {
                            System.out.println("ERRO! VALOR INVALIDO!");
                            run();
                            return;
                        }

                        String resposta = socket.receber();
                        if (resposta.equals("OK - venda realizada") || resposta.equals("ERRO - dados inválidos tente novamente")) {
                            System.out.println("Resposta do servidor: " + resposta);
                        }

                        Thread.sleep(1000 * 5);
                    } catch (IOException ex) {
                        Logger.getLogger(MultiPrograma.class.getName()).log(Level.SEVERE, "Erro na conexao com " + processoLider.getIdentificador() + ": " + ex.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    public void ExibirTabelas() {
        System.out.println("NOME DOS VENDEDORES: joao, paulo, nelson, gabriel, rafael");
        System.out.println("NOME DOS PRODUTOS: livro, caneta, borracha, caderno");
    }

}
