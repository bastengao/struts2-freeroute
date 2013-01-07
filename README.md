struts2-freeroute-plugin
===========

自由的映射路由，像 Spring MVC 那样

# 引用

当前版本还需要有完善的地方，暂时没有同步到中央仓库. 可以通过以下仓库引用最新版本(1.0-SNAPSHOT)。

```xml
<repository>
    <id>freeroute-snapshot</id>
    <name>Freeroute Snapshot Repository</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

当前最新版本 1.0-SNAPSHOT

```xml
<dependency>
    <groupId>com.bastengao.freeroute</groupId>
    <artifactId>struts2-freeroute-plugin</artifactId>
    <version>${version}</version>
</dependency>
```

# 例子

struts.xml
```xml
<struts>
    <constant name="struts.freeroute.controllerPackage" value="org.example"/>
</struts>
```

org.example.BookController.java
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

# 配置

* struts.freeroute.controllerPackage

配置 Controller 所在的包(必须)

struts.xml
```xml
<constant name="struts.freeroute.controllerPackage" value="com.example"/>
```

* struts.freeroute.defaultParentPackage

配置默认的父包(可选)

struts.xml
```xml
<struts>
    <constant name="struts.freeroute.defaultParentPackage" value="my-struts"/>

    <package name="my-struts" extends="struts-default">
    </package>
</struts>
```

# 说明

配置`struts.freeroute.controllerPackage`后, 会在此包中搜索以`Controller`结尾的类做为 Controller,
并在其中查找所有`@Route` 注解的方法做为路由映射.

## 路由映射

在`Controller`任意方法上加`@Route`注解则表示一条路由.

例如：
```java
public MyController{
    @Route("/helloworld")
    public String hello(){
        return "dispatcher:/world.html";
    }
}
```
### 路径

* 简单路径映射

`/helloworld` => `http://HOST:PORT/helloworld`
```java
@Route("/helloworld")
```

* 嵌套路径映射

`/nested/path/mapping` => `http://HOST:PORT/nested/path/mapping`
```java
@Route("/nested/path/mapping")
```

* 路径中包含变量(pathVariable)

变量可以是英文，数字，中文和 ` - _ ~ . ` 这四个标点符号(其他标点符号都不支持，参考[List_of_allowed_URL_characters](http://en.wikipedia.org/wiki/Uniform_resource_locator#List_of_allowed_URL_characters))。

`/users/{id}` => `http://HOST:PORT/users/1013`
```java
// id将会赋予{id}变量值
private int id;

@Route("/users/{id}")
public String user(){
    //...
}

// setter, getter
```

`/users/{id}/tags/{name}` => `http://HOST:PORT/users/1013/tags/free`
```java
// {id}
private int id;
// {name}
private String name

@Route("/users/{id}/tags/{name}")
public String tagedUser(){
    //....
}

// setter, getter
```

### HTTP method

通过`@Route.method`指定 HTTP method, 匹配满足 HTTP method 的路由映射

method 目前有以下类型:

* DELETE
* GET
* HEAD
* OPTIONS
* POST
* PUT
* TRACE
* NONE

`@Route.method`默认表示匹配任意一种 HTTP method.

只响应 POST 请求, `POST http://HOST:PORT/users`
```java
@Route(value = "/users", method = MethodType.POST)
```

或者只响应 GET 和 POST 请求, `GET|POST http://HOST:PORT/users`
```java
@Route(value = "/users", method = {MethodType.GET, MethodType.POST})
```

### HTTP 参数

通过`@Route.params`指定 HTTP param , 匹配满足 HTTP param 的路由映射

只响应带 'order' 参数的请求, `GET http://HOST:PORT/users?order=time`
```java
@Route(value = "/users", params = {"order"})
```

可以有多个参数, `GET http://HOST:PORT/users?order=time&page=13`
```java
@Route(value = "/users", params = {"order", "page"})
```

参数存在且等于某个值, `GET http://HOST:PROT/users?name=basten`
```java
@Rotue(value = "/users", params = {"name=basten"})
```

参数存在但不等于某个值, `GET http://HOST:PROT/users?name=basten` 将会产生 404，如果 name 是其他值则不会
```java
@Rotue(value = "/users", params = {"name!=basten"})
```

## 返回页面

`Controller.routeMethod` 方法的返回值将决定返回的结果类型和页面路径, 如`dispatcher:/example.html`, 类型为 `dispatcher`, 页面路径为 `/example.html`.

目前支持三种类型:

+ dispatcher
    * html
    * jsp
+ velocity
+ freemarker
+ redirect
+ json (待完善)

### 绝对路径

页面路径以 "/" 开始
```java
@Route("/very-long-page-path")
public String show(){
    return "dispatcher:/very/long/page/path/example.html";
}

@Route("/anothor-very-long-page-path")
public String show2(){
    return "dispatcher:/very/long/page/path/example2.html";
}
```

### 相对路径

页面路径不是以 "/" 开始，其地址相对于 @ContentBase 的路径
```java
@ContentBase("/very/long/page")
public class ExampleController{
    @Route("/very-long-page-path")
    public String show(){
        return "dispatcher:example.html";
    }

    @Route("/anothor-very-long-page-path")
    public String show2(){
        return "dispatcher:example2.html";
    }
}
```