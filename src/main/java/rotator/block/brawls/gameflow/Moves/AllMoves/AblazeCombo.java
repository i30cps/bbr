package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import rotator.block.brawls.Main;
import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class AblazeCombo {
    public final static BlockInfo[] ABLAZE_COMBO = {new BlockInfo(0, 0, 0, Material.RED_CONCRETE_POWDER), new BlockInfo(0, 0, 1, Material.CRIMSON_BUTTON)};
    public final static Move AblazeCombo = new Move(new Structure(ABLAZE_COMBO), "AblazeCombo", new String[]{"Place powder, then button on the side.", "Will spew out lots of fire.", "Fire is in opposite direction of button."}) {
        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            // first, determine the rotation.
            World world = game.getActivePlayer(fromCounterableMoveQueue).getWorld();
            // search around in all four directions.
            int[][] dirSearcher = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            for (int[] direction : dirSearcher) {
                // see if it's a crimson button
                int x = placedAt.getBlockX(), y = placedAt.getBlockY(), z = placedAt.getBlockZ();
                
                if (world.getBlockAt(x + direction[0], y, z + direction[1]).getType().equals(Material.CRIMSON_BUTTON)) {
                    
                    // send a fire wave in the opposite direction
                    /* BukkitTask fireSpreader = */ new BukkitRunnable() {
                        int iteration = 0, maxIteration = 8;

                        @Override
                        public void run() {
                            if (iteration >= maxIteration) {
                                this.cancel();
                            }
                            iteration++;
                            
                            int newX = x - iteration * direction[0], newZ = z - iteration * direction[1];
                            int xMod = direction[0] == 0 ? 1 : 0, zMod = direction[1] == 0 ? 1 : 0;

                            if (world.getType(newX, y, newZ).equals(Material.AIR))
                                world.setType(newX, y, newZ, Material.FIRE);
                            if (world.getType(newX + xMod, y, newZ + zMod).equals(Material.AIR))
                                world.setType(newX + xMod, y, newZ + zMod, Material.FIRE);
                            if (world.getType(newX - xMod, y, newZ - zMod).equals(Material.AIR))
                                world.setType(newX - xMod, y, newZ - zMod, Material.FIRE);

                        }
                    }.runTaskTimer(Main.pl, 15L, 3l);

                    new BukkitRunnable() {
                        int iteration = 0, maxIteration = 8;

                        @Override
                        public void run() {
                            if (iteration >= maxIteration) {
                                this.cancel();
                            }
                            iteration++;
                            
                            int newX = x - iteration * direction[0], newZ = z - iteration * direction[1];
                            int xMod = direction[0] == 0 ? 1 : 0, zMod = direction[1] == 0 ? 1 : 0;

                            if (world.getType(newX, y, newZ).equals(Material.FIRE))
                                world.setType(newX, y, newZ, Material.AIR);
                            if (world.getType(newX + xMod, y, newZ + zMod).equals(Material.FIRE))
                                world.setType(newX + xMod, y, newZ + zMod, Material.AIR);
                            if (world.getType(newX - xMod, y, newZ - zMod).equals(Material.FIRE))
                                world.setType(newX - xMod, y, newZ - zMod, Material.AIR);

                        }
                    }.runTaskTimer(Main.pl, 75L, 3l);
                }
            }
            if (!fromCounterableMoveQueue) game.completeTurn();
        }

    };
}
