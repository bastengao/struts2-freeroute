# struts2-freeroute-plugin #

自由的映射路由，像 Spring MVC 那样

## 引用 ##

```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-freeroute-plugin</artifactId>
    <version>${version}</version>
</dependency>
```

## 例子 ##

struts.xml
```xml
<struts>
    <constant name="struts.freeroute.controllerPackage" value="com.example"/>
</struts>
```

com.example.BookController.java
```java
public class BookController{
    private int id;
    private Book book;

    @Route("/books/{id}")
    public String show(){
        book = bookDao.find(id);
        return "dispatcher:/book.jsp"
    }

    //setter, getter
}
```

webapp/book.jsp
```jsp
<!DOCTYPE html>
<html>
    <head>
        <title>show book</title>
    </head>
    <body>
        <h1>Hello book</h1>
    </body>
</html>
```

访问 "/books/13"， 则会显示 book.jsp 的内容


free mapping route just like Spring MVC

## Use ##

```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-freeroute-plugin</artifactId>
    <version>${version}</version>
</dependency>
```