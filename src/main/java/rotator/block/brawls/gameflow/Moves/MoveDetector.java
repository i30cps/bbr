package rotator.block.brawls.gameflow.Moves;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import rotator.block.brawls.Util.Utility;

public class MoveDetector {
	public static HashMap<UUID, Location> lowestCorner = new HashMap<UUID, Location>();
	
	public static void clear(Player p) {
		if (lowestCorner.containsKey(p.getUniqueId())) lowestCorner.remove(p.getUniqueId());
	}
	
	public static QueuedMove addBlock(Player p, Block b) {
		UUID uuid = p.getUniqueId();
		if (lowestCorner.containsKey(uuid)) {
			Location loc = lowestCorner.get(uuid);
			int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
			lowestCorner.put(uuid, new Location(loc.getWorld(), Utility.min(x, b.getX()), Utility.min(y, b.getY()), Utility.min(z, b.getZ())));
		} else {
			lowestCorner.put(uuid, new Location(b.getWorld(), b.getX(), b.getY(), b.getZ()));
		}
		
		Location l = lowestCorner.get(p.getUniqueId());
		
		for (Move m : Moves.allMoves) {
			Structure struct = m.getStructure();
			int x = l.getBlockX() - struct.getMinX(), y = l.getBlockY() - struct.getMinY(), z = l.getBlockZ() - struct.getMinZ();
			if (struct.check(p.getWorld(), x, y, z)) {
				clear(p);
				return new QueuedMove(null, m, new Location(b.getWorld(), x, y, z), false, false);
			}
		}
		
		return null; // returns null if there is no structure
	}
}