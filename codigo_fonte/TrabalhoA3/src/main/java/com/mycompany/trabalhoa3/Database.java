package com.mycompany.trabalhoa3;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;

    public void operarDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            criarTabelas(connection);
            inserirDadosIniciais(connection);

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            fecharConexao();
        }
    }

    private void fecharConexao() {
        try {
            if(connection != null){
                connection.close();
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }



    static void criarTabelas(Connection connection) {
        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS vendedores (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nome TEXT NOT NULL)");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS produtos (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nome TEXT NOT NULL," +
                            "preco DECIMAL(10,2) NOT NULL)");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS vendas (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "id_vendedor INTEGER NOT NULL," +
                            "id_produto INTEGER NOT NULL," +
                            "quantidade INTEGER NOT NULL," +
                            "valor_total DECIMAL(10,2) NOT NULL," +
                            "data_venda TEXT NOT NULL," +
                            "FOREIGN KEY (id_vendedor) REFERENCES vendedores(id)," +
                            "FOREIGN KEY (id_produto) REFERENCES produtos(id))");
        } catch (SQLException e) {
            System.out.println("Falha ao criar tabelas.");
            e.printStackTrace();
        }
    }

    static void inserirDadosIniciais(Connection connection) {
        try {

            String insertVendedorQuery = "INSERT INTO vendedores (nome) VALUES (?)";
            PreparedStatement insertVendedorStmt = connection.prepareStatement(insertVendedorQuery);
            insertVendedorStmt.setString(1, "joao");
            insertVendedorStmt.executeUpdate();

            insertVendedorStmt.setString(1, "paulo");
            insertVendedorStmt.executeUpdate();

            insertVendedorStmt.setString(1, "nelson");
            insertVendedorStmt.executeUpdate();

            insertVendedorStmt.setString(1, "gabriel");
            insertVendedorStmt.executeUpdate();

            insertVendedorStmt.setString(1, "rafael");
            insertVendedorStmt.executeUpdate();

            String insertProdutoQuery = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
            PreparedStatement insertProdutoStmt = connection.prepareStatement(insertProdutoQuery);
            insertProdutoStmt.setString(1, "livro");
            insertProdutoStmt.setBigDecimal(2, new BigDecimal("19.99"));
            insertProdutoStmt.executeUpdate();

            insertProdutoStmt.setString(1, "caneta");
            insertProdutoStmt.setBigDecimal(2, new BigDecimal("1.99"));
            insertProdutoStmt.executeUpdate();

            insertProdutoStmt.setString(1, "borracha");
            insertProdutoStmt.setBigDecimal(2, new BigDecimal("0.99"));
            insertProdutoStmt.executeUpdate();

            insertProdutoStmt.setString(1, "caderno");
            insertProdutoStmt.setBigDecimal(2, new BigDecimal("4.99"));
            insertProdutoStmt.executeUpdate();

            String insertVendaQuery = "INSERT INTO vendas (id_vendedor, id_produto, quantidade, valor_total, data_venda) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertVendaStmt = connection.prepareStatement(insertVendaQuery);

            insertVendaStmt.setInt(1, 5);
            insertVendaStmt.setInt(2, 3);
            insertVendaStmt.setInt(3, 2);
            insertVendaStmt.setBigDecimal(4, new BigDecimal("1.98"));
            insertVendaStmt.setString(5, "2023-02-11");
            insertVendaStmt.executeUpdate();

            insertVendaStmt.setInt(1, 4);
            insertVendaStmt.setInt(2, 4);
            insertVendaStmt.setInt(3, 1);
            insertVendaStmt.setBigDecimal(4, new BigDecimal("4.99"));
            insertVendaStmt.setString(5, "2023-04-23");
            insertVendaStmt.executeUpdate();

            insertVendaStmt.setInt(1, 1);
            insertVendaStmt.setInt(2, 1);
            insertVendaStmt.setInt(3, 5);
            insertVendaStmt.setBigDecimal(4, new BigDecimal("99.95"));
            insertVendaStmt.setString(5, "2023-06-12");
            insertVendaStmt.executeUpdate();

            insertVendaStmt.setInt(1, 2);
            insertVendaStmt.setInt(2, 2);
            insertVendaStmt.setInt(3, 3);
            insertVendaStmt.setBigDecimal(4, new BigDecimal("5.97"));
            insertVendaStmt.setString(5, "2023-10-03");
            insertVendaStmt.executeUpdate();

            insertVendaStmt.setInt(1, 3);
            insertVendaStmt.setInt(2, 4);
            insertVendaStmt.setInt(3, 3);
            insertVendaStmt.setBigDecimal(4, new BigDecimal("14.97"));
            insertVendaStmt.setString(5, "2023-12-30");
            insertVendaStmt.executeUpdate();

            System.out.println("Dados inseridos com sucesso.");
        } catch (SQLException e) {
            System.out.println("Falha ao inserir os dados.");
            e.printStackTrace();
        }
    }

}

