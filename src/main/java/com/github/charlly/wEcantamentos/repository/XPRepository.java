package com.github.charlly.wEcantamentos.repository;

import com.github.charlly.wEcantamentos.config.conexao.Connections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class XPRepository {

    public synchronized boolean hasPlayerData(UUID uuid) {
        if (Connections.con == null) return false;

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = Connections.con.prepareStatement("SELECT uuid FROM player_xp WHERE uuid = ?");
            stm.setString(1, uuid.toString());
            rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void createPlayerData(UUID uuid, int initialLevel) {
        if (Connections.con == null) return;

        PreparedStatement stm = null;
        try {
            stm = Connections.con.prepareStatement("INSERT IGNORE INTO player_xp (uuid, xp, exp_progress) VALUES (?, ?, ?)");
            stm.setString(1, uuid.toString());
            stm.setInt(2, initialLevel);
            stm.setFloat(3, 0.0f);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void updatePlayerData(UUID uuid, int level, float progress) {
        if (Connections.con == null) return;

        PreparedStatement stm = null;
        try {
            stm = Connections.con.prepareStatement("UPDATE player_xp SET xp = ?, exp_progress = ? WHERE uuid = ?");
            stm.setInt(1, level);
            stm.setFloat(2, progress);
            stm.setString(3, uuid.toString());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized int getPlayerLevel(UUID uuid) {
        if (Connections.con == null) return 0;

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = Connections.con.prepareStatement("SELECT xp FROM player_xp WHERE uuid = ?");
            stm.setString(1, uuid.toString());
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("xp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public synchronized float getPlayerProgress(UUID uuid) {
        if (Connections.con == null) return 0.0f;

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = Connections.con.prepareStatement("SELECT exp_progress FROM player_xp WHERE uuid = ?");
            stm.setString(1, uuid.toString());
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getFloat("exp_progress");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0.0f;
    }
}