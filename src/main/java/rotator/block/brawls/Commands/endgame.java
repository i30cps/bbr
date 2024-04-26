package rotator.block.brawls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import rotator.block.brawls.Main;
import rotator.block.brawls.gameflow.Game;

public class endgame implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender.isOp()) {
			for (long gameID : Main.ongoingGames.keySet()) {
				Game game = Main.ongoingGames.get(gameID);
				sender.sendMessage("Ending game: " + game.getPlayers()[0].getName() + " vs " + game.getPlayers()[1].getName());
				Main.ongoingGames.get(gameID).end();
			}
		}
		
		return true;
	}

}
