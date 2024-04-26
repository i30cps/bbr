package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class TargetBlock {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.TARGET)};
    public final static String[] counters = {"RedstoneTorch", "§1End Portal Frame"};
    public final static Move move = new Move(new Structure(structure), "TargetBlock", new String[]{"Normal attack (2♥ damage).", "Countered by Redstone Torch.", "Countered by End Portal Frame."}, counters, Util.emptyStringArr) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}
