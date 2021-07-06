### 作业：

参考`com.salesmanager.shop.tags.CommonResponseHeadersTag` 实现一个自定义的 Tag，将 Hard Code 的 Header 名值对，变为属性配置的方式。
- 实现自定义标签
- 编写自定义标签 tld 文件
- 部署到 Servlet 应用

### 作答：

`Week1Demo`为本次的Demo项目，其功能是一个简单的登录界面。当输入正确的用户名`admin`和密码`123456`，就会跳转到`Login successfully`页面。
- 工程中实现了自定义标签`HelloTag`，取标签body，然后直接显示在页面上，相当于`<span/>`的作用。文件位于`src\main\java\com\hebaojia\week1\tags\HelloTag.java`
```
package com.hebaojia.week1.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Hello Tag extension
 * This tag gets its body and print the context, kinda like <span/>
 *
 */
public class HelloTag extends SimpleTagSupport {
	StringWriter sw = new StringWriter();

	@Override
	public void doTag() throws JspException, IOException {
		getJspBody().invoke(sw);
		getJspContext().getOut().println(sw.toString());
	}
}
```
- 相对应的自定义标签tld文件位于`src\main\webapp\WEB-INF\custom.tld`
```
<taglib>
  <tlib-version>1.0</tlib-version>
  <jsp-version>2.0</jsp-version>
  <short-name>Example TLD with Body</short-name>
  <tag>
    <name>Hello</name>
    <tag-class>com.hebaojia.week1.tags.HelloTag</tag-class>
    <body-content>scriptless</body-content>
  </tag>
</taglib>
```
- 在工程中的jsp文件中添加引入tld文件的语句，并在`<body/>`中使用自定义的标签
```
<%@ taglib prefix="ex" uri="WEB-INF/custom.tld"%>
...
<ex:Hello>
	This is the login page!
</ex:Hello>
```
- 将工程导入到Eclipse中，然后右键工程，选择`Run As > Run on Server`，根据提示选择Tomcat 8.5，等Tomcat起来后就可以访问 http://localhost:8080/Week1Demo/login.jsp 
