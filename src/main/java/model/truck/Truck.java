package model.truck;

public class Truck {

    private final int maxCapacity;
    private int goodsAmount;

    public Truck(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.goodsAmount = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void load() {
        goodsAmount = maxCapacity;
    }

    public void unload(int amount) {
        if(amount > goodsAmount) {
            throw new InsufficientGoodsQuantityException(
                    "There are not enough goods in the truck, tried to unload " + amount + ", but truck had " + goodsAmount);
        }
    }

}
