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
**你应该注意到了sql这个文件夹，别犹豫，把自己的数据库建立起来吧！**
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
在` BookService.java `中，首先持有一个` BookDAO `的对象，这个对象由Spring自动帮你注入（@Autowired），
你不用亲自去实例化，Spring已经很聪明的帮你实例化了。你需要的是将` BookDAO `的方法“包装”
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

如果你的代码都敲完啦，那么打开` localhost:8080/index `看看吧！

**tomcat已经被嵌入到内部了，大家不用操心怎么搭建tomcat。**

## 好了！你一定要自己敲代码！不然你的收获将非常有限！

下一步：
    
    git checkout step-3-project-user-login
    
# 第三步：增加用户User和T票Ticket

    git checkout step-3-project-user-login
    
经过上面的开发，相信你也掌握了Spring Boot以及SSM框架的基本用法。那么接下来我们要
完善用户登录和权限认证工能。

同样的，我们为用户和用户的凭证设计数据模型，根据模型创建相应的数据库表。并相应编写` User.java ` 和 ` Ticket.java `。
例如在` Ticket.java `文件中：

``` java
    /**
     * Created by nowcoder on 2018/08/04 下午3:41
     */
    public class Ticket {
    
        private int id;
    
        /**
         * 相绑定的userId
         */
        private int userId;
    
        /**
         * t票实体
         */
        private String ticket;
    
        /**
         * 过期时间
         */
        private Date expiredAt;
```
留下好的注释是一个好习惯，记住是好的注释，意味着你要避免添加无用的甚至是错误的注释，
那将比没有注释更加糟糕。留下创建人和时间也是不错的习惯，如果你在一个团队中，这样做
能方便别人找到作者，当然，在对别人的代码有比较大的改动的时候也应该留下姓名和更新日志。
有的时候你还需要给这个类做简要的说明，在对相对抽象的类上这种需要更加明显，你可以参考
java的JDK源码或者Spring源码的注释习惯。

你可能会问：Ticket的作用是什么？我更想问你：比如你已经登录了bilibili.com，当你点开
一段舞蹈视频的时候，服务器凭什么知道是你点开了舞蹈视频并将这段视频添加到你的历史记
录里面？在并发环境中可能还有其他人也点开了这个视频。

如果你打开浏览器的Cookie列表你就会明白了（你不会要问怎么看浏览器的Cookie吧？），
当你登录的时候，服务器就会生成一张凭证，这张凭证是你专属的，不会给别人。服务器将这张凭
证写到服务器本地的数据库中，同时随着你的登录写进你的浏览器里面，只要你用这个浏览器去访问bilibili.com，
这个Cookie就会随着你的请求发送到服务器，，服务器就会去找之前写进浏览器的Cookie并去数据库找拿着这张票的人，就是你啦，它就知道了
是谁又在看舞蹈视屏呢~！

好的，到这里就解释了` Ticket.java `类的作用和用法，你可以想象一下，接下来我们会怎么
用这个类呢？

这里我们没有动Controller，所以你之前写的Book CURD应该是不受影响的。

### 请你自己完成User和Ticket的DAO层和Service层。

下一步

    git checkout step-4-project-login-biz

# 第四步：完成Login的主要逻辑-LoginBiz.java

    git checkout step-4-project-login-biz
    
经过上面的铺垫，我们已经写好了User和Login的底层代码。现在我们需要组织我们已经写好
的服务，将其组织为一个登陆逻辑。

## 我们增加了很多Utils类
先从简单的开始：
### MD5.java
这个类就是用来加密的。服务器不保存用户的明文密码是一项基本常识，所以我们用MD5来加密。
这里也不要专注于MD5的具体实现方法，这不是我们的主要任务，但建议你至少要知道MD5常用
在什么地方，并知道这个加密是不可逆的就可以了。
### UuidUtils.java
注意到Cookie都是一串无意义的码串，我们用JDK自带的UUID生成器可以非常方便的生成这样
一串随机的字符串。
### ConcurrentUtils.java
用来保存当前访问者的容器。我们知道，当web程序运行在web服务器中时，都是并发的
环境，拿tomcat来说，对于每一个请求tomcat都会从自己维护的线程池中选一个线程去处理这个
请求。` ThreadLocal `这个类提供了一种线程id到一个泛型的绑定，你可以认为它是一个Map，
当我们从里面取数据的时候，实际上是将当前的线程id作为map的key，取出之前这个线程存的
东西。这里我们将User保存在里面，这样我们就能随时在程序的任何地方拿出User信息了。
### CookieUtils.java
用来封装http请求中的Cookie的操作。
### TicketUtils.java
提供了一个生产Ticket的方法。

这些类封装了一些基本的常用的方法，供我们调用。

我们还封装了一个自己的Exception类：LoginRegisterException.java。

HostHolder是一个重要的类，用来包装ConcurrentUtils.java的方法，并交给Spring容器去管理，
使得我们可以在任何时候都能找当前的User，只要用户登录了，我们就将User信息设置到HostHolder
里面，这样我们就在其他地方可以直接拿出User来用。

## 重头戏LoginBiz
其中注册的逻辑跟登录的逻辑其实是比较相似的，登出也是简单的将用户的T票从数据库删除。
### 我们重点讲讲登录的逻辑
在这之前，简单介绍一下两个依赖包：

我们用了两个额外的依赖包，请见pom.xml文件，jodatime和common.lang3，jodatime提供了
更加好用的时间操作，用来代替java.util.date。common.lang3是一个经典的项目，封装了
一大批常用的工具类，我们主要使用StringUtils这个工具类，里面封装了大量常用的考虑了
null情况的String操作，不会引发String引起的NullPointerException。

``` java 
    /**
     * 登录逻辑，先检查邮箱和密码，然后更新t票。
     * @return 返回最新t票
     * @throws Exception 账号密码错误
     */
    public String login(String email, String password) throws Exception {
        User user = userService.getUser(email);
    
        //登录信息检查
        if (user == null)
            throw new LoginRegisterException("邮箱不存在");
        if(!StringUtils.equals(MD5.next(password),user.getPassword()))
            throw new LoginRegisterException("密码不正确");
    
        //检查ticket
        Ticket t = ticketService.getTicket(user.getId());
        //如果没有t票。生成一个
        if(t == null){
            t = TicketUtils.next(user.getId());
            ticketService.addTicket(t);
            return t.getTicket();
        }
        //是否过期
        if(t.getExpiredAt().before(new Date())){
            //删除
            ticketService.deleteTicket(t.getId());
        }
    
        t = TicketUtils.next(user.getId());
        ticketService.addTicket(t);
    
        ConcurrentUtils.setHost(user);
        return t.getTicket();
    }
```

登录方法两个参数用户email和password。密码检查的方法是将password散列，然后与数据库
中村的加密密码对比。如果用户登录成功后，就会去数据库找用户的t票，进行一系列检查后，
将t票更新为最新的t票，然后将用户信息加入ConcurrentUtils中，供HostHolder使用。最后
返回t票的内容。这个类里我们持有了之前封装的` UserService ` 和 ` TicketService `，
并直接用里面的方法很方便的操作数据库。

你看，是不是很简单。

看看我们还差什么？似乎只差一个controller了呢！

### 自己完成！你还可以做的更好

下一步：

    git checkout step-5-project-login-controller
    
# 第五步：完成Interceptor以及Controller

    git checkout step-5-project-login-controller
    
终于到了最后一步。
## 先看LoginController
与` LoginBiz.java `的逻辑一样，我们主要看` LoginController.java `中的doLogin方法。
这个方法里面完成了真正的登录逻辑：

``` java 
    @RequestMapping(path = {"/users/login/do"}, method = {RequestMethod.POST})
        public String doLogin(
            Model model,
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password
        ) {
        try {
            String t = loginBiz.login(email, password);
            CookieUtils.writeCookie("t", t, response);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        }
    }
``` 
我们直接调用了` loginBiz.login(email, password); `方法，这个方法里面抛出了我们自
定义的异常，记得用try catch包起来。这里对于异常的处理是返回一个自定义的404页面，
并在页面上渲染上出错的原因。

` CookieUtils.writeCookie("t", t, response); `这句将` loginBiz.login(email, password); `
方法返回的t票字符串放入Cookie中，当你成功后，可以在你的浏览器中查看是否成功写入了Cookie。
最后` return "redirect:/index"; `这里实际上是一个跳转，跳转到了/index对应的Action（一般把带有web入口的方法称为Action）。

## 这个方法还是挺简单的对吧？我们再看看BookController发生了什么变化。

``` java
    @RequestMapping(path = {"/index"}, method = {RequestMethod.GET})
    public String bookList(Model model) {
        User host = hostHolder.getUser();
        if (host != null) {
          model.addAttribute("host", host);
        }
        loadAllBooksView(model);
        return "book/books";
    }
```
首先我们持有了一个HostHolder的引用，同样这个引用由Spring实例化并为我们注入。我们
试图从hostHolder中取出当前线程对应的User，可以想象得到，如果用户没有登录的情况下，
host是没有的，也就是null。

## 对应的books.html也发生了一些变化：

``` html
    <#if host??>
		<table cellpadding="10">
			<tr>
			<td>${host.name}</td>
				<td><a href="/users/logout/do">退出登录</a></td>
			</tr>
		</table>
    <#else>
    <h5>未登录！<a href="/users/login">登陆/</a><a href="/users/register">注册</a></h5>
    </#if>
```
这一段可以看到，如果没有host的model的话，就显示“未登录”，并提供“登录/注册”按钮，
如果有host的话，显示当前用户名和退出登录按钮。

## 思考一下，我们再登录的时候会写入host信息到HostHoder里面去，但是我们在操作图书而不是做登录操作的时候，怎么将host信息写到HostHoder中呢？
这时，我们希望在做所有对书本的操作之前，都进行一次t票的认证操作，即通过t票找到t票对应的用户，并将用户信息放入HostHoder中。
如果在所有方法前都加上这么一段，总会显得不那么优雅，因为我们要尽力避免去做重复的事情，这样也方便维护。

这时，我们的AOP登场了。使用AOP来进行权限认证工作再适合不过了。

## HostInfoInterceptor.java
这个拦截器总是返回true，所以无论怎么样这个拦截器都能通过。这个拦截器试图通过请求中的Cookie来寻找t票，
一旦寻找到t票并成功的从数据库中找到了对应的用户，就直接放入ConcurrentUtils中（别忘了ConcurrentUtils 和 HostHolder是一回事）。
这里解释了，为什么你在登录一次之后，再进行其他的操作时，服务器都能识别操作用户是谁，甚至你关闭浏览器之后再次打开也不用重新登录，
因为服务器跟你浏览器发送的请求中附带的Cookie对你的身份自动进行了认证。

如果没有找到host信息，也没关系，直接放行就行。

## LoginInterceptor.java
这里是真正的权限认证：

``` java
@Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {
    
        //没有t票，去登陆
        String t = CookieUtils.getCookie("t",request);
        if(StringUtils.isEmpty(t)){
            response.sendRedirect("/users/login");
            return false;
        }
    
        //无效t票，去登陆
        Ticket ticket = ticketService.getTicket(t);
        if(ticket == null){
            response.sendRedirect("/users/login");
            return false;
        }
    
        //过期t票，去登陆
        if(ticket.getExpiredAt().before(new Date())){
            response.sendRedirect("/users/login");
            return false;
        }
    
        return true;
  }
```

对于以上几种情况，都会直接跳转到登录页面。

## BookWebConfiguration.java 真正让拦截器生效的地方
注意，里面注册的顺序非常重要，同学你可以想一想如果顺序不同会怎么样。

` addPathPatterns ` 设置了对于什么url的请求拦截器会生效。

-----
## 大功告成！大家一定一定要自己亲自敲出自己代码。
