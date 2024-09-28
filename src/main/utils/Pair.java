package main.utils;

public record Pair<Left, Right>(Left left, Right right) {

    public Left first() { return left; }
    public Left key() { return left; }

    public Right second() { return right; }
    public Right last() { return right; }
    public Right value() { return right; }

}
