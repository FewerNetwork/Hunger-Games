package me.aymanisam.hungergames.handlers;

import me.aymanisam.hungergames.HungerGames;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Level;

public class DatabaseHandler {
    private final HungerGames plugin;
    private final ConfigHandler configHandler;

    private Connection connection;

    public DatabaseHandler (HungerGames plugin) {
        this.plugin = plugin;
        LangHandler langHandler = new LangHandler(plugin);
        this.configHandler = new ConfigHandler(plugin, langHandler);
    }

    public Connection getConnection() throws SQLException{
        if (connection != null) {
            return connection;
        }

        String url = configHandler.createPluginSettings().getString("database-url");
        String user = configHandler.createPluginSettings().getString("database-user");
        String password = configHandler.createPluginSettings().getString("database-password");

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, e.toString());
        }

        assert url != null;
        this.connection = DriverManager.getConnection(url, user, password);

        System.out.println("Connected to HungerGames Database");

        return this.connection;
    }

    public void initializeDatabase() throws SQLException{
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS player_stats(uuid char(36) primary key, username varchar(16), deaths int, kills int, killAssists int, gamesCreated int, gamesPlayed int, gamesWon int, chestsOpened int, supplyDropsOpened int, environmentDeaths int, borderDeaths int, arrowsShot int, arrowsLanded int, fireworksShot int, attacksBlocked int, healthRegenerated int, potionsConsumed int, foodConsumed int, totemsPopped int, credits double, damageDealt double, projectileDamageDealt double, damageTaken double, projectileDamageTaken double, soloPercentile double, teamPercentile double, lastLogin DATE, lastLogout DATE, secondsPlayed LONG, secondsPlayedMonth LONG)";
        statement.execute(sql);
        statement.close();

        System.out.println("Created the stats table in the database.");
    }

    public PlayerStatsHandler getPlayerStatsFromDatabase(Player player) throws SQLException {
        PlayerStatsHandler stats = this.plugin.getDatabase().findPlayerStatsByUUID(player.getUniqueId().toString());

        if (stats == null) {
            stats = new PlayerStatsHandler(player.getUniqueId().toString(), player.getName(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new java.util.Date(), new java.util.Date(), 0L, 0L);
            this.plugin.getDatabase().createPlayerStats(stats);
        }

        return stats;
    }

    public PlayerStatsHandler findPlayerStatsByUUID(String uuid) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);
        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String username = results.getString("username");
            int deaths = results.getInt("deaths");
            int kills = results.getInt("kills");
            int killAssists = results.getInt("killAssists");
            int gamesCreated = results.getInt("gamesCreated");
            int gamesPlayed = results.getInt("gamesPlayed");
            int gamesWon = results.getInt("gamesWon");
            int chestsOpened = results.getInt("chestsOpened");
            int supplyDropsOpened = results.getInt("supplyDropsOpened");
            int environmentDeaths = results.getInt("environmentDeaths");
            int borderDeaths = results.getInt("borderDeaths");
            int arrowsShot= results.getInt("arrowsShot");
            int arrowsLanded = results.getInt("arrowsLanded");
            int fireworksShot = results.getInt("fireworksShot");
            int attacksBlocked = results.getInt("attacksBlocked");
            int healthRegenerated = results.getInt("healthRegenerated");
            int potionsConsumed = results.getInt("potionsConsumed");
            int foodConsumed = results.getInt("foodConsumed");
            int totemsPopped = results.getInt("totemsPopped");
            double credits = results.getLong("credits");
            double damageDealt = results.getDouble("damageDealt");
            double projectileDamageDealt = results.getDouble("projectileDamageDealt");
            double damageTaken = results.getDouble("damageTaken");
            double projectileDamageTaken = results.getDouble("projectileDamageTaken");
            double soloPercentile = results.getDouble("soloPercentile");
            double teamPercentile = results.getDouble("teamPercentile");
            Date lastLogin = results.getDate("lastLogin");
            Date lastLogout = results.getDate("lastLogout");
            Long secondsPlayed = results.getLong("secondsPlayed");
            Long secondsPlayedMonth = results.getLong("secondsPlayedMonth");

            PlayerStatsHandler playerStats = new PlayerStatsHandler(uuid, username, deaths, kills, killAssists, gamesCreated, gamesPlayed, gamesWon, chestsOpened, supplyDropsOpened, environmentDeaths, borderDeaths, arrowsShot, arrowsLanded, fireworksShot, attacksBlocked, healthRegenerated, potionsConsumed, foodConsumed, totemsPopped, credits, damageDealt, projectileDamageDealt, damageTaken, projectileDamageTaken, soloPercentile, teamPercentile, lastLogin, lastLogout, secondsPlayed, secondsPlayedMonth);

            statement.close();

            return playerStats;
        } else {
            statement.close();
            return null;
        }
    }

    public void createPlayerStats(PlayerStatsHandler stats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO player_stats(uuid, username, deaths, kills, killAssists, gamesCreated, gamesPlayed, gamesWon, chestsOpened, supplyDropsOpened, environmentDeaths, borderDeaths, arrowsShot, arrowsLanded, fireworksShot, attacksBlocked, healthRegenerated, potionsConsumed, foodConsumed, totemsPopped, credits, damageDealt, projectileDamageDealt, damageTaken, projectileDamageTaken, soloPercentile, teamPercentile, lastLogin, lastLogout, secondsPlayed, secondsPlayedMonth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, stats.getUuid());
        statement.setString(2, stats.getUsername());
        statement.setInt(3, stats.getDeaths());
        statement.setInt(4, stats.getKills());
        statement.setInt(5, stats.getKillAssists());
        statement.setInt(6, stats.getGamesCreated());
        statement.setInt(7, stats.getGamesPlayed());
        statement.setInt(8, stats.getGamesWon());
        statement.setInt(9, stats.getChestsOpened());
        statement.setInt(10, stats.getSupplyDropsOpened());
        statement.setInt(11, stats.getEnvironmentDeaths());
        statement.setInt(12, stats.getBorderDeaths());
        statement.setInt(13, stats.getArrowsShot());
        statement.setInt(14, stats.getArrowsLanded());
        statement.setInt(15, stats.getFireworksShot());
        statement.setInt(16, stats.getAttacksBlocked());
        statement.setInt(17, stats.getHealthRegenerated());
        statement.setInt(18, stats.getPotionsConsumed());
        statement.setInt(19, stats.getFoodConsumed());
        statement.setInt(20, stats.getTotemsPopped());
        statement.setDouble(21, stats.getCredits());
        statement.setDouble(22, stats.getDamageDealt());
        statement.setDouble(23, stats.getProjectileDamageDealt());
        statement.setDouble(24, stats.getDamageTaken());
        statement.setDouble(25, stats.getProjectileDamageTaken());
        statement.setDouble(26, stats.getSoloPercentile());
        statement.setDouble(27, stats.getTeamPercentile());
        statement.setDate(28, new Date(stats.getLastLogin().getTime()));
        statement.setDate(29, new Date(stats.getLastLogout().getTime()));
        statement.setLong(30, stats.getSecondsPlayed());
        statement.setLong(31, stats.getSecondsPlayed());

        statement.executeUpdate();
        statement.close();
    }

    public void updatePlayerStats(PlayerStatsHandler stats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE player_stats SET username = ?, deaths = ?, kills = ?, killAssists = ?, gamesCreated = ?, gamesPlayed = ?, gamesWon = ?, chestsOpened = ?, supplyDropsOpened = ?, environmentDeaths = ?, borderDeaths = ?, arrowsShot = ?, arrowsLanded = ?, fireworksShot = ?, attacksBlocked = ?, healthRegenerated = ?, potionsConsumed = ?, foodConsumed = ?, totemsPopped = ?, credits = ?, damageDealt = ?, projectileDamageDealt = ?, damageTaken = ?, projectileDamageTaken = ?, soloPercentile = ?, teamPercentile = ?, lastLogin = ?, lastLogout = ?, secondsPlayed = ?, secondsPlayedMonth = ? WHERE uuid = ?");
        statement.setString(1, stats.getUsername());
        statement.setInt(2, stats.getDeaths());
        statement.setInt(3, stats.getKills());
        statement.setInt(4, stats.getKillAssists());
        statement.setInt(5, stats.getGamesCreated());
        statement.setInt(6, stats.getGamesPlayed());
        statement.setInt(7, stats.getGamesWon());
        statement.setInt(8, stats.getChestsOpened());
        statement.setInt(9, stats.getSupplyDropsOpened());
        statement.setInt(10, stats.getEnvironmentDeaths());
        statement.setInt(11, stats.getBorderDeaths());
        statement.setInt(12, stats.getArrowsShot());
        statement.setInt(13, stats.getArrowsLanded());
        statement.setInt(14, stats.getFireworksShot());
        statement.setInt(15, stats.getAttacksBlocked());
        statement.setInt(16, stats.getHealthRegenerated());
        statement.setInt(17, stats.getPotionsConsumed());
        statement.setInt(18, stats.getFoodConsumed());
        statement.setInt(19, stats.getTotemsPopped());
        statement.setDouble(20, stats.getCredits());
        statement.setDouble(21, stats.getDamageDealt());
        statement.setDouble(22, stats.getProjectileDamageDealt());
        statement.setDouble(23, stats.getDamageTaken());
        statement.setDouble(24, stats.getProjectileDamageTaken());
        statement.setDouble(25, stats.getSoloPercentile());
        statement.setDouble(26, stats.getTeamPercentile());
        statement.setDate(27, new Date(stats.getLastLogin().getTime()));
        statement.setDate(28, new Date(stats.getLastLogout().getTime()));
        statement.setLong(29, stats.getSecondsPlayed());
        statement.setLong(30, stats.getSecondsPlayedMonth());
        statement.setString(31, stats.getUuid());

        statement.executeUpdate();
        statement.close();
    }

    public void deletePlayerStats(String uuid) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);
        statement.executeUpdate();
        statement.close();
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, e.toString());
            }
        }
    }
}