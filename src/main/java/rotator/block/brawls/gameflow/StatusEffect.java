package rotator.block.brawls.gameflow;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import rotator.block.brawls.Main;

public abstract class StatusEffect implements Listener {
    public Game game;
    public Player owner;

    public StatusEffect(Game game, Player owner) {
        this.game = game;
        this.owner = owner;
        game.statusEffects.add(this);
        Bukkit.getPluginManager().registerEvents(this, Main.pl);
    }

    public void destroy() {
        game.statusEffects.remove(this);
        HandlerList.unregisterAll(this);
    }

    public abstract void onTurnEnd(int t);
}
