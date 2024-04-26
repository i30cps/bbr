package rotator.block.brawls.gameflow;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Queue {
    
    public ArrayList<UUID> queuelist = new ArrayList<UUID>();
    private static Queue instance;


    public boolean isReady(){
        if (queuelist.size() == 2) return true;

        return false;
    }


    public void removePlayer(Player p){
        if (queuelist.contains(p.getUniqueId())){
            queuelist.remove(p.getUniqueId());
        }
    }

    public void addPlayer(Player p){
        if (p == null) return;
        queuelist.add(p.getUniqueId());
    }
    
    public static Queue getQueue(){
        if (instance == null){
            instance = new Queue();
        }
        return instance;
    }
}
