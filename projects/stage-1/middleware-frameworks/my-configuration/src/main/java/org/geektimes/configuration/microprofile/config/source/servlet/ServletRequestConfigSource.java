/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import static java.lang.String.format;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * {@link ServletRequest} {@link ConfigSource}
 */
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
			configData.put(parameterName, servletRequest.getParameter(parameterName));
		}
	}
}
