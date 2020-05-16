package me.darkeyedragon.ganghouse.storage;

import me.jet315.houses.Core;
import me.jet315.houses.utils.files.HouseItem;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class StorageHandler {

    private final Connection connection;
    private final Core houseCore;

    public StorageHandler(Core houseCore) {
        this.houseCore = houseCore;
        connection = houseCore.getDb().getSQLConnection();
    }

    public void createColumnIfNotExist() throws SQLException {
        Statement statement;
        /*String checkIfExists = "SELECT * FROM (SELECT COUNT(*) AS CNT FROM pragma_table_info('Houses') WHERE name='wealth');";
        statement = connection.createStatement();
        ps = connection.prepareStatement("SELECT * FROM pragma_table_info('Houses')");
        ResultSet resultSet = statement.executeQuery("PRAGMA table_info('Houses');");*/
        //if (resultSet.getInt(0) == 0) {
        statement = connection.createStatement();
        statement.execute("ALTER TABLE Houses ADD COLUMN wealth REAL default 0");
        connection.close();
        //}
    }


    public CompletableFuture<Boolean> setWealth(UUID ownerUuid, double wealth) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE Houses SET wealth = ? WHERE UUID = ? ");
                ps.setDouble(1, wealth);
                ps.setString(2, ownerUuid.toString());
                System.out.println("Wealth set to: " +wealth);
                return ps.execute();
            } catch (SQLException exception) {
                throw new CompletionException(exception);
            }
        });
    }

    public CompletableFuture<Double> getWealth(UUID ownerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT wealth FROM Houses WHERE UUID = ? ");
                ps.setString(1, ownerUuid.toString());
                ResultSet resultSet = ps.executeQuery();
                return resultSet.getDouble(1);
            }catch (SQLException exception){
                throw new CompletionException(exception);
            }
        });
    }
    public CompletableFuture<Map<UUID, Double>> getWealthMap(int page, int itemsPerPage){
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<UUID, Double> wealthMap = new HashMap<>();
                PreparedStatement ps = connection.prepareStatement("SELECT uuid, wealth FROM Houses ORDER BY wealth LIMIT ?,?");
                ps.setInt(1, page*itemsPerPage);
                ps.setInt(2, itemsPerPage);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()){
                    wealthMap.put(UUID.fromString(resultSet.getString("uuid")), resultSet.getDouble("wealth"));
                }
                return wealthMap;
            }catch (SQLException exception){
                throw new CompletionException(exception);
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
