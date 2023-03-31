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
2. What if we do not want to expose setters for some data fields? Encapsulation
#### So, we need to use Builder Pattern


