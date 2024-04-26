package rotator.block.brawls.statmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;
import rotator.block.brawls.Main;

@Deprecated
public enum Rank {
    
    Wood1(0, 250, "§f[" + ChatColor.of("d7ba89") + "Wood 1§f] "),
    Wood2(250, 500, "§f[§6Wood 2§f] "),
    Wood3(500, 750, "§f[§6Wood 3§f] "),
    Iron1(750, 1250, "§f[§6Leather 1§f] "),
    Iron2(1250, 1750, "§f[§6Leather 2§f] "),
    Iron3(1750, 2250, "§f[§6Leather 3§f] "),
    Gold1(2250, 750, "§f[§6Copper 1§f] "),
    Gold2(0, 750, "§f[§6Copper 2§f] "),
    Gold3(0, 750, "§f[§6Copper 3§f] ");

    public int low;
    public int high;
    public String prefix;

    private Rank(int lowestElo, int highestElo, String prefix) {
        this.low = lowestElo;
        this.high = highestElo;
        this.prefix = prefix;
    }

    public static Rank getRank(int elo) {
        return Rank.Wood1;
    }

    public static Scoreboard scorebaord = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void updateRank(Player p) {
        String staffPrefix = PlayerdataManager.getString(p, "perm-rank");
        String rankPrefix = PlayerdataManager.getString(p, "elo-rank");

        if (staffPrefix == null) staffPrefix = "";
        if (rankPrefix == null) {
            int elo = PlayerdataManager.getInt(p, "elo");
            
            rankPrefix = getRank(elo).prefix;
        }

        String prefix = staffPrefix + rankPrefix;

        Team team = null;

        for (Team t : scorebaord.getTeams()) {
            if (t.getPrefix().equals(prefix)) {
                team = t;
            }
        }

        if (team == null) {
            // create a team.
            team = scorebaord.registerNewTeam(Main.pl.getName() + prefix.replace("§", "&"));
            team.setPrefix(prefix);
        }

        team.addEntry(p.getName());
        p.setScoreboard(scorebaord);
    }
}