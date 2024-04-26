package rotator.block.brawls;

import java.net.http.WebSocket.Listener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import rotator.block.brawls.Commands.*;
import rotator.block.brawls.Util.*;
import rotator.block.brawls.gameflow.*;
import rotator.block.brawls.gameflow.Moves.*;
import rotator.block.brawls.guis.DeckBuilderGUI;
import rotator.block.brawls.statmanager.PlayerdataManager;


public class Main extends JavaPlugin implements org.bukkit.event.Listener{
    public ArrayList<Game> gamelist = new ArrayList<Game>();
    public static Set<UUID> cannotMove = new HashSet<UUID>();
	public static Set<DeckBuilderGUI> deckBuilderGUIs = new HashSet<DeckBuilderGUI>();

    public Queue currentQueue;

    public static HashMap<Long, Game> ongoingGames = new HashMap<>();

    public static JavaPlugin pl;
	public static JavaPlugin getPl() {
		return pl;
	}
	public static Main main;
	public static Main getMain() {return main;}

	public YamlManager playerinfo;

    @Override
    public void onEnable(){
        pl = this;
		main = this;

		Moves.initialize();
		PlayerdataManager.initialize();

        getCommand("queue").setExecutor(new joinqueue());
        getCommand("endgame").setExecutor(new endgame());
        getCommand("leavequeue").setExecutor(new leavequeue());
		getCommand("bbdebug").setExecutor(new debugOutput());
		getCommand("stats").setExecutor(new stats());
		getCommand("setvector").setExecutor(new setVector());
		getCommand("deck").setExecutor(new Deck());
		getCommand("duel").setExecutor(new DuelCommand());

        Bukkit.getPluginManager().registerEvents(this, this);
        
        getLogger().info("Block Brawls Up and Running");

		// datafolder stuff
		// first, check if datafolder doesn't exist. if so, create it
		if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();

		// TODO: get player info manager

		new BukkitRunnable() { // location updater

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					Game game = Game.playerToGame.get(p.getUniqueId());
					if (game != null) {
						game.updatePlayerLocation(p);
					}
				}
			}

		}.runTaskTimer(this, 5, 5);
		
    }

	@EventHandler
	public void onItemDrop (PlayerDropItemEvent e) {
		if (!e.getPlayer().isOp()) e.setCancelled(true); // if you're not op you can't drop items
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		// if the player stats have never been tracked, add it to playerdata manager.
		if (!PlayerdataManager.playerData.yamlConfig.contains(e.getPlayer().getUniqueId().toString())) {
			PlayerdataManager.playerData.yamlConfig.createSection(e.getPlayer().getUniqueId().toString());
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		for (DeckBuilderGUI gui : deckBuilderGUIs) {
			if (gui.p.equals(e.getWhoClicked())) gui.onInventoryClick(e);
		}
	}

	@EventHandler
	public void onInventoryDrag (final InventoryDragEvent e) {

		for (DeckBuilderGUI gui : deckBuilderGUIs) {
			if (gui.p.equals(e.getWhoClicked())) gui.onInventoryDrag(e);
		}
	}

	@EventHandler
	public void onInventoryClose (final InventoryCloseEvent e) {

		for (DeckBuilderGUI gui : deckBuilderGUIs) {
			if (gui.p.equals(e.getPlayer())) gui.onInventoryClose(e);
			return;
		}
	}

	@EventHandler
	public void onPlayerCraft(CraftItemEvent e) {
		if (!e.getWhoClicked().isOp()) e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {

		// if it's near spawn (1000 blocks), cancel it:
		if (e.getEntity() instanceof Player && e.getEntity().getLocation().distance(e.getEntity().getWorld().getSpawnLocation()) < 1000) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			e.setCancelled(true);
			return;
		} 
	}

	@Override
	public void onDisable() {
		YamlManager.saveAll();
	}

    @EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		Game game = Game.playerToGame.get(e.getPlayer().getUniqueId());

		if (game == null) return;

		// if the block is not in the board, cancel it
		int x = e.getBlock().getX(), z = e.getBlock().getZ();
		int boardX = game.board.coords[0][0], boardZ = game.board.coords[0][1];
		if (boardX + 12 > x || boardZ + 12 > z || boardX + 19 < x || boardZ + 19 < z) {
			e.setCancelled(true);
			return;
		}

		// now check for a move
		QueuedMove moveToRun = MoveDetector.addBlock(p, e.getBlock());

		if (moveToRun == null) return;

		Move m = moveToRun.move;

		p.sendMessage(ChatColor.GREEN + "You placed something.");

		// determine if we play the move immediately or we queue it
		// if the move is counterable, we queue it.
		boolean willBeQueued = false;

		for (String potentialCounter : m.getCounters()) {
			// can the opponent build the counter
			Bukkit.getLogger().info("Potential counter: " + potentialCounter);

			if (Moves.stringToMove.get(potentialCounter).getStructure().canBuild(game.getOtherPlayer(false))) {
				// if yes, it's going to be queued
				willBeQueued = true;
			}
		}

		// now, determine the move settings.
		// Setting - fromCounterableMoveQueue = willBeQueued
		// another setting - isCountering determines if it will be countering anything.
		
		boolean isCountering = false;
		// let's see if isCountering is true

		// game's current counterable move must be one of the moves that move m counters
		QueuedMove qm = game.counterableMoveQueue[1-game.turn];
		if (qm != null) {
			for (String potentialCounteredMoveName : m.getCountered()) {
				if (qm.move.name.equals(potentialCounteredMoveName)) {

					isCountering = true;
					game.counterableMoveQueue[1-game.turn] = null;
					
					getLogger().info(e.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType().toString());
					break;
				}
			}
		}
		getLogger().info("Move " + m.name + " played by " + game.getActivePlayer(false).getName() + " has following settings: willBeQueued: " + willBeQueued + ", isCountering: " + isCountering);
		moveToRun.game = game;
		moveToRun.isCountering = isCountering;
		moveToRun.fromCounterableMoveQueue = willBeQueued;
		// now that we have the settings, either run this move directly or append this move to the queue
		if (willBeQueued) {
			game.counterableMove(moveToRun);
			game.completeTurn();

			// if the player is located on top of the block, teleport them off.
			Location pLoc = game.getOtherPlayer(false).getLocation();
			
			double px = Math.round(pLoc.getX() * 100) / 100, pz = Math.round(pLoc.getZ() * 100) / 100;

			if (Utility.abs(b.getX() + 0.5 - px) <= 0.8 || Utility.abs(b.getZ() + 0.5 - pz) <= 0.8)
				p.teleport(new Location(b.getWorld(), b.getX()+1.65, pLoc.getBlockY(), b.getZ()));
			
		}
		else moveToRun.run();
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {

		/* 
		e.setCancelled(true);

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage("§7" + e.getPlayer().getName() + ": " + e.getMessage());
		}
		*/
	}

	@EventHandler
	public void onPlayerMove (PlayerMoveEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		Location to = e.getTo();
		Location from = e.getFrom();

		if (cannotMove.contains(uuid) && (from.getX() != to.getX() || from.getZ() != to.getZ())) 
			p.teleport(from);
		
		if (!p.isOp() && p.getLocation().distance(p.getWorld().getSpawnLocation()) < 1000) {
			p.setGameMode(GameMode.ADVENTURE);
		}
 	}

	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent e) {
		// go through all the games. if the player is a player in that game, they lose.
		Player p = e.getEntity();
		
		for (Long gameId : ongoingGames.keySet()) {
			Player[] gamePlayers = ongoingGames.get(gameId).getPlayers();
			if (p.getUniqueId().equals(gamePlayers[0].getUniqueId())) {
				ongoingGames.get(gameId).end(gamePlayers[1]);
				return;
			}
			else if (p.getUniqueId().equals(gamePlayers[1].getUniqueId())) {
				ongoingGames.get(gameId).end(gamePlayers[0]);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		if (Queue.getQueue().queuelist.contains(e.getPlayer().getUniqueId())){
			Queue.getQueue().queuelist.remove(e.getPlayer().getUniqueId());
		}

		Game game = Game.playerToGame.get(e.getPlayer().getUniqueId());
		if (game != null) { // if the player is in a game, then they lose.
			game.end((e.getPlayer().getUniqueId().equals(game.getPlayers()[0].getUniqueId())) ? game.getPlayers()[1] : game.getPlayers()[0]);
		}
	}

	@EventHandler
	public void onBlockBreak (BlockBreakEvent e) {
		// first, check if the player is in a game.
		// if the player is in a game, cancel it.
		// otherwise, check if the player is opped. cancel it if they aren't opped
		if (!e.getPlayer().isOp()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to break blocks!");
		}
		else {
			Game game = Game.playerToGame.get(e.getPlayer().getUniqueId());
			if (game != null) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "You can't break blocks right now!");
			}
		}
	}
}