# euclidean

### Jackson Library
Manually parsing JSON is not efficient, and error-prone. It doesn’t impose type safety, you can put in anything for any field. A better way is having a model entity class, and use a library to perform the parsing. Jackson is one of such libraries. 

### Singleton - Builder Pattern
#### Reason to Use
1. Some fields may not be initialized in constructor
2. Too many constructors (need many constructors for possible attribute combinations)
3. Very hard to use with a long parameter list
#### What if using limited constructor, but with getter/setter?
There are two limitations:
1. Need to call many times to complete object creation, semantically not sure when an object is constructed completely
2. What if we do not want to expose setters for some data fields? **Encapsulation**
#### So, we need to use Builder Pattern
e.g. use Builder Pattern to build a game entity
```Java
public class Game {
    private final String name; // required, and immutable
    private double price;

    private Game(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public static class Builder {
        private final String name; // required
        private double price;
        
        public Builder(String name) {
            this.name = name; // b/c name is required
        }
        
        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }
        
        public Game build() {
            return new Game(this);
        }
    }
    
    public static void main(String [] args) {
        Game game = new Game.Builder("Pokemon Go")
                            .setPrice(9.99)
                            .build();
    }
}
```
