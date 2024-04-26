package rotator.block.brawls.statmanager;

import java.util.List;

import org.bukkit.entity.Player;

import rotator.block.brawls.Util.YamlManager;

public class PlayerdataManager {
    public static YamlManager playerData;
    
    public static void initialize() {
        playerData = new YamlManager("playerdata.yaml");
    }

    public static List<String> getDeck(Player p, int deckNumber) {
        return playerData.yamlConfig.getConfigurationSection(p.getUniqueId().toString()).getConfigurationSection("deck").getStringList("" + deckNumber);
    }

    public static void setInt(Player p, String stat, int value) {
        playerData.yamlConfig.getConfigurationSection(p.getUniqueId().toString()).set(stat, value);
    }

    public static int getInt(Player p, String stat) {
        return playerData.yamlConfig.getConfigurationSection(p.getUniqueId().toString()).getInt(stat);
    }

    public static String getString(Player p, String stat) {
        return playerData.yamlConfig.getConfigurationSection(p.getUniqueId().toString()).getString(stat);
    }

    public static void setValue(Player p, String stat, Object value) {
        playerData.yamlConfig.getConfigurationSection(p.getUniqueId().toString()).set(stat, value);
    }
}
