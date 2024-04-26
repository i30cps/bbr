package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class GildedBlackstone {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.GILDED_BLACKSTONE)};
    public final static String[] counters = {"GoldBlock", "SoulTorch"};
    public final static Move move = new Move(new Structure(structure), "GildedBlackstone", new String[]{"Normal attack (2♥ damage).", "Countered by Gold Block.", "Countered by Soul Torch."}, counters, Util.emptyStringArr) {

        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}