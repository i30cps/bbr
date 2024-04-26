package rotator.block.brawls.gameflow.Moves;

import org.bukkit.Location;

import rotator.block.brawls.gameflow.Game;

public abstract class Move {
    private Structure structure;
    private String counteredBy[] = {};
    private String itCounters[] = {};
    public String name;
    public String[] description;

    public Move(Structure structure, String name, String[] description) {
        this.structure = structure;
        this.name = name;
        this.description = description;
        if (!Moves.stringToMove.containsKey(name)) Moves.stringToMove.put(name, this);
    }

    public Move(Structure structure, String name, String[] description, String[] counteredBy, String[] itCounters) {
        this.structure = structure;
        this.name = name;
        this.counteredBy = counteredBy;
        this.itCounters = itCounters;
        this.description = description;
        if (!Moves.stringToMove.containsKey(name)) Moves.stringToMove.put(name, this);
    }

    public String[] getCounters() {
        return this.counteredBy;
    }

    public String[] getCountered() {
        return this.itCounters;
    }

    public void setCounters(String[] c) {
        this.counteredBy = c;
    }

    public void setCountered(String[] c) {
        this.itCounters = c;
    }

    public Structure getStructure() {
        return structure;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Move) {
            Move otherMove = (Move) other;
            return otherMove.name.equals(this.name);
        }
        return false;
    }

    public abstract void effect(Game game, Location placedAt, boolean isCountering, boolean fromCounterableMoveQueue);
}
