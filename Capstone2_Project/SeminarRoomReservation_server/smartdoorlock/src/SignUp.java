
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * A Servlet for Sign Up 
 * 1) HttpServlet클래스 상속
 * 2) service 메소드 오버라이딩
 */
public class SignUp extends HttpServlet {
	
	/*
	 * request : 요청에 관련된 정보를 갖는 객체 (클라이언트가 보낸 정보) 아이디, 비밀번호, 이름, 전화번호 
	 * 
	 * response : 응답에 관련된 정보를 갖는 객체
	 * 
	 */

	public static final int SUCCESS = 1;
	public static final int FAIL = 0;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// request 파라미터로 전송된 값 얻기
		request.setCharacterEncoding("euc-kr");
		response.setContentType("text/html;charset=euc-kr");

		String text_id = request.getParameter("id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");

		ResultSet rs = null;
		Statement stmt = null;
		PrintWriter pw = response.getWriter();

		ConnectionPool cp = new ConnectionPool();
		
		try {
			String sql = "insert into user (text_id, password, name, phone) values ('"+ text_id + "', '" + password + "', '" + name + "', '" + phone + "');";
			stmt = cp.returnCon().createStatement();
			int n = stmt.executeUpdate(sql);

			if (n == 1) {
				pw.println(SUCCESS);
			} else
				pw.println(FAIL);
			
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		} finally {
			try {
				if (stmt != null){
					stmt.close();
					cp.termination();
				}
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}

		/*
		 * try{ String sql = "insert into bb values(" + id + "," + name + ")";
		 * stmt = con.createStatement(); int n = stmt.executeUpdate(sql);
		 * if(stmt.executeQuery(sql).next()) pw.println(SUCCESS); else
		 * pw.println(FAIL);
		 * 
		 * pstmt = con.prepareStatement(sql); pstmt.setString(1, id);
		 * pstmt.setString(2, password); pstmt.setString(3, name);
		 * pstmt.setString(4, phone); }catch(SQLException se){
		 * System.out.println(se.getMessage()); }finally{ try{ if(stmt != null)
		 * stmt.close(); if(con != null) con.close(); }catch(SQLException se){
		 * System.out.println(se.getMessage()); } }
		 */
	}
}