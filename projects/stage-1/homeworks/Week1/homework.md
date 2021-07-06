作业：参考`com.salesmanager.shop.tags.CommonResponseHeadersTag` 实现一个自定义的 Tag，将 Hard Code 的 Header 名值对，变为属性配置的方式。
# 实现自定义标签
# 编写自定义标签 tld 文件
# 部署到 Servlet 应用

作答：
`Week1Demo`为本次的Demo项目，其功能是一个简单的登录界面。当输入正确的用户名`admin`和密码`123456`，就会跳转到`Login successfully`页面。
# 工程中实现了自定义标签`HelloTag`，取标签body，然后直接显示在页面上，相当于`<span/>`的作用。文件位于`src\main\java\com\hebaojia\week1\tags\HelloTag.java`
# 相对应的自定义标签tld文件位于`src\main\webapp\WEB-INF\custom.tld`
# 将工程导入到Eclipse中，然后右键工程，选择`Run As > Run on Server`，根据提示选择Tomcat 8.5，等Tomcat起来后就可以访问 http://localhost:8080/Week1Demo/login.jsp 
