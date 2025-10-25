package com.github.charlly.wEcantamentos.config.conexao;

import com.github.charlly.wEcantamentos.Main;
import com.github.charlly.wEcantamentos.config.util.MensagensUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {

    public static Connection con = null;

    public static void open() {
        Main main = Main.getPlugin(Main.class);

        String type = main.getConfiguracao().getString("database.type");
        boolean enable = main.getConfiguracao().getBoolean("database.mysql.enable");

        if (enable && type.equalsIgnoreCase("MySQL")) {
            String host = main.getConfiguracao().getString("database.mysql.host");
            String user = main.getConfiguracao().getString("database.mysql.user");
            String password = main.getConfiguracao().getString("database.mysql.password");
            Integer port = main.getConfiguracao().getInt("database.mysql.port");
            String database = main.getConfiguracao().getString("database.mysql.database");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

            try {
                con = DriverManager.getConnection(url, user, password);
                main.getLogger().info(MensagensUtil.prefix + "§aConexão com o banco de dados estabelecida!");

            } catch (SQLException e) {
                main.getLogger().info(MensagensUtil.prefix + "§cNão foi possível realizar a conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        if (con != null) {
            try {
                con.close();
                Main.getPlugin(Main.class).getLogger().info(MensagensUtil.prefix + "§aConexão com o banco de dados fechada");
            } catch (SQLException e) {
                Main.getPlugin(Main.class).getLogger().info(MensagensUtil.prefix + "§cErro para fechar a conexão: " + e.getMessage());
            }
        }
    }

    public static void createTables() {
        if (con == null) {
            return;
        }

        PreparedStatement stm = null;
        try {
            String sql = "CREATE TABLE IF NOT EXISTS player_xp ("
                    + "uuid VARCHAR(36) NOT NULL, "
                    + "xp INT DEFAULT 0, "
                    + "exp_progress FLOAT DEFAULT 0.0, "
                    + "PRIMARY KEY (uuid));";

            stm = con.prepareStatement(sql);
            stm.executeUpdate();

        } catch (SQLException e) {
            Main.getPlugin(Main.class).getLogger().severe("Erro ao criar a tabela 'player_xp'!");
            e.printStackTrace();
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}