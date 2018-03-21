package kr.co.dhflour.mysite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.dhflour.mysite.dao.BoardDao;
import kr.co.dhflour.mysite.vo.BoardVo;
import kr.co.dhflour.mysite.vo.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String action = request.getParameter("a");
		if (action == null) {
			action = "default";
		}
		
		switch (action) {
		case "view" :{
			
			long no = Long.parseLong(request.getParameter("no"));
			
			BoardVo vo = new BoardVo();
			vo.setNo(no);
			
			BoardDao dao = new BoardDao();
			dao.updateHit(vo);
			
			vo = dao.fetch(no);
	
			//System.out.println(vo);
			request.setAttribute("boardVo", vo);
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/board/view.jsp" );
			rd.forward(request, response);
			
			break;
		}
		case "writeform":{
			HttpSession session = request.getSession();
			if (session == null) {
				response.sendRedirect("/mysite/board?m=needlogin");
				return;
			}
			
			UserVo vo = (UserVo)session.getAttribute("authUser");
			if (vo == null) {
				response.sendRedirect("/mysite/board?m=needlogin");
				return;
			}

			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/board/write.jsp" );
			rd.forward(request, response);
			
			break;
		}
		case "write":{
			
			String title = request.getParameter("title");
			String contents = request.getParameter("content");
			
			HttpSession session = request.getSession();
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if (authUser == null) {
				response.sendRedirect("/mysite/board?m=needlogin");
				return;
			}
			
			BoardVo vo = new BoardVo();
			
			vo.setTitle(title);
			vo.setContents(contents);
			//insert문에서 바로 처리(고정값)
//			vo.setOrderNo(1);
//			vo.setDepth(0);
			vo.setUsersNo(authUser.getNo());
			
			BoardDao dao = new BoardDao();
			boolean result = dao.insert(vo);
			
			if (result) {
				response.sendRedirect("/mysite/board?m=success");
				return;
			}
			
			break;
		}
		case "delete":{
			
			HttpSession session = request.getSession();
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if (authUser == null) {
				response.sendRedirect("/mysite/board?m=needlogin");
				return;
			}
			
			long no = Long.parseLong(request.getParameter("no"));
			BoardVo vo = new BoardVo();
			vo.setNo(no);
			
			BoardDao dao = new BoardDao();
			
			boolean result = dao.delete(vo);
			
			if (result) {
				response.sendRedirect("/mysite/board?m=delsuccess");
				return;
			}
			
			break;
		}
		case "modifyform":{
			
			long no = Long.parseLong(request.getParameter("no"));
			
			BoardVo vo = new BoardVo();
			vo.setNo(no);
			
			BoardDao dao = new BoardDao();
			
			vo = dao.fetch(no);

			request.setAttribute("boardVo", vo);
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/board/modify.jsp" );
			rd.forward(request, response);
			
			break;
		}
		case "modify": {
			
			long no = Long.parseLong(request.getParameter("no"));
			String title = request.getParameter("title");
			String contents = request.getParameter("content");
			
			BoardVo vo = new BoardVo();
			vo.setNo(no);
			vo.setTitle(title);
			vo.setContents(contents);
			
			BoardDao dao = new BoardDao();
			boolean result = dao.update(vo);
			
			if (result) {
				response.sendRedirect("/mysite/board?a=view&no=" + request.getParameter("no"));
				return;
			}
			
			break;
		}
		
		case "replyform": {
			
			HttpSession session = request.getSession();
			if( session == null ) {
				response.sendRedirect( "/mysite/board?m=needlogin"  );
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute( "authUser" );
			if( authUser == null ) {
				response.sendRedirect( "/mysite/board?m=needlogin"  );
				return;
			}
			
			long no = Long.parseLong( request.getParameter( "no" ) );
			BoardDao dao = new BoardDao();
			BoardVo boardVo = dao.fetch(no);
			
			request.setAttribute( "boardVo", boardVo );
			
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/board/reply.jsp" );
			rd.forward(request, response);
			
			break;
		}
		case "reply": {
			
			HttpSession session = request.getSession();
			if( session == null ) {
				response.sendRedirect( "/mysite/board?m=needlogin"  );
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute( "authUser" );
			if( authUser == null ) {
				response.sendRedirect( "/mysite/board?m=needlogin"  );
				return;
			}
			
			String title = request.getParameter( "title" );
			String contents = request.getParameter( "content" );
			long groupNo = Long.parseLong( request.getParameter( "gno" ) );
			long orderNo = Long.parseLong( request.getParameter( "ono" ) );
			long depth = Long.parseLong( request.getParameter( "d" ) );
			
			BoardDao dao = new BoardDao();
			BoardVo vo = new BoardVo();
		
			vo.setTitle(title);
			vo.setContents(contents);
			vo.setUsersNo( authUser.getNo() );
			
			// 같은 그룹의 orderNo 보다 큰 글 들의 orderNo 1씩 증가
			orderNo = orderNo + 1;
			depth = depth + 1;
			
			dao.increaseGroupOrder( groupNo, orderNo );
			
			vo.setGroupNo(groupNo);
			vo.setOrderNo(orderNo);
			vo.setDepth( depth );
//			System.out.println(vo);
			dao.insert2(vo);
			
			response.sendRedirect( "/mysite/board" );
			
			break;
		}
		default :{
			
			/* default 요청(list 처리) */
			BoardDao dao = new BoardDao();
			List<BoardVo> list = dao.fetchList();
			
			// 데이터 전달
			request.setAttribute("list", list);
			
			// 포워딩
			RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/views/board/list.jsp" );
			rd.forward(request, response);
			
			break;
		}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
