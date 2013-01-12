struts2-freeroute-plugin
===========

自由的映射路由，像 Spring MVC 那样。
[![Build Status](https://travis-ci.org/bastengao/struts2-freeroute.png?branch=master)](https://travis-ci.org/bastengao/struts2-freeroute)

- [API doc](http://bastengao.github.com/struts2-freeroute/1.0-SNAPSHOT/apidocs/)
- [引用](#引用)
- [例子](#例子)
- [配置](#配置)
- [说明](#说明)
    - [路由映射](#路由映射)
        * [路径](#路径)
        * [绑定 cookie](#绑定-cookie)
        * [HTTP method](#http-method)
        * [HTTP 参数](#http-参数)
    - [返回结果](#返回结果)
        * 方式
        * [绝对路径](#绝对路径)
        * [相对路径](#相对路径)

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

配置 Controller 所在的包(必须), 插件会在此包下查找相应的 Controller，包括子包。

struts.xml
```xml
<constant name="struts.freeroute.controllerPackage" value="org.example"/>
```

* struts.freeroute.controllerSuffixes

配置 Controller 的后缀(可选)，默认`Controller`。只解析 controllerPackage 下所有以 `Controller` 结尾的类。
可指定多个后缀，中间用逗号隔开。

struts.xml
```xml
<struts>
<!-- 将默认 Controller 改为 Action 和 Controller -->
<constant name="struts.freeroute.controllerSuffixes" value="Action, Controller"/>
</struts>
```

* struts.freeroute.defaultParentPackage

配置默认的父包(可选), 默认 `struts-defualt`

struts.xml
```xml
<struts>
    <constant name="struts.freeroute.defaultParentPackage" value="my-struts"/>

    <package name="my-struts" extends="struts-default">
    </package>
</struts>
```

* struts.freeroute.contentBase

配置全局的内容基路径，类型于 `@ContentBase`。如果返回结果中路径是相对地址，则通过内容基路径将其转换为绝对路径。
如果 controller 类上有 `@ContentBase` 注解则优先使用。

# 说明

配置`struts.freeroute.controllerPackage`后, 会在此包中默认搜索以`Controller`结尾的类做为 Controller,
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

注意：路径变量比参数的优先级高, 上面的例子中请求如果是 `http://HOST:PORT/users/1013/tags/free?name=never` , controller 中的 name 属性值将会是 `free` 而不是 `never`。

### 绑定 cookie

通过在属性上加 `@CookieValue` 注解绑定某个 cookie 的值

```java
//如果有 name 为 id 的 cookie 存在，那么将会把 cookie 的值绑定给 userId 属性
@CookieValue("id")
private String userId;

@Route("/users")
public String show(){
    //...
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

## 返回结果

`Controller.routeMethod` 方法的返回值将决定返回的结果类型和页面路径, 如`dispatcher:/example.html`, 类型为 `dispatcher`, 页面路径为 `/example.html`.

两种方式：

- 字面值

    例如 `dispatcher:/example.html`，这种方式只要返回 `type:location` 或者直接返回 `type`。

- DSL

    通过 `Results` 和 `Result` 等 DSL 方式的 api 构造返回结果。`Results` 能够满足常用返回结果。例如：

    * `Results.html("/example.html")`
    * `Results.jsp("/exmaple.jsp")`
    * `Results.json().includeProperties("value1, value2").done()`

    不过有时候 `Results` 未覆盖的情况，也可以通过 `Result` 来组织返回结果。例如：

    ```java
    Result.create("json")
        .param("includeProperties", "value1, value2")
        .done();
    ```


目前支持以下类型:

- dispatcher
    * html
    * jsp
- redirect
- chain
- httpHeader
- stream
- velocity
- freemarker
- json

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
@ContentBase("/very/long/page/path")
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