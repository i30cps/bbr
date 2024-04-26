package rotator.block.brawls.Util;

import java.util.PriorityQueue;

public class GameManager {
    private PriorityQueue<Pair<Integer,Integer>> availableSlots = new PriorityQueue<>();
    private Pair<Integer,Integer> currentPlace = new Pair<Integer,Integer>(0, 0);

    public GameManager() {

    }

    public Pair<Integer,Integer> nextPair() {
        if (availableSlots.size() != 0) // if there is an available slot, then return it.
            return availableSlots.poll();
        // otherwise, let's increment currentPlace.
        // if first is 0, then will teleport
        if (currentPlace.first == 0) {
            currentPlace.first = currentPlace.second + 1;
            currentPlace.second = 0;
        } else {
            // if the second is not 0, then first-- and second++
            currentPlace.first--;
            currentPlace.second++;
        }

        return new Pair<Integer,Integer>(currentPlace.first, currentPlace.second);
    }

    public void removePair(Pair<Integer,Integer> pii) {
        availableSlots.add(pii);
    }
}