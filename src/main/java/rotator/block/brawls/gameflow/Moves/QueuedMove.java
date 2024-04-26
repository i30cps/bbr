package rotator.block.brawls.gameflow.Moves;

import org.bukkit.Location;

import rotator.block.brawls.gameflow.Game;

public class QueuedMove {
    public Move move = null;
    public boolean isCountering = false;
    public boolean fromCounterableMoveQueue = false;
    public Game game;
    public Location placedAt;


    public QueuedMove(Game game, Move m, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue) {
        this.game = game;
        this.move = m;
        this.isCountering = isCountering;
        this.fromCounterableMoveQueue = fromCounterableMoveQueue;
        this.placedAt = placedAt;
    }

    public void run() {
        this.move.effect(this.game, this.placedAt, this.isCountering, this.fromCounterableMoveQueue);
    }
}
