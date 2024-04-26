package rotator.block.brawls.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.gameflow.Queue;

public class joinqueue implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player ){
            Player p = (Player) sender;
            
            Game game = Game.playerToGame.get(p.getUniqueId());
            if (game != null) {
                p.sendMessage(ChatColor.RED + "bro you're already in a game");
                return true;
            }


            if (Queue.getQueue().queuelist.contains(p.getUniqueId())){
                sender.sendMessage(ChatColor.RED + "You are already in a queue");
                return true;
            }else{
                Queue.getQueue().queuelist.add(p.getUniqueId());
                p.sendMessage(ChatColor.GREEN + "Joined the queue!");

                if (Queue.getQueue().isReady()){
                    Player p1 = Bukkit.getPlayer(Queue.getQueue().queuelist.get(0));
                    Player p2 = Bukkit.getPlayer(Queue.getQueue().queuelist.get(1));

                    p1.playSound(p1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
                    p2.playSound(p2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);

                    Game.makeGame(p1, p2).start();
                    Queue.getQueue().queuelist.remove(p1.getUniqueId());
                    Queue.getQueue().queuelist.remove(p2.getUniqueId());
                }

            }
        }
        return true;
    }
}
