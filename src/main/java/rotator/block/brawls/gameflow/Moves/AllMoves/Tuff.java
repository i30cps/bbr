package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class Tuff {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.TUFF)};
    public final static String[] counters = {"§6Hay Bale", "§6Dripstone"};
    public final static Move move = new Move(new Structure(structure), "§8Tuff", new String[]{"Normal attack (2♥).", "Countered by §6Hay Bale.", "Countered by §6Dripstone."}, counters, Util.emptyStringArr) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}
