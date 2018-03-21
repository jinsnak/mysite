package kr.co.dhflour.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.dhflour.mysite.vo.UserVo;

public class UserDao {
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
	
	// 회원가입 insert
	public boolean insert(UserVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. Statement 준비
			String sql = "INSERT INTO USERS \r\n" + 
						 "VALUES (SEQ_USERS.NEXTVAL, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			
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
	
	// 로그인 select
	public UserVo fetch(UserVo vo) {
		UserVo result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. Statement 준비
			String sql = "SELECT NO, NAME, PASSWORD\r\n" + 
						 "FROM USERS\r\n" + 
						 "WHERE EMAIL = ?\r\n" + 
						 "AND PASSWORD = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setString(1, vo.getEmail());
			pstmt.setString(2, vo.getPassword());
			
			//5. sql문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				long no = rs.getLong(1);
				String name = rs.getString(2);
				
				result = new UserVo();
				result.setNo(no);
				result.setName(name);
				
			}
			
		} catch (SQLException e) {
			System.out.println("연결: " + e);
			
		} finally {
			//7. 자원 정리
			try {
				if (rs != null) {
					rs.close();
				}
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
	
	// 회원정보 수정
	public UserVo fetch(long no) {
		UserVo result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. Statement 준비
			String sql = "SELECT NO, NAME, EMAIL, GENDER \r\n" + 
						 "FROM USERS\r\n" + 
						 "WHERE NO = ?\r\n" ;
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setLong(1, no);
			
			//5. sql문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new UserVo();
				result.setNo(rs.getLong(1));
				result.setName(rs.getString(2));
				result.setEmail(rs.getString(3));
				result.setGender(rs.getString(4));
			}
			
		} catch (SQLException e) {
			System.out.println("연결: " + e);
			
		} finally {
			//7. 자원 정리
			try {
				if (rs != null) {
					rs.close();
				}
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
	
	// 회원정보 수정
	public boolean update(UserVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConncetion();
			
			//3. Statement 준비 (패스워드 null체크)
			if (vo.getPassword() == null || "".equals(vo.getPassword()) ) {
				String sql = "UPDATE USERS\r\n" + 
					     "SET NAME = ?, \r\n" + 
					     "    GENDER = ?, \r\n" + 
					     "WHERE NO = ? ";
				pstmt = conn.prepareStatement(sql);
				
				//4. binding
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());
				pstmt.setLong(3, vo.getNo());
				
			} else {
				String sql = "UPDATE USERS\r\n" + 
					     "SET NAME = ?, \r\n" + 
					     "    GENDER = ?, \r\n" + 
					     "    PASSWORD = ? \r\n" + 
					     "WHERE NO = ? ";
				pstmt = conn.prepareStatement(sql);
				
				//4. binding
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());
				pstmt.setString(3, vo.getPassword());
				pstmt.setLong(4, vo.getNo());
			}
			
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
