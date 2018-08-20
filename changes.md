# 第一步：搭建项目框架
    git checkout step-1-project-framework
## 1.搭建框架
我们计划使用Maven来管理项目，并使用Spring Boot（SSM）作为我们项目的主体框架。
Spring Boot是目前最流行的开源框架，在[https://start.spring.io/](https://start.spring.io/)
上你可以轻松的挑选自己需要的模块。本项目中使用了Freemaker、Web、MyBatis、Aspect四个
模块，填写好GroupId和ArtifactId（ArtifactId使用BookManager）之后，下载到本地，用
你自己的IDE打开pom.xml文件，待pom.xml文件解析下载完毕后，我们的项目框架就搭建好了。
下面简单介绍一下我们使用到的几个模块：
### Freemaker
Freemaker是用来代替JSP的模板渲染工具，用来将Model中的数据渲染到View上。
### Web
Web组件本质上就是SpringMVC，用来控制整个MVC主体流程，部分同学将SpringMVC理解为Web
端的入口也是可以的。
### MyBatis
负责与数据库交互
### Aspect
Spring中重要的AOP组件，用来面向切面编程，多用在日志打印、权限认证等地方。

#### **如果同学们对任何地方有疑问一定要去百度、Google**

## 2.目录及已有文件解释
### 自动生成的文件：
* static（文件夹） 用来放css、font、img、js等静态文件
* templates（文件夹） 存放html文件以及模板文件
* application.properties（文件） Spring Boot的配置文件
* log4j.properties（文件） log4j的配置文件
* mybatis-config.xml MyBatis的配置文件，一般不做改动
* BookManagerApplication.java Spring Boot的入口，你搭好项目后可以运行一下试试看

### 下面是我们自己创建的文件夹：
* biz 用来存放比较复杂的逻辑
* configuration 用来放Spring Boot的代码配置
* controllers 控制器都在这里，也可以认为是网页的入口都在这
* dao 跟数据库交互的包，主要是MyBatis在这里编码
* interceptor AOP的代码都在这
* model 各种数据模型，对数据的描述
* service 一般用作对dao层的封装，建议稍复杂的逻辑全部放到biz包，而不是service
* utils 工具包，一般都是静态方法。

### 在model里面还有几个包：
* constants 放项目中的常量类（类里面全部是常量）
* enums 所有的枚举类
* exceptions 自定义的异常

## 好啦第一步我们就完成啦

下一步：
    git checkout step-2-project-book-curd
