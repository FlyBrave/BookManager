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
* mybatis-config.xml（文件） MyBatis的配置文件，一般不做改动
* BookManagerApplication.java（文件） Spring Boot的入口，你搭好项目后可以运行一下试试看

### 下面是我们自己创建的文件夹：
* biz 用来存放比较复杂的逻辑
* configuration 用来放Spring Boot的代码配置
* controllers 控制器都在这里，也可以认为是网页的入口都在这
* dao 跟数据库交互的包，主要是MyBatis在这里编码
* interceptor AOP的代码都在这
* model 各种数据模型，对数据的描述
* service 一般用作对dao层的封装，建议稍复杂的逻辑全部放到biz包，而不是service
* utils 工具包，一般都是静态方法。

这里的核心主要是controllers、service、dao、model这四个包。

### 在model里面还有几个包：
* constants 放项目中的常量类（类里面全部是常量）
* enums 所有的枚举类
* exceptions 自定义的异常

## 好啦第一步我们就完成啦总结一下：
我们搭建好了项目的全部的框架，而且介绍了自动生成的，和我们自己生成的文件夹，可能
你现在对这些文件夹的具体作用还不是很熟悉，等你走完这整个项目之后，相信你能有一些
自己的体会。
**代码一定一定要自己敲才有用**

下一步：

    git checkout step-2-project-book-curd

# 第二步：完成图书的增删改查
    
    git checkout step-2-project-book-curd
在四个核心的包中（controllers、service、dao、model），我们分别创建了几个文件，这时
其他的包里面可能还是空的，先不用管它们。
### 最底层的是model包中的Book.java，它非常的简单，用来描述书本的基本属性。

``` java
    private int id;

    private String name;

    private String author;

    private String price;
```
代码的命名直接阐述了这个变量的含义，在你能做得到的时候请一定保持这种命名风格。
有时候这样做并不合适，比如你想起一个变量名来描述“墨西哥郊外一座靠近海岸的美丽城市”，
我的建议是：

``` java
    /*- 墨西哥郊外一座靠近海岸的美丽城市 -*/
    private String countryside;
    
    //而不是
    private String aBeautifulCountrysideOfMexico......
```
加个注释就好。这应该很容易理解，对吧？
### dao包中的BookDAO.java就是用来跟数据库打交道的

``` java
    @Insert({"insert into", table_name, "(", insert_field, ") values (#{name},#{author},#{price})"})
    int addBook(Book book);
```
比如上面这一段，相信你看着它也能猜出这个方法是干什么的吧？这里面包含了一段很简单的SQL语句，
如果你忘了数据库常用操作，去看看书复习一下吧~用不了多久。当然，如果你对其中的原理非常
感兴趣的话，建议去看官方文档或者源码（虽然可能很困难，但是这是想变成大佬的必经之路呀），
这里不会讲其中任何一个框架的实现原理，因为这些原理需要你自己去探索。

``` java
    String table_name = " book ";
    String insert_field = " name, author, price ";
    String select_field = " id, status, " + insert_field;
```
上面这段代码不用解释你也能感受到它的好处，将程序中不变的部分和需要变化的部分分开是设
计模式中的一项基本原则，这里很显然数据库表名、插入和选择范围都是基本不会变化的部分。
在命名方面，如果你有两个方法分别需要根据书的id和name去查找一本书，建议这样：

``` java
    ... //根据书本id选择
    Book selectBookById(int id);
    
    ... //根据书名选择
    Book selectBookByName(String name);
```
是不是一目了然呢。
### service包中的BookService.java对BookDAO.java做一些封装，你可以认为是一种代理（Proxy）模式
在BookService.java中，首先持有一个BookDAO的对象，这个对象由Spring自动帮你注入（@Autowired），
你不用亲自去实例化，Spring已经很聪明的帮你实例化了。你需要的是将BookDAO的方法“包装”
一下，供上层的类去调用。你一定很疑惑为什么要这么麻烦非得加一个什么包装（代理），
这里解释一下：
* 分层，将一些功能统一处理，比如

``` java
    public List<Book> getAllBooks() {
        try {
            return bookDAO.selectAll();
        } catch (Exception e) {
            logger.error(e);
            return null;
            /*- 或者抛出自定义的异常 -*/
        }
    }
```
这样，所有的异常都在service层被处理掉了，而且记录到了log中，上层调用的时候再也不用
关心底层的异常问题。这里只是举一个service层的例子，你可以做更多的事情。关于异常需要
说一句，千万不要静默的吞掉异常（就是在catch里面什么都不做，后者返回一个假数据），
不然排查问题的时候你将无从下手，如果你不知道怎么处理异常又非处理不可的时候，至少
先将异常记录在日志中吧！
* 多态、封装、重载

很显然多了一层你可以干更多的事情，而且能将DAO层的方法封装的更优雅一点，以至于上层
完全不知道你在跟数据库打交道。而且你可以有更多的操作空间，比如：
``` java
    //在BookDAO.java中有如下两个方法根据不同的条件查询一本书
    Book selectBookById(int id);
    Book selectBookByName(String name);
    
    //在对应的BookService.java中可以这么写
    Book getBook(int id) { return bookDAO.selectBookById(id); }
    Book getBook(String name) { return bookDAO.selectBookByName(name); }
```
你可能会问，好不容易在DAO层用不用的命名将不同的查询方法区分开，而在Service层中又
合并成了一个重载的方法名称getBook，那为什么不在DAO层就重载呢？这是一个很重要的问
题，你需要真正理解的是，不同的层关心的东西不一样，DAO层关心的就是跟数据库打交道，这样
所有的方法名都应该要尽量的去描述自己的功能；而Service层关心的是功能，就是说，
根据Name也好还是根据id也好在上层来看并没有区别，都是给我去取一本书来，我不用管你
是根据书的什么属性去取。在面向对象的编程中，慢慢养成面向对象思维才是最重要的，不然
就是用着java写着C。

#### 这里的service只做了最基本的包装，相信你能写的更漂亮！
### 真正的大佬Controller
有的同学有一个误区，认为Controller就是网页的入口，这是错误的。我们看看BookController.java
中的第一个方法：

``` java
    @RequestMapping(path = {"/index"}, method = {RequestMethod.GET})
    public String bookList(Model model) {
        loadAllBooksView(model);
        return "book/books";
    }
```
这是一个简单的controller方法，极其优雅的为我们展现了MVC框架：
* 第一行告诉了web什么样的url才能进入这个方法，
* 方法的主体部分：` loadAllBooksView(model); ` 告诉web如何处理和组装Model
* 最后` return "book/books"; `告诉web返回什么样的View
* 而这整段代码整体就是一个控制器Controller，它控制了怎么进入、怎么处理、怎么返回的所有操作。

根据这个简单的例子，你一定可以对MVC框架有一个深刻的体会。
### 讲讲 return "book/books"; 这句，return到哪去了？
在resources/templates包下你应该就能找到book/books.html，你是不是恍然大悟？
### 最后讲讲application.properties
我们在里面添加了一些内容，数据库的部分很简单，相信大家能看明白，
```
spring.freemarker.suffix=.html
```
这一句呢，作用就是让我们能保持习惯，将templates文件夹下的模板文件以.html结尾。
关于application.properties的设置还有很多，同样建议你去查看官方文档。

## 好了！你一定要自己敲代码！不然你的收获将非常有限！

下一步：
    
    git checkout step-3-project-user-login