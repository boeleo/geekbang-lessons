### 作业：

在 `my-configuration` 基础上，实现 `ServletRequest` 请求参数的 `ConfigSource（MicroProfile Config）`。

- 参考：`Apache Commons Configuration` 中的 `org.apache.commons.configuration.web.ServletRequestConfiguration`。


### 作答：
`ServletRequest` 请求参数的 `ConfigSource（MicroProfile Config）` 的实现类为`org.geektimes.configuration.microprofile.config.source.servlet.ServletRequestConfigSource`。

根据参考提示中提到的`org.apache.commons.configuration.web.ServletRequestConfiguration`可以发现Servlet请求的参数不一定是一个String，也可以是List。 借用小马老师实现好的`org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource`，实现如下
```
public class ServletRequestConfigSource extends MapBasedConfigSource {

	private final ServletRequest servletRequest;

	public ServletRequestConfigSource(ServletRequest servletRequest) {
		super(format("ServletRequest from [host:%s] Parameters", servletRequest.getServerName()), 550);
		this.servletRequest = servletRequest;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void prepareConfigData(Map configData) throws Throwable {
		// 取所有参数名
		Enumeration<String> parameterNames = servletRequest.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			// 按照参数名取到参数值，并一个个插入configData
			String parameterName = parameterNames.nextElement();
			// 直接把String[]转为String
			configData.put(parameterName, Arrays.toString(servletRequest.getParameterValues(parameterName)));
		}
	}
}
```

创建了JUnit测试来测试`ServletRequestConfigSource`，此处借用Spring Framework里的`MockHttpServletRequest`来模拟一个servlet请求。实现如下：
添加依赖
```
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-test</artifactId>
	<scope>test</scope>
</dependency>
```
```
public class ServletRequestConfigSourceTest {
	// 准备测试数据
	private static String serverName = "www.example.com";
	@SuppressWarnings("serial")
	private static Map<String, Object> parametersMap = new HashMap<String, Object>() {
		{
			put("from", "baojia");
			put("to", "geekbang");
			put("say", "hello");
			put("testMultiValues", new String[]{"1", "2"});
		}
	};

	@Test
	public void test() {
		// 模拟Servlet请求
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServerName(serverName);
		request.setParameters(parametersMap);

		ServletRequestConfigSource configSource = new ServletRequestConfigSource(request);
		// 打印configSource信息
		System.out.println("ConfigSource Name: " + configSource.getName());
		System.out.println("ConfigSource Properties: " + configSource.getProperties());
		// 测试是否与测试数据相符
		assertEquals(configSource.getName(), String.format("ServletRequest from [host:%s] Parameters", serverName));
		assertNotNull(configSource.getPropertyNames());
		configSource.getPropertyNames().forEach(propertyName -> {
			System.out.println(String.format(" -- %s %s", propertyName, configSource.getValue(propertyName)));
			assert(parametersMap.containsKey(propertyName));
		});
	}
}
```

打印信息为：
```
ConfigSource Name: ServletRequest from [host:www.example.com] Parameters
ConfigSource Properties: {from=[baojia], say=[hello], to=[geekbang], testMultiValues=[1, 2]}
 -- from [baojia]
 -- say [hello]
 -- to [geekbang]
 -- testMultiValues [1, 2]
```