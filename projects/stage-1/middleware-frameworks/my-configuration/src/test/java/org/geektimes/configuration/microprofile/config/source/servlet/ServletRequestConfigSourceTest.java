package org.geektimes.configuration.microprofile.config.source.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

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
			assert (parametersMap.containsKey(propertyName));
		});
	}
}
