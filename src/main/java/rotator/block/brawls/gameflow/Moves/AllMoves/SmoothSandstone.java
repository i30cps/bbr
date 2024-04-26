package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class SmoothSandstone {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.SMOOTH_SANDSTONE)};
    public final static String[] counters = {"§6Decorated Pot", "EndRod"};
    public final static Move move = new Move(new Structure(structure), "§eSmooth Sandstone", new String[]{"Normal attack (2♥).", "Countered by §6Decorated Pot." , "Countered by §eEnd Rod."}, counters, Util.emptyStringArr) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}
