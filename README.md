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

IoC Example:
```
abstract class Grinder {
	
	public void grind() {

	}
}

// Expensive
class BurrGrinder extends Grinder {

}


abstract class Boiler {

	public void boil() {


	}
}


@Component
class DualBoiler extends Boiler {
	
}

@Component
class CoffeeMaker {

	private int count = 0;

	private int max = 10;

	@AutoWire
	private Grinder grinder;

	@AutoWire
	private Boiler boiler;

	public void makeCoffee() {

		grinder.grind();

		boiler.boil();
	}
}

/// main 

grinder = new BladeGrinder();

boiler = new DualBolier();

coffeeMakder = new CoffeeMaker(grinder, boiler) 


// post localhost:8080/make_coffee


// CoffeeMakerController
@Controller
class CoffeeMakerController {
	

    @AutoWire
    private CoffeeMaker coffeeMaker 


   	public CoffeeMakerController() {
   	   
   	}


   @RequestMapping(value = "/make_coffee", method = RequestMethod.POST)
   public void makeCoffee() {

   		coffeeMaker.makeCoffee();
   }
}


// CoffeeMakerController
@Controller
class OfficeController {
	

    @AutoWire
    private CoffeeMaker coffeeMaker 


   	public CoffeeMakerController() {
   	   
   	}

   @RequestMapping(value = "/make_coffee_in_office", method = RequestMethod.POST)
   public void makeCoffee() {

   		coffeeMaker.makeCoffee();
   }
}

// CoffeeMakerController
@Controller
class HomeController {
	

    @AutoWire
    private CoffeeMaker coffeeMaker 


   	public CoffeeMakerController() {
   	   
   	}
  @RequestMapping(value = "/make_coffee_at_home", method = RequestMethod.POST)
   public void makeCoffee() {

   		coffeeMaker.makeCoffee();
   }
}
```


- IoC Container
= [ApplicationContext](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)
  - Inversion of Control (IoC)在大部分情况下就是Dependency Injection
  - The IoC container will create the objects, wire them together, configure them, and manage their complete life cycle from creation till destruction. These objects are called Spring Beans.
  - The container gets its instructions on what objects to instantiate, configure, and assemble by reading the configuration metadata provided. The configuration metadata can be represented either by XML, Java annotations, or Java code

- How a Spring IOC container is created on the web application?
  - When Tomcat runs the **DispatcherServlet**, it will internally create a spring ioc container


- Bean
= Java Object
  - The Java object that is initialized and managed by Spring is called bean.

### How to configure a class as Bean in the Spring container?
- [XML based configuration](https://www.baeldung.com/spring-xml-injection)
- Annotation-based configuration

Annotation-based configuration using annotations on the relevant class, method or the field so that Spring can recognize and initialize them. @Component and @Autowired

// @Component -> class level

// @Autowired -> field or constructor method level

- Java-based configuration
  - Using Java based configuration allows you to write your Spring configuration without using XML. Java based configuration uses the @Configuration-annotated classes and @Bean-annotated methods.
  - Using @Configuration annotation indicates that Spring IoC container can use it as a source of Bean definitions. Using the @Bean tells Spring that the method will return an object which should be registered as a bean in the Spring container.
  [spring config](https://www.baeldung.com/spring-xml-vs-java-config)

### Why do we choose Spring?
- **Versatile**: We can integrate any technologies with spring, such as hibernate, structs, etc.
- **Modularity**: We've options to use the entire Spring framework or just the modules necessary. Moreover, we can optionally include one or more Spring projects depending upon the need.
- **End to End Development**: Spring supports all aspects of application development, Web aspects, Business aspects, Persistence aspects, etc, so we can develop a complete application using spring.

### Spring Summary:
1. 什么是dependency? 类的成员变量
2. 什么是IoC container？生成Bean实例的工厂, 并且管理容器中的Bean
3. 什么是dependency injection？容器将所需要的依赖注入到类中的过程
4. 什么是inversion of control? 不在主动创建依赖, 而是通过上游(Spring framework)来提供

### Apache HTTP Client
We’re developing our backend for Job service, but most often than not your service will also need data from other services. So you’ll be making RESTful requests from your services. In our case, the job information is not hosted by yourself, we fetch them from Twitch API.

Examples of how to use the HttpClient library:
Example 1:
```
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpGet httpGet = new HttpGet("http://targethost/homepage");
CloseableHttpResponse response1 = httpclient.execute(httpGet);


// https://hc.apache.org/httpcomponents-client-5.1.x/quickstart.html
// The underlying HTTP connection is still held by the response object
// to allow the response content to be streamed directly from the network socket.
// In order to ensure correct deallocation of system resources
// the user MUST call CloseableHttpResponse#close() from a finally clause.

try {
    System.out.println(response1.getStatusLine());
    HttpEntity entity1 = response1.getEntity();

    // do something useful with the response body
    // and ensure it is fully consumed
    EntityUtils.consume(entity1);
} finally {
    response1.close();
}

HttpPost httpPost = new HttpPost("http://targethost/login");
List <NameValuePair> nvps = new ArrayList <NameValuePair>();
nvps.add(new BasicNameValuePair("username", "vip"));
nvps.add(new BasicNameValuePair("password", "secret"));
httpPost.setEntity(new UrlEncodedFormEntity(nvps));
CloseableHttpResponse response2 = httpclient.execute(httpPost);

try {
    System.out.println(response2.getStatusLine());
    HttpEntity entity2 = response2.getEntity();

    // do something useful with the response body
    // and ensure it is fully consumed
    EntityUtils.consume(entity2);
} finally {
    response2.close();
}
```

Example 2:
```
CloseableHttpClient httpclient = HttpClients.createDefault();

try {
    HttpGet httpget = new HttpGet("http://httpbin.org/");
    System.out.println("Executing request " + httpget.getRequestLine());

    // Create a custom response handler
    ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        @Override
        public String handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };

    String responseBody = httpclient.execute(httpget, responseHandler);
    System.out.println("----------------------------------------");
    System.out.println(responseBody);
} finally {
    httpclient.close();
}
```

#### @JsonIgnoreProperties(ignoreUnknown = true) 
indicates that other fields in the response can be safely ignored. Without this, you’ll get an exception at runtime.

#### @JsonInclude(JsonInclude.Include.NON_NULL) 
indicates that null fields can be skipped and not included.

#### @JsonDeserialize 
indicates that Jackson needs to use Game.Builder when constructing a Game object from JSON strings.

### Database and Database Management System
#### What is a Database?
A database is an organized collection of data.

#### What is a Database Management System?
A database management system (DBMS) is a computer-software application that interacts with end-users, other applications, and the database itself to capture and analyze data. A general-purpose DBMS allows the definition, creation, querying, update, and administration of databases.

#### Why do we need a Database?
- We need to store some data set, a list of videos/games with id, name, address, and date. What will you do? Text File? Excel? The size of the list is large( > 1 million users).
- Add some constraints to some data, such as the ID of each user should be different.
- Create relations between different kinds of data, such as users saved some videos before.
- Quickly retrieve data based on a given condition, such as retrieving all videos favorited by Rick Sun.
- Quickly update or delete data based on given conditions, such as updating all favorite videos for Rick Sun.
- Need access control on the data, meaning only authorized users can have access to the data set.
- Allow multiple users access(add, search, update, delete) the data set at the same time.

A DBMS allows you to fulfill all requirements above easily.
### MySQL
MySQL is an open-source relational database management system (RDBMS).
#### Basic Concepts
- Table: a collection of attributions. Similar to what you’ve seen in an excel chart. Each column is an attribute of an entity, and each row is a record/instance of an entity.
- Row: a single, implicitly structured data item in a table
- Column: a set of data values of a particularly simple type, one for each row of the table
- Schema: the blueprint of how the table is constructed.
- SQL: a programming language that is used to communicate with the DBMS.

#### Tables for Euclidean Project
![Database Design Diagram](/mysql_tables.png)
- users - store user information.
- items - store item information.
- favorite_records - store user favorite history.

### Setup MySQL Instance on RDS
#### Create a Security Group for RDS
1. Go to http://aws.amazon.com, sign into your account and then open the EC2 dashboard.
2. Under the EC2 dashboard, click the Security Group in the menu on the left.
3. Click the Create Security Group at the top of page.
4. In the Create Security Group popup window, choose a name for your group and then add a description for this group.
5. Click the Add Rule button to add a new rule for your group.
6. Choose MySQL/Aurora as the type of your rule and then make sure the port range is 3306 and the source is Anywhere.
7. Click the Create button to create your security group.

#### Create a MySQL database on RDS
1. Open the RDS dashboard, click Create database.
2. In the Engine options section, choose MySQL as your engine type.
3. In the Templates section, choose Free tier instead of Production for the free trial.
4. In the Settings section, enter a name for your database instance and then choose a password for the admin user of your database.
5. In the Connectivity section, select “Yes” for publicly accessible, and then add the security group you’ve just created under the VPC security group.
6. Finally, scroll down to the bottom of the page and click Create database. Then you should be able to see your MySQL DB instance running on RDS.

### JDBC Library
JDBC (Java Database Connectivity) provides interfaces and classes for writing database operations. Technically speaking, JDBC is a standard API that defines how Java programs access database management systems. Since JDBC is a standard specification, one Java program that uses the JDBC API can connect to any database management system (DBMS), as long as a driver exists for that particular DBMS.

[Official documentation of MySQL JDBC](https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-connect-drivermanager.html)

#### What’s the disadvantages of accessing the database via JDBC directly?
1. Too many columns need to be set, too much code change needed if we add more data later.
2. Error-prone approach to set data.
3. SQL adjustment when switching to a different DB.

Sample Code:
```
// String name = "Wang'; INSERT XXXX; ";
// String query = "Select * from Person where Person.LastName = '" + name+"'" ;
// String query = "Select * from Person where Person.LastName =  ? " ;
String insertItemSql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?)";
try {
PreparedStatement statement = conn.prepareStatement(insertItemSql);
statement.setString(1, item.getId());
statement.setString(2, item.getTitle());
statement.setString(3, item.getLocation());
statement.setString(4, item.getCompanyLogo());
statement.setString(5, item.getUrl());
statement.executeUpdate();
} catch (SQLException e) {
e.printStackTrace();
}
```

### ORM
#### What is ORM (Object-Relational Mapping)?
ORM is a technique to convert data between the object model and relational database is known as object-relational mapping.

Pro:
- DRY (Don’t Repeat Yourself): model code in one place, and reusable for different DBMS.
- Doesn’t require too much SQL knowledge. Code in your favorite language.
- Apply OOP knowledge.

Con:
- You have to learn it, and ORM libraries are not lightweight tools.
- Performance is OK for usual queries, but a SQL master will always do better with his own SQL for big projects.

#### What is Hibernate?
- Hibernate is an object-relational mapping tool for the Java programming language which implements the Java Persistence API.
- Hibernate's primary feature is mapping from Java classes to database tables, and mapping from Java data types to SQL data types. Hibernate provides the API for manipulating data.

[Java(TM) EE 8 Specification APIs](https://javaee.github.io/javaee-spec/javadocs/)
[JPA vs ORM vs Hibernate](https://stackoverflow.com/questions/27462185/jpa-vs-orm-vs-hibernate)
总而言之, JPA是ORM Java规范, hibernate是ORM Java实现.

Popular ORM libraries:
- Java: Hibernate, MyBatis, Room (Android)
- PHP: Propel or Doctrine.
- Python: the Django ORM or SQLAlchemy.
- Go: Gorm

For example:
```SQL
sql = "CREATE TABLE items ("
+ "item_id VARCHAR(255) NOT NULL,"
+ "name VARCHAR(255),"
+ "address VARCHAR(255),"
+ "image_url VARCHAR(255),"
+ "url VARCHAR(255),"
+ "PRIMARY KEY (item_id)"
+ ")";
```
```
@Entity
@Table(name = "items")
public class Item {
   @Id
   @Column(name = "item_id")
   private String id;

   private String address;
   
   private String url;

   private String name;

   @Column(name = "image_url")
   private String imageUrl;
}
```

#### Important annotations used for mapping
- javax.persistence.Entity: Used with model class to specify that it is an entity and mapped to a table in the database.
- javax.persistence.Table: Used with entity class to define the corresponding table name in the database.
- javax.persistence.Id: Used to define the primary key in the entity class.
- javax.persistence.Column: Used to define the column name in the database table.
- javax.persistence.OneToOne: Used to define the one-to-one mapping between two entity classes. We have other similar annotations as OneToMany, ManyToOne and ManyToMany
  - OneToOne: extended information, e.g, Customer and Cart.
  - ManyToOne: Foreign Key. It references the property from another Entity.
  - OneToMany: The other direction of foreign keys. List of referencing entities.
  - ManyToMany: Between two entities where one can have relations with multiple other entity instances, for example, item and user relationships on the first project.

#### Types
Basic type: Refer to [basic types](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#basic-provided).

#### Identifiers
Simple identifiers: Single basic attribute and denoted using @Id
```
@Entity
public class Item {
@Id
private String id;
}
```

According to JPA only [these types](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#identifiers-simple) can be used as identifier attribute types.


### Authentication (Login/Logout)
Why do we need authentication/authorization?
- Access control: user can only access data that is authorized to that user.
- Logging: record user-specific activity for book keeping, statistics, etc.

#### User Journey
- For an application, some resources can only be accessed by an authenticated user.
- Once a user is authenticated, the server uses a session to maintain his/her status. The session object is stored on the server-side, only session ID is returned to the client-side. Users need to provide a session ID to access resources that require authentication.
- When a user logs out, the session is destroyed. Next time a user comes, he/she has to authenticate again to get a new session.

### Recommendation System
Widely exists in industry and it is a popular interview question.
- Facebook: news feed, friends, ‘Do you know this friend?’
- LinkedIn: jobs, companies, acquaintances, ‘Are you interested in this job?’

推荐系统是用来干什么的? 已知Users对某些Items的评价, 判断用户对其它Item的兴趣. 把“User-Item”的打分矩阵中的缺失值补齐.

#### Content-based Recommendation
Key point: You will like people or things of similar characteristics.
- Given item profiles (category, price, etc.) of your favorite, recommend items that are similar to what you liked before. This is what we’ll use in our application. 
- Content-based method是不同于Item-based method的推荐系统设计思想. 它是通过物品”本身的特点”, 而不是“购买这个物品的用户的信息”, 来计算物品之间的相似度. 在系统没有足够数据的时候, 一般先使用content-based method.
- Example: Apple Music

#### Item-based Recommendation (collaborative filtering)
Filter based on the similarity of Items.

Item A is liked by User A, User B, User C.
Item B is liked by User B.
Item C is liked by User A, User B.
=> Item A and Item C are alike.
Item C is liked by users who like Item A.
=> Recommend it to user C (another user who likes Item A).

#### User-based Recommendation (collaborative filtering)
Filter based on the similarity of Users.

User A likes Item A, Item C.
User B likes Item B.
User C likes Item A, Item C, Item D.
=> User A shares the similar preference as User C compared to User B.
User C also likes Item D
=> User A may like Item D.


### Engineering Design
1. Given a user, get all the items (ids) this user has favorited.
- Set<String> itemIds = dao.getFavoriteItemIds(userId);
2. Given all these items, get their gameIds and sort by count.
- Map<String, List<String>> keywords = dao.getFavoriteGameIds(itemIds);
3. Given these gameIds, use Twitch API with , then filter out user favorited items.
- List<Item> items = gameService.searchByType(gameId, type);



