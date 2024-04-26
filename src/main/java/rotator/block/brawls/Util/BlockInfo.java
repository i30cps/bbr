package rotator.block.brawls.Util;

import org.bukkit.Material;

public class BlockInfo {
    public int x;
    public int y;
    public int z;
    private Material material;

    public BlockInfo(int x, int y, int z, Material material) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Material getMaterial() {
        return material;
    }
    
    @Override
    public boolean equals(Object other) {
    	BlockInfo o;
    	if (other instanceof BlockInfo) o = (BlockInfo) other;
    	else return false;
    	
    	if (this.x != o.getX()) return false;
    	if (this.y != o.getY()) return false;
    	if (this.z != o.getZ()) return false;
    	if (!this.material.equals(o.getMaterial())) return false;
    	
    	return true;
    }
}