package core;


import model.Media;

public sealed interface Event permits NewMedia, RemoveMedia, UpdateCopies, Borrow, Return {}
public record NewMedia(Media media) implements Event {}