package rotator.block.brawls.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class setVector implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;

        if (args.length != 4) return true;

        Player target = null;
        if (args[0] == "@p") {
            Location loc;
            if (sender instanceof CommandBlock) {
                loc = ((CommandBlock) sender).getLocation();
            } else if (sender instanceof Player) {
                loc = ((Player) sender).getLocation();
            } else {
                return true;
            }
            double closest = Double.MAX_VALUE;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().distance(loc) < closest) {
                    target = p;
                    closest = p.getLocation().distance(loc);
                }
            }
        } else target = Bukkit.getPlayer(args[0]);
        
        if (target == null) return true;

        double x = Double.parseDouble(args[1]), y = Double.parseDouble(args[2]), z = Double.parseDouble(args[3]);
        target.setVelocity(new Vector(x, y, z));
        return true;
    }
    
}
