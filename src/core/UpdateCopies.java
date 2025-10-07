package core;

public record UpdateCopies(int mediaId, int delta) implements Event {}
