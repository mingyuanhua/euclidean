# euclidean


### Initial Design
![Initial Design Diagram](/BackendDesignDiagram1.png)

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
[Official link](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments)

#### @Controller
Use @Controller to mark a class its role as a web component, so the spring mvc will register the methods which annotated the @RequestMapping.

#### @RequestMapping
Use the @RequestMapping annotation to define **REST API**, such as HTTP URL, method, etc.

#### @RequestParam
use the @RequestParam annotation to bind Servlet request parameters (that is, query parameters) to a method argument in a controller.

For example, 
```http request
http://localhost:8080/search?lon=22&lat=37
```
If we would like to define a REST API via Spring MVC, we could define it as below
```
@RequestMapping(value = "/search", method = RequestMethod.GET)
public String search(@RequestParam("lon") double lon, @RequestParam("lat") double lat) {
    return "hello search";
}
```

#### @PathVariable
can be used to handle template variables in the request URI mapping.

For example: get menu for a specific restaurant
```
@RequestMapping(value = "/restaurant/{id}/menu", method = RequestMethod.GET)
public void searchMenu(@PathVariable(“id”) int id) {}
```

### Spring Framework
The Spring Framework is the most widely used framework for the development of **Java Enterprise Edition** applications, specifically, **web applications**. 

The [Spring Framework](https://spring.io/projects/spring-framework) is divided into modules. At the heart are the modules of the core container, including a configuration model and a dependency injection mechanism. The main usage of Spring core is to create all the objects (for example, objects that connect to database, business logic related class, controller, etc) that your application is required for running and dependency injection to achieve the loosely coupled.

### Spring Core
#### What is dependency?
The objects that the current class cooperates with.

For example:
```java
public interface Connection {
void connect();
}

public class MySqlConnection implements Connection {
}

public class OracleConnection implements Connection{
}

public class PaymentAction {
private Connection connection;

    public PaymentAction() {
        connection= new OracleConnection();
    }

    public void makePayment() {
       connection.connect();
    }
}
```

#### Dependency Injection/Inversion of Control
Instead of maintaining dependencies by the PaymentAction object, it can be injected by someone else(spring framework). This is called Dependency Injection. Here are two mainstream approaches as follows:

- Constructor Injection
```
@Autowired
public PaymentAction(Connection connection) {
    this.connection = connection;
}
```
- Field Injection
```
public class PaymentAction {
    @Autowired
    private Connection connection;
}
```

So, the example above can be converted to code below:
```
@Component
public class PaymentAction {
    private Connection connection;
    
    @Autowired
    public PaymentAction(Connection connection) {
        this.connection = connection;
    }

    public void makePayment() {
       connection.connect();
    }
}

@Component
public class MySqlConnection implements Connection {

    Public void connect() {
           // connect to MySQL
    } 
}
```
从PaymentAction的角度来看，它只是想使用connection，不在意其是如何创建的。PaymentAction希望someone(spring)把connection实例化之后在交给PaymentAction对象，就是用Dependency Injection来实现的.

依赖倒置原则（Dependence Inversion Principle）是程序要依赖于抽象接口，不要依赖于具体实现。

依赖注入，是指程序运行过程中，如果需要调用另一个对象协助时，无须在代码中创建被调用者，而是依赖于外部的注入。

- IoC Container
= [ApplicationContext](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)
  - Inversion of Control (IoC)在大部分情况下就是Dependency Injection
  - The IoC container will create the objects, wire them together, configure them, and manage their complete life cycle from creation till destruction. These objects are called Spring Beans.
  - The container gets its instructions on what objects to instantiate, configure, and assemble by reading the configuration metadata provided. The configuration metadata can be represented either by XML, Java annotations, or Java code

- Bean
= Java Object
  - The Java object that is initialized and managed by Spring is called bean.




