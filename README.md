# euclidean

### Jackson Library
Manually parsing JSON is not efficient, and error-prone. It doesn’t impose type safety, you can put in anything for any field. A better way is having a model entity class, and use a library to perform the parsing. Jackson is one of such libraries. 
#### JsonProperty
Use @JsonProperty annotations to the class fields.
It indicates the mapping, the exact match is not required, but it’s required for multi-word snake case and camel case conversions, like release_time to releaseTime.


### Singleton - Builder Pattern
#### Reason to Use
1. Some fields may not be initialized in constructor.
2. Too many constructors (need many constructors for possible attribute combinations).
3. Very hard to use with a long parameter list.
#### What if using limited constructor, but with getter/setter?
There are two limitations:
1. Need to call many times to complete object creation, semantically not sure when an object is constructed completely.
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

### Presentation tier
It’s the tier that users can access directly. It provides the application’s user interface. For example, how to register, login, search, save favorite games in the browser and how to provide an easy way for users to send requests to the backend.
Language: HTML, CSS, Javascript

For our project, we are using react

### Logic tier
Maintain the business logic of the application, sitting between presentation tier and data tier, receive requests from presentation tier, make correct database operation, and return the final result back to the presentation tier.
Language: Java, Go, Python, …

We are using Spring in our project

### Data tier
Mostly we don’t need to create a database system ourselves, we just need to use an existing one like MySQL. What we need to do is tell the database system how to store our data. For example, what does each table look like, what’s the relationship between each other.
Language: SQL

We are using Hibernate framework to operate the database 

### What is MVC?
The Model-View-Controller (MVC) is an architectural pattern that commonly used for developing user interfaces that divide the related program logic into three interconnected elements

Model: The Model component corresponds to all the data-related logic that the user works with(管理这个模块中的数据)

View: The View component is used for all the UI logic of the application(主要用于显示数据和提交请求)

Controller: Controllers act as an interface between Model and View components to process all the business logic and incoming requests, manipulate data using the Model component and interact with the Views to render the final output(主要用作捕获并控制请求转发)

1. Web浏览器发送HTTP请求到服务端, 被Controller(Servlet)获取并进行处理（例如参数解析、请求转发）
2. Controller(Servlet)调用核心业务逻辑——Model部分, 获得结果
3. Controller(Servlet)将逻辑处理结果交给View(JSP), 动态输出HTML内容
4. 动态生成的HTML内容返回到浏览器显示

### What is Spring Web MVC?
The Spring MVC framework provides MVC architecture and components that can be used to develop flexible and loosely coupled web applications. It is designed around a DispatcherServlet that handles all the HTTP requests and responses and discovers the delegate components for actual work.

#### @Controller
Use @Controller to mark a class its role as a web component, so the spring mvc will register the methods which annotated the @RequestMapping.

#### @RequestMapping
Use the @RequestMapping annotation to define **REST API**, such as HTTP URL, method, etc.




