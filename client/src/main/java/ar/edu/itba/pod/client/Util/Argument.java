package ar.edu.itba.pod.client.Util;

import java.time.LocalDateTime;

public class Argument {

    // Argumentos generales

    private final String[] addresses;

    private final String city;

    private final String inPath;

    private final String outPath;

    // Query 3

    private final int n;

    // Query 4

    //TODO - LocalDate?
    private final LocalDateTime from;
    private final LocalDateTime to;

    // GETTERS
    public String getCity() {
        return city;
    }

    public String getInPath() {
        return inPath;
    }

    public String getOutPath() {
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
    }

    public static class Builder {
        private String[] addresses;
        private String city;
        private String inPath;
        private String outPath;
        private int n;
        private LocalDateTime from;
        private LocalDateTime to;

        public Builder addresses(String[] addresses) {
            this.addresses = addresses;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder inPath(String inPath) {
            this.inPath = inPath;
            return this;
        }

        public Builder outPath(String outPath) {
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

        public Argument build() {
            return new Argument(this);
        }
    }

}
