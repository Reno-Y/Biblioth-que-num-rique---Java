package core;

import model.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<Event> queue;
    private final File csvFile;

    public Producer(BlockingQueue<Event> queue, File csvFile) {
        this.queue = queue;
        this.csvFile = csvFile;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isBlank()) continue;
                Event ev = parseLine(line);
                if (ev != null) queue.put(ev);
            }
            queue.put(new PoisonPill());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Parse une ligne CSV en Event
    private Event parseLine(String line) {
        String[] parts = line.split(";");
        String type = parts[0].trim();
        int id = Integer.parseInt(parts[1].trim());
        switch (type) {
            case "NEW" -> {
                String titre = parts[2].trim();
                String personne = parts[3].trim();
                String genre = parts[4].trim();
                // Par défaut on peut supposer que les NEW sont des Livres
                Media m = new LivreMedia(id, titre, personne, genre);
                return new NewMedia(m);
            }
            case "UPD" -> {
                int delta = Integer.parseInt(parts[5].trim());
                return new UpdateCopies(id, delta);
            }
            case "BOR" -> {
                int userId = Integer.parseInt(parts[6].trim());
                return new Borrow(id, userId);
            }
            case "RET" -> {
                int userId = Integer.parseInt(parts[6].trim());
                return new Return(id, userId);
            }
            case "REM" -> {
                return new RemoveMedia(id);
            }
            default -> {
                return null;
            }
        }
    }
}