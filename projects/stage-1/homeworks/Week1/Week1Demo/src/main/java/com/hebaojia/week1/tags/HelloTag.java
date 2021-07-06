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
