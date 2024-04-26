package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Moves;
import rotator.block.brawls.gameflow.Moves.Structure;

public class RedstoneTorch {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.REDSTONE_TORCH)};
    public final static String[] willCounter = {"TargetBlock", "AmethystBlock"};
    public final static Move move = new Move(new Structure(structure), "RedstoneTorch", new String[]{"Counters Target Block.", "Counters Amethyst Block.", "Must be placed ON TOP of it;", "It will flip back 4♥ damage."}, Util.emptyStringArr, willCounter) {

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


