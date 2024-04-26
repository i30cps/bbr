package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class Calcite {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.CALCITE)};
    public final static String[] counters = {"§8Chain", "§6Decorated Pot"};
    public final static Move move = new Move(new Structure(structure), "§fCalcite", new String[]{"Normal attack (2♥).", "Countered by §8Chain", "Countered by §6Decorated Pot."}, counters, Util.emptyStringArr) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}
