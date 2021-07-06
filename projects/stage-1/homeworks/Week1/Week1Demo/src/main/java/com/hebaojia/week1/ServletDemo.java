package com.hebaojia.week1;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletDemo
 */
@WebServlet(description = "This is my first servlet for homework in week1.", urlPatterns = { "/ServletDemo" })
public class ServletDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletDemo() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String str = "This is my first servlet for homework in week1.";
		response.getWriter().write(str);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String account = request.getParameter("account");
		String psd = request.getParameter("psd");
		if (account.equals("admin") && psd.equals("123456")) {
			request.getRequestDispatcher("result.jsp").forward(request, response);
		} else if (account.equals("admin") && !psd.equals("123456")) {
			request.setAttribute("psdErrorString", "Wrong password!");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else if (!account.equals("admin") && psd.equals("123456")) {
			request.setAttribute("accountErrorString", "Invalid account!");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {
			request.setAttribute("errorString", "login error");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

}
