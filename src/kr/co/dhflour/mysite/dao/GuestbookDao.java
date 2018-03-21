package kr.co.dhflour.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.co.dhflour.mysite.vo.GuestbookVo;


public class GuestbookDao {

	private Connection getConncetion() {
		Connection conn = null;
		
		try {
			//1. JDBC 드라이브 로딩
			Class.forName( "oracle.jdbc.driver.OracleDriver" );
			
			//2. Connection 가져오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이브 로딩 실패!");
		} catch (SQLException e) {
			System.out.println("연결: " + e);
			
		}
		
		return conn;
		
	}
	
	// 조회
	public List<GuestbookVo> fetchList() {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		
		Connection conn = null;
		Statement  stmt = null;
		ResultSet    rs = null;
		
		try {
			//1. JDBC 드라이브 로딩
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2. Connection 가져오기
			//String url = "jdbc:oracle:thin:@localhost:1521:xe";
			//conn = DriverManager.getConnection(url, "webdb", "webdb");
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. statement 준비
			stmt = conn.createStatement();
			
			//4. sql
			String sql = "SELECT NO, NAME, TO_CHAR(REG_DATE, 'YYYY-MM-DD HH:MI:SS') AS REG_DATE, CONTENTS \r\n" + 
					     "FROM GUESTBOOK\r\n" + 
					     "ORDER BY REG_DATE DESC";
			
			//5. binding
			//stmt.setLong(1, n);
			
			rs = stmt.executeQuery(sql);
			
			while ( rs.next()) { 
				long no = rs.getLong(1);
				String name = rs.getString(2);
				String regDate = rs.getString(3);
				String contents = rs.getString(4);
				
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setRegDate(regDate);
				vo.setContents(contents);
				
				list.add(vo);
			}
			
			
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	// 입력
	public boolean insert(GuestbookVo vo) {
		
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			//1. JDBC 드라이브 로딩
			//Class.forName( "oracle.jdbc.driver.OracleDriver" );
			
			//2. Connection 가져오기
			//String url = "jdbc:oracle:thin:@localhost:1521:xe";
			//conn = DriverManager.getConnection(url, "webdb", "webdb");
			//System.out.println("연결 성공!");
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. Statement 준비
			String sql = "INSERT INTO GUESTBOOK\r\n" + 
					"VALUES (SEQ_GUESTBOOK.NEXTVAL, ?, ?, SYSDATE, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setString(1, vo.getName());;
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());
			
			//5. sql문 실행
			int count = pstmt.executeUpdate();
			
			//6. 성공 유무
			if (count == 1) {
				result = true;
			} else {
				result = false;
			}
			
		} catch (SQLException e) {
			System.out.println("연결: " + e);
			
		} finally {
			//7. 자원 정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	// 삭제
	public boolean delete(GuestbookVo vo) {
		
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			//1. JDBC 드라이브 로딩
			//Class.forName( "oracle.jdbc.driver.OracleDriver" );
			
			//2. Connection 가져오기
			//String url = "jdbc:oracle:thin:@localhost:1521:xe";
			//conn = DriverManager.getConnection(url, "webdb", "webdb");
			//System.out.println("연결 성공!");
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. Statement 준비
			String sql = "DELETE GUESTBOOK \r\n" + 
						 "WHERE NO = ? \r\n" + 
						 "AND PASSWORD = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setLong(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());
			
			//5. sql문 실행
			int count = pstmt.executeUpdate();
			
			//6. 성공 유무
			if (count == 1) {
				result = true;
			} else {
				result = false;
			}
			
		} catch (SQLException e) {
			System.out.println("연결: " + e);
			
		} finally {
			//7. 자원 정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
}
