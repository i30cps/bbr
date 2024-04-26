package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;

import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class PurpurBlock {
    public final static BlockInfo[] structure = {new BlockInfo(0, 0, 0, Material.PURPUR_BLOCK)};
    public final static String[] counters = {"§1End Portal Frame", "SoulTorch"};
    public final static Move move = new Move(new Structure(structure), "§5Purpur Block", new String[]{"Normal attack (2♥)", "Countered by §1End Portal Frame.", "Countered by §bSoul Torch."}, counters, Util.emptyStringArr) {
        @Override
        public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
            game.getOtherPlayer(fromCounterableMoveQueue).damage(4);
            if (!fromCounterableMoveQueue) game.completeTurn();
        }
        
    };
}
