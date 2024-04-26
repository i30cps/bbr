package rotator.block.brawls.Util;

public class Pair<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Comparable<Pair<T1,T2>> {
    public T1 first;
    public T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public void setFirst(T1 first) {
        this.first = first;
    }

    public T2 getSecond() {
        return second;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?, ?> otherPair = (Pair<?, ?>) o;

        // Check for equality of both elements of the pair
        if (first != null ? !first.equals(otherPair.first) : otherPair.first != null) return false;
        if (second != null ? !second.equals(otherPair.second) : otherPair.second != null) return false;

        return true;
    }
    
    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Pair<T1, T2> o) {
        if (this.first.compareTo(o.first) == 0) return this.second.compareTo(o.second);
        else return this.first.compareTo(o.first);
    }
}
