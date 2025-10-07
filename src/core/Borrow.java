package core;

public record Borrow(int mediaId, int userId) implements Event {}
