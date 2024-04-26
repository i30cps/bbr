package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Moves;
import rotator.block.brawls.gameflow.Moves.Structure;

public class MossBlock {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.MOSS_BLOCK)};
    public final static String[] willCounter = {"§6Oak Wood", "§5Obsidian"};
    public final static Move move = new Move(new Structure(structure), "§2Moss Block", new String[]{"Counters §6Oak Wood.", "Counters §5Obsidian.", "Place ON TOP of it, and ", "it will flip back 4♥ damage."}, Util.emptyStringArr, willCounter) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            if (isCountering) {
                Material toBeCountered = placedAt.subtract(0, 1, 0).getBlock().getType();

                for (String s : willCounter) {
                    if (toBeCountered.equals(Moves.stringToMove.get(s).getStructure().getBlock().getMaterial())) {
                        game.getOtherPlayer(fromCounterableMoveQueue).damage(8);
                        break;
                    }
                }
            }
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
    };
}
