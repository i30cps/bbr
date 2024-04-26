package rotator.block.brawls.gameflow.Moves.AllMoves;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rotator.block.brawls.Util.BlockInfo;
import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Structure;

public class DuckCombo {
    public final static BlockInfo[] DUCK = {new BlockInfo(0, 0, 0, Material.BLACK_BANNER), new BlockInfo(0, 1, 0, Material.ORANGE_CARPET)};
	public final static Move duck = new Move(new Structure(DUCK), "Duck", new String[]{"Place Banner, then carpet on top.", "Will launch opponent into air.", "Up to 3♥ of damage."}) {
		@Override
		public void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
			for (Player p : game.getPlayers()) {
				p.sendMessage(ChatColor.YELLOW + "Quack!");
			}
			game.getOtherPlayer(fromCounterableMoveQueue).setVelocity(new org.bukkit.util.Vector(0, 1.22, 0));
			if (!fromCounterableMoveQueue) game.completeTurn();
		}

	};
}
