package com.hmy.euclidean.entity.response;

public class Game {
    private String name;
    private double price;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Game(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
    }

    public static class Builder {
        private String name;
        private double price;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
}