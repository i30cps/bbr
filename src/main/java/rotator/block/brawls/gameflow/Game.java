package rotator.block.brawls.gameflow;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import rotator.block.brawls.Main;
import rotator.block.brawls.Util.IDGenerator;
import rotator.block.brawls.gameflow.Moves.MoveDetector;
import rotator.block.brawls.gameflow.Moves.QueuedMove;
import rotator.block.brawls.statmanager.PlayerdataManager;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Game {
	public static HashMap<UUID,Game> playerToGame = new HashMap<>();

	private Player[] players = {null, null};
	private double[] luck = {0, 0};
	public QueuedMove[] counterableMoveQueue = {null, null};
	public int turn;
	private Player activePlayer = null, otherPlayer = null;
	private BukkitTask turnSkip;
	private long id;
	public boolean isAttackOut = false;
	public boolean finished = false;
	private boolean[] mustBeOnBoard = {false, false};
	public Board board;
	public Set<StatusEffect> statusEffects;

	public Game(Player player1, Player player2, long id) {
		playerToGame.put(player1.getUniqueId(), this);
		playerToGame.put(player2.getUniqueId(), this);

		this.players[0] = player1;
		this.players[1] = player2;
		this.id = id;

		this.board = new Board(this);
	}
	
	public Player[] getPlayers() {
		return this.players;
	}
	
	public Player getActivePlayer(boolean inQueue) {
		if (inQueue) return this.otherPlayer;
		return this.activePlayer;
	}
	
	public Player getOtherPlayer(boolean inQueue) {
		if (inQueue) return this.activePlayer;
		return this.otherPlayer;
	}
	
	public static Game makeGame(Player p1, Player p2) {
	    long id = IDGenerator.generate();
	    Game newGame = new Game(p1, p2, id);
	    Main.ongoingGames.put(id, newGame);
	    return newGame;
	}
	
	public boolean luckRandom(double baseChance, int turn) {
		Random rand = new Random();
		
		if (rand.nextDouble() + baseChance + this.luck[turn]/100.0 > 1) return true;
		return false;
	}
	
	public void updatePlayerLocation(Player p) {
		int t = (p.getUniqueId().equals(this.players[0].getUniqueId()) ? 0 : 1);

		Location loc = p.getLocation();

		// to check if player is on board: corner[0][0]+9.5 <= x <= corner[0][0] + 21.5, same for z
		if (board.coords[0][0] + 10.5 <= loc.getX() && loc.getX() <= board.coords[0][0] + 20.5 && board.coords[0][1] + 10.5 <= loc.getZ() && loc.getZ() <= board.coords[0][1] + 20.5) {
			if (!mustBeOnBoard[t]) mustBeOnBoard[t] = true;
		} else {
			if (mustBeOnBoard[t]) {
				players[t].sendMessage(ChatColor.RED + "You lost by stepping off the board! You must stay on the board at all times.");
				this.end(players[1-t]);
			}
		}
	}

	public void completeTurn() {

		this.turnSkip.cancel();
		
		// check if there is a counterablemove waiting. if so, run it.
		if (this.counterableMoveQueue[1 - this.turn] != null) {
			Bukkit.getLogger().info("Counterable Move " + this.counterableMoveQueue[1 - this.turn].move.name + " is running... (Active Player: " + this.getActivePlayer(true).getName() +")");
			this.counterableMoveQueue[1 - this.turn].run();
			this.counterableMoveQueue[1 - this.turn] = null;
		}

		if (this.finished) return; // necessary check in case the queued move ends the game

		startTurnSkipper();

		nextTurn(1 - this.turn);
	}

	
	private void startTurnSkipper() {
		Game self = this;
		this.turnSkip = new BukkitRunnable() {
			@Override
			public void run() {
				if (self.finished) this.cancel();

				// check for queued move(s)

				if (self.counterableMoveQueue[1 - self.turn] != null) {
					Bukkit.getLogger().info("Counterable Move " + self.counterableMoveQueue[1 - self.turn].move.name + " is running... (Active Player: " + self.getActivePlayer(true).getName() +")");
					self.counterableMoveQueue[1 - self.turn].run();
					self.counterableMoveQueue[1 - self.turn] = null;
				}

				if (self.finished) { // necessary check in case queued move ends game
					this.cancel();
					return;
				}

				self.players[self.turn].damage(2);
				self.players[self.turn].sendMessage("§cSince you didn't play a move in time, your turn has ended.");

				if (self.finished) { // necessary check in case the above damage ended the game
					this.cancel();
					return;
				}
				self.nextTurn(1 - self.turn);
			}
		}.runTaskTimer(Main.pl, 140L, 140L);
	}

	private void nextTurn(int t) {
		if (this.finished) return;

		this.turn = t;
		
		// prevent other player from doing anything
		this.players[1-t].setGameMode(GameMode.ADVENTURE);
		Main.cannotMove.add(this.players[1-t].getUniqueId());
		
		this.mustBeOnBoard[1-t] = true;
		
		// now, allow current player from doing those things
		this.players[t].setGameMode(GameMode.SURVIVAL);
		if (Main.cannotMove.contains(this.players[t].getUniqueId())) Main.cannotMove.remove(this.players[t].getUniqueId());
		
		this.players[t].sendMessage("§aIt's now your turn!");
		
		this.activePlayer = this.players[t];
		this.otherPlayer = this.players[1 - t];

		MoveDetector.clear(this.activePlayer);
	}
	
	public void start() {
		// randomly decide who goes first
		Random random = new Random();
		this.turn = random.nextInt(2);

		// set both players to adventure, and make them unable to move.
		for (Player p : this.players) {
			p.setGameMode(GameMode.ADVENTURE);
			Main.cannotMove.add(p.getUniqueId());
		}
		
		// board configuration stuff
		this.board.paste();
		
		Game self = this;

		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : self.players) {
					if (Main.cannotMove.contains(p.getUniqueId())) Main.cannotMove.remove(p.getUniqueId());
				}

				self.activePlayer = self.players[self.turn];
				self.otherPlayer = self.players[1 - self.turn];
				
				self.activePlayer.setGameMode(GameMode.SURVIVAL);
				self.otherPlayer.setGameMode(GameMode.ADVENTURE);
				
				self.activePlayer.sendMessage("§eYou go first!"); 
				self.otherPlayer.sendMessage("§e" + self.activePlayer.getName() + " is going first!"); 

				for (Player p : self.players) {
					p.setHealth(20);
					p.setSaturation(20);
				}
				
				// start the turn skipping every something time
				self.startTurnSkipper();
			}
			
		}.runTaskLater(Main.pl, 120L);

		new BukkitRunnable() {
			int iteration = 0;
			int maxIteration = 5;
			
			@Override
			public void run() {
				if (this.iteration == 2) {
					self.players[self.turn].teleport(new Location(self.players[self.turn].getWorld(), self.board.coords[0][0] + 15.5, 0, self.board.coords[0][1] + 7.5, 0, 0));
					self.players[1-self.turn].teleport(new Location(self.players[self.turn].getWorld(), self.board.coords[0][0] + 16.5, 0, self.board.coords[0][1] + 24.5, 180, 0));
				}

				if (this.iteration >= this.maxIteration) this.cancel();
				for (Player p : self.players) {
					p.sendMessage(ChatColor.YELLOW + "Game starts in " + (maxIteration-iteration));
				}

				iteration++;
			}
		}.runTaskTimer(Main.pl, 20L, 20L);
		
	}
	
	public void end() {
		this.finished = true;

		this.mustBeOnBoard[0] = false;
		this.mustBeOnBoard[1] = false;

		for (Player p : this.players) playerToGame.remove(p.getUniqueId());

		if (this.turnSkip != null) this.turnSkip.cancel();
		this.board.erase();

		for (Player p : this.players) {
			Bukkit.getLogger().info("removing cannotMove for player " + p.getName());
			if (Main.cannotMove.contains(p.getUniqueId())) Main.cannotMove.remove(p.getUniqueId());
			p.setGameMode(GameMode.ADVENTURE);
		}
		Main.ongoingGames.remove(this.id);
	}

	public void end(Player winner) {
		if (this.finished) return;

		Player loser = null;
		if (winner.getUniqueId().equals(this.players[0].getUniqueId())) loser = this.players[1];
		else loser = this.players[0];
		
		
		this.finished = true;
		
		PlayerdataManager.setInt(winner, "wins", PlayerdataManager.getInt(winner, "wins") + 1);
		PlayerdataManager.setInt(loser, "losses", PlayerdataManager.getInt(loser, "losses") + 1);

		for (Player p : this.players) {
			p.sendMessage("winner: " + winner.getName() + "; loser: " + loser.getName());
		}

		this.counterableMoveQueue[0] = null;
		this.counterableMoveQueue[1] = null;

		winner.sendMessage(ChatColor.GREEN + "You win!");
		for (Player p : this.players) {
			if (p != null && !p.getUniqueId().equals(winner.getUniqueId())) {
				p.setHealth(0);
				p.sendMessage(ChatColor.RED + "You lose.");
			} else if (p != null) {
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				p.setSaturation(20);
			}
		}

		winner.teleport(winner.getWorld().getSpawnLocation());

		this.end();
	}

	public void counterableMove(QueuedMove m) {
		this.counterableMoveQueue[this.turn] = m;
	}

	public QueuedMove[] getCounterableMoves() {
		return this.counterableMoveQueue;
	}
}
