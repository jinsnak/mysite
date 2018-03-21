package kr.co.dhflour.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.co.dhflour.mysite.vo.BoardVo;

public class BoardDao {
	
	// list조회
	public List<BoardVo> fetchList() {
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		Connection conn = null;
		Statement  stmt = null;
		ResultSet    rs = null;
		
		try {
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConnection();
			
			//3. statement 준비
			stmt = conn.createStatement();
			
			//4. sql
			String sql = "select a.no, a.title, a.contents, a.hit, to_char(a.reg_date, 'yyyy-mm-dd HH:MI:SS'), a.group_no, a.order_no, a.depth, a.users_no, b.name from board a, users b where a.users_no = b.no order by a.group_no desc, order_no asc";
			
			//5. binding
			//stmt.setLong(1, n);
			
			rs = stmt.executeQuery(sql);
			
			while ( rs.next()) { 
				long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				long hit = rs.getLong(4);
				String reg_date = rs.getString(5);
				long group_no = rs.getLong(6);
				long order_no = rs.getLong(7);
				long depth = rs.getLong(8);
				long users_no = rs.getLong(9);
				String name = rs.getString(10);
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setHit(hit);
				vo.setRegDate(reg_date);
				vo.setGroupNo(group_no);
				vo.setOrderNo(order_no);
				vo.setDepth(depth);
				vo.setUsersNo(users_no);
				vo.setName(name);
				
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
	
	//상세조회
	//fetch (select single)
	public BoardVo fetch(long num) {
		BoardVo vo = null;
		
		Connection conn = null; //지역변수는 반드시 초기화해주어야 한다.(=null)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = getConnection();
			
			String sql = "select no, title, contents, group_no, order_no, depth, users_no from board where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				long groupNo = rs.getLong(4);
				long orderNo = rs.getLong(5);
				long depth = rs.getLong(6);
				long usersNo = rs.getLong(7);
				
				vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUsersNo(usersNo);
				
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
		
		return vo;
	}
	
	// 입력 (새글 등록)
	public boolean insert(BoardVo vo) {
		
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "insert into board values (seq_board.nextval, ?, ?, 0, sysdate, nvl((select max(group_no) + 1 from board), 1), 1, 0, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setString(1, vo.getTitle());;
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(5, vo.getUsersNo());
			
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
	
	//댓글등록
	public void insert2( BoardVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
		
			/* 답글 등록 */
			String sql = 
					" insert" +
					"   into board" +
					" values( seq_board.nextval, ?, ?, 0, sysdate, ?, ?, ?, ? )"; 
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString( 1, vo.getTitle() );
				pstmt.setString( 2, vo.getContents() );
				pstmt.setLong( 3, vo.getGroupNo() );
				pstmt.setLong( 4, vo.getOrderNo() );
				pstmt.setLong( 5, vo.getDepth() );
				pstmt.setLong( 6, vo.getUsersNo() );

				pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}
	
	//그룹오더 증가
	public void increaseGroupOrder( Long groupNo, Long orderNo ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "update board set order_no = order_no + 1 where group_no = ? and order_no >= ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, groupNo );
			pstmt.setLong(2, orderNo );
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}
	
	// 조회수 증가
	public boolean updateHit(BoardVo vo) {
		
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "update board set hit = hit + 1 where no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setLong(1, vo.getNo());;
			
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
	
	// 글 수정
	public boolean update(BoardVo vo) {
		
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "update board set title = ?, contents = ? where no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			
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
	public boolean delete(BoardVo vo) {
		
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			//1~2. JDBC 드라이브 로딩 및 Connection 가져오기
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "delete board where no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			//4. binding
			pstmt.setLong(1, vo.getNo());
			
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
	
	private Connection getConnection() {
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
}
