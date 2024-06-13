package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.dataset.City;
import ar.edu.itba.pod.client.Query;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class Argument {

    // Argumentos generales

    private final String[] addresses;

    private final City city;

    private final Path inPath;

    private final Path outPath;

    // Query 3

    private final int n;

    // Query 4

    //TODO - LocalDate?
    private final LocalDateTime from;
    private final LocalDateTime to;

    private final Query query;

    // GETTERS
    public City getCity() {
        return city;
    }

    public Path getInPath() {
        return inPath;
    }

    public Path getOutPath() {
        return outPath;
    }

    public int getN() {
        return n;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    private Argument(Builder builder) {
        this.addresses = builder.addresses;
        this.city = builder.city;
        this.inPath = builder.inPath;
        this.outPath = builder.outPath;
        this.n = builder.n;
        this.from = builder.from;
        this.to = builder.to;
        this.query = builder.query;
    }

    public String[] getAddresses() {
        return addresses;
    }

    public Query getQuery() {
        return query;
    }

    public static class Builder {
        private String[] addresses;
        private City city;
        private Path inPath;
        private Path outPath;
        private int n;
        private LocalDateTime from;
        private LocalDateTime to;
        private Query query;

        public Builder addresses(String[] addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder city(City city) {
            this.city = city;
            return this;
        }

        public Builder inPath(Path inPath) {
            this.inPath = inPath;
            return this;
        }

        public Builder outPath(Path outPath) {
            this.outPath = outPath;
            return this;
        }

        public Builder n(int n) {
            this.n = n;
            return this;
        }

        public Builder from(LocalDateTime from) {
            this.from = from;
            return this;
        }

        public Builder to(LocalDateTime to) {
            this.to = to;
            return this;
        }

        public Builder query(Query query) {
            this.query = query;
            return this;
        }

        public Argument build() {
            return new Argument(this);
        }
    }

}
