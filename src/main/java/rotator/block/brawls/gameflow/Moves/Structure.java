package rotator.block.brawls.gameflow.Moves;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.Util.Utility;

import org.bukkit.Material;

public class Structure {
	private BlockInfo[] blocks;
	private int xSize, ySize, zSize;
	private int xMin = 2147483647, yMin = 2147483647, zMin = 2147483647;
	public HashMap<Material, Integer> matAmounts = new HashMap<>();
	
	public Structure(BlockInfo[] blocks) {
		this.blocks = blocks;
		
		for (BlockInfo i : blocks) {
			this.xSize = Utility.max(this.xSize, i.getX());
			this.ySize = Utility.max(this.ySize, i.getY());
			this.zSize = Utility.max(this.zSize, i.getZ());

			this.xMin = Utility.min(this.xMin, i.getX());
			this.yMin = Utility.min(this.yMin, i.getY());
			this.zMin = Utility.min(this.zMin, i.getZ());

			if (this.matAmounts.containsKey(i.getMaterial())) this.matAmounts.put(i.getMaterial(), this.matAmounts.get(i.getMaterial())+ 1);
			else this.matAmounts.put(i.getMaterial(), 1);
		}
	}
	
	public BlockInfo[] getBlocks() {
		return this.blocks;
	}

	public boolean canBuild(Player p) {
		PlayerInventory inv = p.getInventory();
		
		for (Material key : this.matAmounts.keySet()) {
			if (!inv.contains(key, this.matAmounts.get(key))) return false;
		}
		return true;
	}
	
	public int getX() {
		return this.xSize;
	}
	public int getY() {
		return this.ySize;
	}
	public int getZ() {
		return this.zSize;
	}

	public int getSizeX() {
		return this.xSize;
	}
	public int getSizeY() {
		return this.ySize;
	}
	public int getSizeZ() {
		return this.zSize;
	}

	public int getMinX() {
		return this.xMin;
	}
	public int getMinY() {
		return this.yMin;
	}
	public int getMinZ() {
		return this.zMin;
	}
	
	public boolean check(World world, int x, int y, int z) {
		for (BlockInfo b : this.blocks) {
			Block toCheck = world.getBlockAt(x + b.getX(), y + b.getY(), z + b.getZ());
			if (!toCheck.getType().equals(b.getMaterial())) {
				return false;
			}
		}
		return true;
	}

	public BlockInfo getBlock(){
		return blocks[0];
	}
	
	@Override
	public boolean equals(Object o) {
		Structure other;
		
		if (o instanceof Structure) other = (Structure) o;
		else return false;
		
		
		if (this.blocks.length != other.getBlocks().length) return false;
		
		for (BlockInfo b : other.getBlocks()) {
			boolean success = false;
			for (BlockInfo b2 : this.blocks) {
				if (b.equals(b2)) {
					success = true;
					break;
				}
			}
			if (!success) return false;
		}
		return true;
	}
}
