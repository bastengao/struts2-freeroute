struts2-freeroute 实现说明
========

参考 [plugin deveploper guide](https://cwiki.apache.org/WW/plugin-developers-guide.html)
，[plugins](https://cwiki.apache.org/WW/plugins.html)

# PackageProvider <-  ControllerPackageProvider

注册 PackageProvider, 应用启动时加载. 扫描分析 controller 和 路由。

# ActionMapper <= DefaultActionMapper

重写 ActionMapper, 拦截请求到 action 的映射. 先查找符合自己的映射，如果无则走之前默认逻辑。

# UnknowHandler <- DefaultUnknowHandler

动态处理 action 方法执行完后的 result, 分析返回类型, 通常映射到页面路径。