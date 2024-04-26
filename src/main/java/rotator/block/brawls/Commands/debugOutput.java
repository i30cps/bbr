package rotator.block.brawls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rotator.block.brawls.guis.DeckBuilderGUI;

public class debugOutput implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "you really think you could just debug without op");
			return true;
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			new DeckBuilderGUI(p);
		}
        return true;
	}

}
