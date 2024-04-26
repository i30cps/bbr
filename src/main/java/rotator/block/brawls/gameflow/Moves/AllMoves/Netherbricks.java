package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class Netherbricks{
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.NETHER_BRICKS)};
    public final static String[] counters = {"§4Red Nether Brick Wall", "§8Chain"};
    public final static Move move = new Move(new Structure(structure), "NetherBricks", new String[]{"Normal attack (2♥ damage).", "Countered by Red Nether Brick Wall.", "Countered by Chain."}, counters, Util.emptyStringArr) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}
