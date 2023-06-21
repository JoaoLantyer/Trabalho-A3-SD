package com.mycompany.laboratorio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClienteHandler implements Runnable {

    private Socket socket;
    private Integer identificador;

    private Processo me;

    public ClienteHandler(Socket socket, Integer identificador, Processo me) {
        this.socket = socket;
        this.identificador = identificador;
        this.me = me;
    }

    @Override
    public void run() {
        try {
            System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String msg = in.readLine();
            System.out.println("Mensagem recebida: " + msg);
            String[] protocolo = msg.split("\\|");
            switch (protocolo[0]) {
                case "01":
                    out.println("Oi! E eu sou o processo " + this.me.getIdentificador());
                    break;
                case "02":
                    if (me.isLider()) {
                        out.println("Eu sou o líder! Processo " + this.me.getIdentificador());
                    } else {
                        out.println("Eu não sou o líder, eu sou o processo " + this.me.getIdentificador());
                    }
                    break;
                case "03":
                    out.println("OK");
                    Eleicao.getInstance().checkEleicao(protocolo[1]);
                    break;
                case "04":
                    out.println("OK");
                    Eleicao.getInstance().atualizarLider(Integer.valueOf(protocolo[1]));
                    break;
                case "05":
                    if (protocolo.length >= 5) {
                        int quantidade = Integer.parseInt(protocolo[1]);
                        String nomeProduto = protocolo[2];
                        String nomeVendedor = protocolo[3];
                        String dataVenda = protocolo[4];

                        boolean vendaRealizada = realizarVenda(nomeVendedor, nomeProduto, quantidade, dataVenda);

                        if (vendaRealizada){
                            out.println("OK");
                        } else {
                            out.println("ERRO");
                        }
                    }
                    break;
                case "06":

                    out.println(exibirVendedorMaiorVendas(protocolo[1]));

                    break;
                case "07":

                    out.println(exibirProdutoMaiorVendas(protocolo[1]));

                    break;
                case "08":

                    out.println(exibirVendasDatas(protocolo[1], protocolo[2]));

                    break;
                case "09":

                    out.println(exibirMelhorVendedor());

                    break;
                case "10":

                    out.println(exibirMelhorProduto());

                    break;
                default:
                    System.out.println("codigo: " + protocolo[0]);
                    out.println("11|Error");
                    break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean realizarVenda(String nomeVendedor, String nomeProduto, int quantidade, String dataVenda) {
        boolean vendaRealizada = false;
        try {
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            String selectVendedorQuery = "SELECT id FROM vendedores WHERE nome = ?";
            PreparedStatement selectVendedorStmt = connection.prepareStatement(selectVendedorQuery);
            selectVendedorStmt.setString(1, nomeVendedor);
            ResultSet rsVendedor = selectVendedorStmt.executeQuery();

            int vendedorId = -1;
            if (rsVendedor.next()) {
                vendedorId = rsVendedor.getInt("id");
            }

            String selectProdutoQuery = "SELECT id, preco FROM produtos WHERE nome = ?";
            PreparedStatement selectProdutoStmt = connection.prepareStatement(selectProdutoQuery);
            selectProdutoStmt.setString(1, nomeProduto);
            ResultSet rsProduto = selectProdutoStmt.executeQuery();

            float valorTotal = -1;
            int produtoId = -1;

            if (rsProduto.next()) {
                produtoId = rsProduto.getInt("id");
                valorTotal = quantidade * (rsProduto.getFloat("preco"));
            }

            if (vendedorId != -1 && produtoId != -1) {
                vendaRealizada = true;
            }

            String insertVendaQuery = "INSERT INTO vendas (id_vendedor, id_produto, quantidade, valor_total, data_venda) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertVendaStmt = connection.prepareStatement(insertVendaQuery);

            insertVendaStmt.setInt(1, vendedorId);
            insertVendaStmt.setInt(2, produtoId);
            insertVendaStmt.setInt(3, quantidade);
            insertVendaStmt.setBigDecimal(4, new BigDecimal(valorTotal));
            insertVendaStmt.setString(5, dataVenda);
            insertVendaStmt.executeUpdate();

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return vendaRealizada;
    }
    public String exibirVendedorMaiorVendas(String nomeVendedor) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            String query = "SELECT vendedores.nome, COUNT(*) AS vendas_count " +
                    "FROM vendas " +
                    "JOIN vendedores ON vendas.id_vendedor = vendedores.id " +
                    "WHERE vendedores.nome = ? " +
                    "GROUP BY vendedores.nome";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomeVendedor);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int vendasCount = rs.getInt("vendas_count");
                return "NOME DO VENDEDOR: " + nomeVendedor + ", VENDAS: " + vendasCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Vendedor não encontrado ou não possui vendas";
    }

    public String exibirProdutoMaiorVendas(String nomeProduto) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            String query = "SELECT produtos.nome, COUNT(*) AS vendas_count " +
                    "FROM vendas " +
                    "JOIN produtos ON vendas.id_produto = produtos.id " +
                    "WHERE produtos.nome = ? " +
                    "GROUP BY produtos.nome";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomeProduto);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int vendasCount = rs.getInt("vendas_count");
                return "NOME DO PRODUTO: " + nomeProduto + ", VENDAS: " + vendasCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Produto não encontrado ou não possui vendas";
    }

    public String exibirMelhorVendedor() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            float maiorValor = 0;
            int vendedorId = 0;

            for(int i = 0; i < 6; i++) {
                float valorAtual = 0;
                String selectVendedorQuery = "SELECT valor_total FROM vendas WHERE id_vendedor = ?";
                PreparedStatement selectVendedorStmt = connection.prepareStatement(selectVendedorQuery);
                selectVendedorStmt.setInt(1, i);
                ResultSet rsVendedor = selectVendedorStmt.executeQuery();
                while (rsVendedor.next()) {
                    valorAtual += rsVendedor.getFloat("valor_total");
                }
                if(valorAtual > maiorValor){
                    maiorValor = valorAtual;
                    vendedorId = i;
                }
            }

            String selectNomeVendedorQuery = "SELECT nome FROM vendedores WHERE id = ?";
            PreparedStatement selectNomeVendedorStmt = connection.prepareStatement(selectNomeVendedorQuery);
            selectNomeVendedorStmt.setInt(1, vendedorId);
            ResultSet rsNomeVendedor = selectNomeVendedorStmt.executeQuery();
            String nomeVendedor = rsNomeVendedor.getString("nome");

                return "NOME DO VENDEDOR: " + nomeVendedor + ", VALOR TOTAL DE VENDAS: " + maiorValor;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Nenhum vendedor encontrado ou não há vendas registradas.";
    }

    public String exibirMelhorProduto() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            String query = "SELECT produtos.nome, COUNT(*) AS vendas_count " +
                    "FROM vendas " +
                    "JOIN produtos ON vendas.id_produto = produtos.id " +
                    "GROUP BY produtos.nome " +
                    "ORDER BY vendas_count DESC " +
                    "LIMIT 1";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                String nomeProduto = rs.getString("nome");
                int vendasCount = rs.getInt("vendas_count");
                return "NOME DO PRODUTO: " + nomeProduto + ", VENDAS: " + vendasCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Nenhum produto encontrado ou não há vendas registradas.";
    }

    public String exibirVendasDatas(String dataInicial, String dataFinal) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            String query = "SELECT * FROM vendas WHERE data_venda BETWEEN date(?) AND date(?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dataInicial);
            statement.setString(2, dataFinal);

            ResultSet rs = statement.executeQuery();

            int quantidadeTotal = 0;
            while (rs.next()) {
                quantidadeTotal += rs.getInt("quantidade");
            }

            if (quantidadeTotal > 0) {
                return "Quantidade de vendas realizada neste período: " + quantidadeTotal;
            } else {
                return "Nenhuma venda realizada neste período.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Erro ao recuperar as vendas.";
    }

}
