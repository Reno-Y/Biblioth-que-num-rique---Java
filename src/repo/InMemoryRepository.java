package repo;

import model.Media;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class InMemoryRepository<T extends Media> implements Repository<T> {

    private final Map<Integer, T> storage;
    private final ReadWriteLock lock;

    public InMemoryRepository() {
        this.storage = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void add(T media) {
        lock.writeLock().lock();
        try {
            storage.put(media.id(), media);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(int id) {
        lock.writeLock().lock();
        try {
            return storage.remove(id) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<T> findById(int id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(storage.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<T> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(storage.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<T> findBy(Predicate<? super T> predicate) {
        lock.readLock().lock();
        try {
            return storage.values().stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int count() {
        lock.readLock().lock();
        try {
            return storage.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean exists(int id) {
        lock.readLock().lock();
        try {
            return storage.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            storage.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}

