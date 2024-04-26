package rotator.block.brawls.Util;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import rotator.block.brawls.Main;

public class SchematicManager {
    public final static String BATTLE_BOARD = new File(Main.pl.getDataFolder(), "BattleBoard.schem").getAbsolutePath();
    public final static String EMPTY_BOARD = new File(Main.pl.getDataFolder(), "empty.schem").getAbsolutePath();

    public static void paste(String schematicFilePath, Location location) {
        File file = new File(schematicFilePath);

        if (!file.exists()) {
            Bukkit.getLogger().warning("Schematic file path " + schematicFilePath + " doesn't exist, please contact rotator.cf on discord for support regarding this issue!!!");
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        ClipboardReader reader = null;
        World world = FaweAPI.getWorld(location.getWorld().getName());

        if (format == null) return;

        try {
            reader = format.getReader(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reader == null) return;

        Clipboard clipboard = null;

        try {
            clipboard = reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clipboard == null) return;

        BlockVector3 to = BlockVector3.at(location.getX(), location.getY(), location.getZ());
        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).maxBlocks(32768).build()) {
            try (ClipboardHolder holder = new ClipboardHolder(clipboard)) {
                Operation operation = holder.createPaste(editSession).to(to).ignoreAirBlocks(false).build();
                Operations.complete(operation);
            }
        }
    }
}
