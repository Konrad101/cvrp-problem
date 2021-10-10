package model.truck;

public class InsufficientGoodsQuantityException extends RuntimeException {
    public InsufficientGoodsQuantityException(String message) {
        super(message);
    }
}
