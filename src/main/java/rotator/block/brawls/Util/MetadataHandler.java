package rotator.block.brawls.Util;

import org.bukkit.entity.Entity;

import rotator.block.brawls.Main;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class MetadataHandler {
    public static void setData(Entity entity, String key, Object value) {
        MetadataValue metadataValue = new FixedMetadataValue(Main.getPl(), value);
        entity.setMetadata(key, metadataValue);
    }

    public static Object getData(Entity entity, String key) {
        for (MetadataValue metadataValue : entity.getMetadata(key)) {
            if (metadataValue.getOwningPlugin().equals(Main.getPl())) {
                return metadataValue.value();
            }
        }
        return null; // If the key is not found, return null or handle it accordingly
    }
    
    public static void removeData(Entity entity, String key) {
        entity.removeMetadata(key, Main.getPl());
    }
}
