package rotator.block.brawls.gameflow.Moves;

import java.util.HashMap;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.Util.Utility;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.AllMoves.*;

public class Moves {
	public static HashMap<String, Move> stringToMove = new HashMap<>();

	public final static BlockInfo[] WOOD_WALL = {new BlockInfo(0, 0, 0, Material.OAK_PLANKS), new BlockInfo(0, 1, 0, Material.OAK_PLANKS),
			new BlockInfo(1, 0, 0, Material.OAK_PLANKS), new BlockInfo(1, 1, 0, Material.OAK_PLANKS)};
	
	public final static Move woodWall = new Move(new Structure(WOOD_WALL), "WoodWall", new String[]{"Healing item (For now)", "Place in a 2x2 square.", "Will heal 2.5♥ damage."}) {
		@Override
		public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
			game.getActivePlayer(fromCounterableMoveQueue).setHealth(Utility.min(game.getActivePlayer(fromCounterableMoveQueue).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(),
				game.getActivePlayer(fromCounterableMoveQueue).getHealth() + 5));
			
			if (!fromCounterableMoveQueue) game.completeTurn();
		}
	};

	public final static Move[] allUnrotatedMoves = {
		woodWall,
		AblazeCombo.AblazeCombo, AmethystBlock.move, Calcite.move, Chain.move, DecoratedPot.move, Dripstone.move,
		DuckCombo.duck, EndPortalFrame.move, EndRod.move, EndStoneBricks.move, GildedBlackstone.move, GoldBlock.move,
		HayBale.move, MossBlock.move, Netherbricks.move, OakWood.move, Obsidian.move, PurpurBlock.move,
		RedNetherBrickWall.move, RedstoneTorch.move, ShroomLight.move, SmoothSandstone.move, SoulTorch.move,
		TargetBlock.move, Tuff.move, WarpedwartBlock.move
	};
	public static Vector<Move> allMoves = new Vector<Move>();
	
	public static void initialize() {


		for (String s : stringToMove.keySet()) {
			Bukkit.getLogger().info(s);
		}

		for (Move m : allUnrotatedMoves) {
			Bukkit.getLogger().info("Rotating for move " + m.name);
			allMoves.add(m);
			// Rotation
			BlockInfo[] arr = m.getStructure().getBlocks();
			int n = arr.length;
			for (int iteration = 0; iteration < 3; iteration++) {
				arr = arr.clone();
				for (int i = 0; i < n; i++) {
					BlockInfo currentBlock = arr[i];
					// Rotate 90 degrees by swapping x and z coordinates
					int newX = currentBlock.getZ();
					int newY = currentBlock.getY();
					int newZ = -currentBlock.getX(); // Negative sign to ensure a 90-degree rotation
					arr[i] = new BlockInfo(newX, newY, newZ, currentBlock.getMaterial());
				}

				allMoves.add(new Move(new Structure(arr), m.name, m.description) {
					@Override
					public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
						m.effect(game, placedAt, isCountering, fromCounterableMoveQueue);
					}
				});
			}
		}
	}
}
