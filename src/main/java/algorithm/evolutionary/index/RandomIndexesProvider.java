package algorithm.evolutionary.index;

import java.util.Collection;
import java.util.Random;

public class RandomIndexesProvider {

    private static final Random random = new Random();

    private final int indexFrom;
    private final int indexTo;

    private RandomIndexesProvider(int indexFrom, int indexTo) {
        this.indexFrom = indexFrom;
        this.indexTo = indexTo;
    }

    public static RandomIndexesProvider randomIndexRangeOf(Collection<Object> collection) {
        return randomIndexRangeOf(collection.size());
    }

    public static RandomIndexesProvider randomIndexRangeOf(int collectionSize) {
        int indexFrom = random.nextInt(collectionSize);
        int indexTo;

        do {
            indexTo = random.nextInt(collectionSize);
        } while (indexFrom == indexTo);

        if (indexFrom > indexTo) {
            int indexToSwap = indexFrom;
            indexFrom = indexTo;
            indexTo = indexToSwap;
        }

        return new RandomIndexesProvider(indexFrom, indexTo);
    }

    public int indexFrom() {
        return indexFrom;
    }

    public int indexTo() {
        return indexTo;
    }
}
