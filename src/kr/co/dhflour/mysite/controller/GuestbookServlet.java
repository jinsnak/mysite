package kr.co.dhflour.mysite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.dhflour.mysite.dao.GuestbookDao;
import kr.co.dhflour.mysite.vo.GuestbookVo;


@WebServlet("/guestbook")
public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding( "utf-8" );
		
		String actionName = request.getParameter( "a" );
		
		if( "add".equals(actionName) == true ) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String contents = request.getParameter("contents");
			
			//System.out.println(name + " " + password + " " + contents);
			
			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(contents);
			
			new GuestbookDao().insert(vo);
			
			response.sendRedirect("/mysite/guestbook");
			
		} else if( "deleteform".equals(actionName) == true ) {			
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/guestbook/deleteform.jsp" );
			rd.forward(request, response);	
			
		} else if( "delete".equals(actionName) == true ) {
			String no = request.getParameter("no");
			String password = request.getParameter("password");
			
			GuestbookVo vo = new GuestbookVo();
			vo.setNo(Long.parseLong(no));
			vo.setPassword(password);
			
			GuestbookDao dao = new GuestbookDao();
			dao.delete(vo);
			
			response.sendRedirect("/mysite/guestbook");
			
		} else {
			/* default 요청(list 처리) */
			GuestbookDao dao = new GuestbookDao();
			List<GuestbookVo> list = dao.fetchList();
			
			// 데이터 전달
			request.setAttribute("list", list);
			
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/guestbook/list.jsp" );
			rd.forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
