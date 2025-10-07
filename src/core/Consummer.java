package core;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<Event> queue;
    private final Catalogue catalogue;

    public Consumer(BlockingQueue<Event> queue, Catalogue catalogue) {
        this.queue = queue;
        this.catalogue = catalogue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Event ev = queue.take();  // attend si vide
                if (ev instanceof PoisonPill) {
                    break;
                }
                handleEvent(ev);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleEvent(Event ev) {
        if (ev instanceof NewMedia e) {
            catalogue.addMedia(e.media());
        } else if (ev instanceof RemoveMedia e) {
            catalogue.removeMediaById(e.mediaId());
        } else if (ev instanceof UpdateCopies e) {
            // TODO : mettre à jour le stock si Catalogue gère les copies
        } else if (ev instanceof Borrow e) {
            // TODO : enregistrer un emprunt (statistiques)
        } else if (ev instanceof Return e) {
            // TODO : enregistrer un retour
        }
    }
}