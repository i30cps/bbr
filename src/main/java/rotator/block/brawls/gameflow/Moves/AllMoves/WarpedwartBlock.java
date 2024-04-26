package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Moves;
import rotator.block.brawls.gameflow.Moves.Structure;

public class WarpedwartBlock {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.WARPED_WART_BLOCK)};
    public final static String[] willCounter = {"Shroomlight", "§5Obsidian"};
    public final static Move move = new Move(new Structure(structure), "WarpedWartBlock", new String[]{"Will counter Shroomlight", "Will counter Obsidian.", "Place ON TOP of it.", "Flips back 4♥ damage."}, Util.emptyStringArr, willCounter) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            if (isCountering) {
                for (String s : willCounter) {
                    if (placedAt.subtract(0,1,0).getBlock().getType().equals(Moves.stringToMove.get(s).getStructure().getBlocks()[0].getMaterial())) {
                        game.getOtherPlayer(fromCounterableMoveQueue).damage(8);
                        break;
                    }
                }
            }
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
    };
}