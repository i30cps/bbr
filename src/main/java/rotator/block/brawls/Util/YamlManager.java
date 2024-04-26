// class that manages 1 yaml file at a time.

package rotator.block.brawls.Util;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;

import rotator.block.brawls.Main;

public class YamlManager {
    private static File dataFolder = Main.pl.getDataFolder();
    private static Set<YamlManager> allManagers = new HashSet<YamlManager>();

    private File file;
    public YamlConfiguration yamlConfig;

    public YamlManager(String path) {
        file = new File(dataFolder, path);

        allManagers.add(this);

        reload();
    }
    
    public void save() {
        try {
            this.yamlConfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.yamlConfig = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveAll() {
        for (YamlManager ym : allManagers) {
            ym.save();
        }
    }
}