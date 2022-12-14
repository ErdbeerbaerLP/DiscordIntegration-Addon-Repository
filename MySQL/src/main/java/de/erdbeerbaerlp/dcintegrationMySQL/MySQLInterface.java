package de.erdbeerbaerlp.dcintegrationMySQL;

import de.erdbeerbaerlp.dcintegration.common.storage.PlayerLink;
import de.erdbeerbaerlp.dcintegration.common.storage.PlayerSettings;
import de.erdbeerbaerlp.dcintegration.common.storage.database.DBInterface;
import de.erdbeerbaerlp.dcintegration.common.util.Variables;

import java.sql.*;
import java.util.ArrayList;

public class MySQLInterface extends DBInterface {
    Connection connection = null;

    @Override
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + MySQLAddon.cfg.host + ":" + MySQLAddon.cfg.port + "/" + MySQLAddon.cfg.database, MySQLAddon.cfg.user, MySQLAddon.cfg.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        try {
            final Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerLinks(id BIGINT not null primary key,mcuuid text,fluuid text,settings json not null)");
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isConnected() {
        if (connection == null) return false;
        try {
            return connection.isValid(10);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void addLink(PlayerLink link) {
        try {
            final Statement statement = connection.createStatement();
            statement.executeUpdate("replace into PlayerLinks(id,mcuuid,fluuid,settings) values('" + link.discordID + "', '" + link.mcPlayerUUID + "', '" + link.floodgateUUID + "','" + gson.toJson(link.settings) + "')");
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeLink(String id) {
        try {
            final Statement statement = connection.createStatement();
            statement.executeUpdate("delete from PlayerLinks where id='" + id + "'");
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private PlayerLink getLink(ResultSet res) throws SQLException {
        if (res != null && res.next()) {
            if (res.wasNull())
                return null;
            return new PlayerLink(res.getString(1), res.getString(2), res.getString(3), gson.fromJson(res.getString(4), PlayerSettings.class));
        }
        return null;
    }

    @Override
    public PlayerLink[] getAllLinks() {
        final ArrayList<PlayerLink> links = new ArrayList<>();
        try {
            final Statement statement = connection.createStatement();
            final ResultSet res = statement.executeQuery("SELECT id,mcuuid,fluuid,settings FROM PlayerLinks");
            statement.closeOnCompletion();
            while (res != null && res.next()) {
                if (res.wasNull())
                    return links.toArray(new PlayerLink[0]);
                links.add(new PlayerLink(res.getString(1), res.getString(2), res.getString(3), gson.fromJson(res.getString(4), PlayerSettings.class)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return links.toArray(new PlayerLink[0]);
    }
}
