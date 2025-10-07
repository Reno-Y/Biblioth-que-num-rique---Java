package core;

import model.*;
import repo.InMemoryRepository;
import repo.Repository;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Catalogue {

    private final Repository<LivreMedia> livresRepository;
    private final Repository<Film> filmsRepository;
    private final Repository<Audio> audiosRepository;
    private final ReadWriteLock lock;

    public Catalogue() {
        this.livresRepository = new InMemoryRepository<>();
        this.filmsRepository = new InMemoryRepository<>();
        this.audiosRepository = new InMemoryRepository<>();
        this.lock = new ReentrantReadWriteLock();
    }

    // Constructeur pour injection de dépendances (tests)
    public Catalogue(Repository<LivreMedia> livresRepository,
                     Repository<Film> filmsRepository,
                     Repository<Audio> audiosRepository) {
        this.livresRepository = livresRepository;
        this.filmsRepository = filmsRepository;
        this.audiosRepository = audiosRepository;
        this.lock = new ReentrantReadWriteLock();
    }

    // === ACCESSEURS AUX REPOSITORIES ===

    public Repository<LivreMedia> getLivresRepository() {
        return livresRepository;
    }

    public Repository<Film> getFilmsRepository() {
        return filmsRepository;
    }

    public Repository<Audio> getAudiosRepository() {
        return audiosRepository;
    }

    // === MÉTHODES D'AJOUT ===

    public void ajouterLivre(LivreMedia livre) {
        lock.writeLock().lock();
        try {
            livresRepository.add(livre);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void ajouterFilm(Film film) {
        lock.writeLock().lock();
        try {
            filmsRepository.add(film);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void ajouterAudio(Audio audio) {
        lock.writeLock().lock();
        try {
            audiosRepository.add(audio);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // === MÉTHODES GÉNÉRIQUES AVEC WILDCARDS ===

    public List<? extends Media> rechercherTous(Predicate<? super Media> predicate) {
        lock.readLock().lock();
        try {
            return Stream.of(
                    livresRepository.findAll().stream(),
                    filmsRepository.findAll().stream(),
                    audiosRepository.findAll().stream()
            )
            .flatMap(stream -> stream)
            .filter(predicate)
            .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<? extends Media> rechercherParGenre(String genre) {
        return rechercherTous(media -> media.genre().equalsIgnoreCase(genre));
    }

    public List<? extends Media> rechercherParTitre(String titrePartiel) {
        return rechercherTous(media ->
            media.titre().toLowerCase().contains(titrePartiel.toLowerCase())
        );
    }

    public List<? extends Media> obtenirTousLesMedias() {
        lock.readLock().lock();
        try {
            List<Media> tousMedias = new ArrayList<>();
            tousMedias.addAll(livresRepository.findAll());
            tousMedias.addAll(filmsRepository.findAll());
            tousMedias.addAll(audiosRepository.findAll());
            return tousMedias;
        } finally {
            lock.readLock().unlock();
        }
    }

    public <T extends Media> List<T> trier(
            List<T> medias,
            Comparator<? super T> comparator) {
        lock.readLock().lock();
        try {
            return medias.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<? extends Media> trierParTitre() {
        lock.readLock().lock();
        try {
            return obtenirTousLesMedias().stream()
                    .sorted(Comparator.comparing(Media::titre))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<? extends Media> trierParGenreEtTitre() {
        lock.readLock().lock();
        try {
            return obtenirTousLesMedias().stream()
                    .sorted(Comparator.comparing(Media::genre)
                            .thenComparing(Media::titre))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Media> List<T> filtrerParType(Class<T> type) {
        lock.readLock().lock();
        try {
            return (List<T>) obtenirTousLesMedias().stream()
                    .filter(type::isInstance)
                    .map(type::cast)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public int compterTousLesMedias() {
        lock.readLock().lock();
        try {
            return livresRepository.count() +
                   filmsRepository.count() +
                   audiosRepository.count();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<String, Integer> obtenirStatistiques() {
        lock.readLock().lock();
        try {
            Map<String, Integer> stats = new LinkedHashMap<>();
            stats.put("Livres", livresRepository.count());
            stats.put("Films", filmsRepository.count());
            stats.put("Audios", audiosRepository.count());
            stats.put("Total", compterTousLesMedias());
            return stats;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<String, List<Media>> grouperParGenre() {
        lock.readLock().lock();
        try {
            return obtenirTousLesMedias().stream()
                    .collect(Collectors.groupingBy(
                        Media::genre,
                        Collectors.mapping(media -> media, Collectors.toList())
                    ));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void viderCatalogue() {
        lock.writeLock().lock();
        try {
            livresRepository.clear();
            filmsRepository.clear();
            audiosRepository.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<? extends Media> rechercherParId(int id) {
        lock.readLock().lock();
        try {
            Optional<LivreMedia> livre = livresRepository.findById(id);
            if (livre.isPresent()) return livre;

            Optional<Film> film = filmsRepository.findById(id);
            if (film.isPresent()) return film;

            return audiosRepository.findById(id);
        } finally {
            lock.readLock().unlock();
        }
    }
}
