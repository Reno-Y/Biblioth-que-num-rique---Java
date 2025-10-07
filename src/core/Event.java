package core;


import model.Media;

public sealed interface Event permits Borrow, NewMedia, PoisonPill, RemoveMedia, Return, UpdateCopies {}
