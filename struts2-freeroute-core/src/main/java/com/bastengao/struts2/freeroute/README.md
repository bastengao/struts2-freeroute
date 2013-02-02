struts2-freeroute 实现说明
========

参考 [plugin deveploper guide](https://cwiki.apache.org/WW/plugin-developers-guide.html)
，[plugins](https://cwiki.apache.org/WW/plugins.html)

## PackageProvider <-  ControllerPackageProvider

注册 PackageProvider, 应用启动时加载. 扫描分析 controller 和 路由。

## ActionMapper <= DefaultActionMapper

重写 ActionMapper, 拦截请求到 action 的映射. 先查找符合自己的映射，如果无则走之前默认逻辑。

## UnknowHandler <- DefaultUnknowHandler

动态处理 action 方法执行完后的 result, 分析返回类型, 通常映射到页面路径。

## RouteMappingHandler <- DefaultRouteMappingHandler

最核心的类，将前三个类联系起来。顾名思义路由映射处理哭，路由的映射统一由他来处理。

他有三个方法
```java
public void put(RouteMapping routeMapping, ActionConfig actionCfg);

public RouteMapping route(HttpServletRequest request);

public RouteMapping route(ActionConfig actionConfig);
```

其中 `ControllerPackageProvider` 将解析的路由交给(put) `RouteMappingHandler`,
`ControllerPackageProvider` 即为路由`RouteMapping` 的生产者。

`ActionMapper` 与 `UnknownHandler` 都是 `RouteMapping` 的消费都。
而 `ActionMapper` 则使用 `route(request)` 解析请求对应的 `RouteMapping`。
同样 `UnknownHandler` 使用 `route(actionConfig)` 得到对应的 `RouteMapping`。
