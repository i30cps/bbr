package rotator.block.brawls.Util;

public class IDGenerator {
	public static long nextId = 0;

    public static long generate() {
        nextId++;
        return nextId;
    }
}