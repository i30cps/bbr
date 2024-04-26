package rotator.block.brawls.gameflow;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import rotator.block.brawls.Util.GameManager;
import rotator.block.brawls.Util.Pair;
import rotator.block.brawls.Util.SchematicManager;

public class Board {

	private static GameManager gm = new GameManager();

	private static final Pair<Integer,Integer> lowestCorner = new Pair<Integer,Integer>(4096, 4096);
	private static final Pair<Integer,Integer> boardSize = new Pair<Integer,Integer>(32, 32); // do not touch this :)

    public Integer[][] coords = {{null ,null}, {null, null}};
    private Game game = null;
    private Pair<Integer,Integer> pii = new Pair<Integer,Integer>(null, null);

    public Board(Game game) {
        this.pii = gm.nextPair();
        int x = this.pii.first, y = this.pii.second;

        this.coords[0][0] = x * boardSize.first + lowestCorner.first;
        this.coords[0][1] = y * boardSize.second + lowestCorner.second;
        this.coords[1][0] = (x + 1) * boardSize.first + lowestCorner.first - 1;
        this.coords[1][1] = (y + 1) * boardSize.second + lowestCorner.second - 1;

        this.game = game;
    }

    public void paste() {
        Bukkit.getLogger().info("Sus baka (World: " + game.getPlayers()[0].getWorld().getName() + ", x: " + coords[0][0] + ", y=0, z: " + coords[0][1]);
        SchematicManager.paste(SchematicManager.BATTLE_BOARD, new Location(game.getPlayers()[0].getWorld(), coords[0][0], 0, coords[0][1]));
    }

    public void erase() {
        gm.removePair(this.pii);
        Bukkit.getLogger().info("Bus saka");
        SchematicManager.paste(SchematicManager.EMPTY_BOARD, new Location(game.getPlayers()[0].getWorld(), coords[0][0], 0, coords[0][1]));
    }
}
