package kookmin.cs.capstone2.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kookmin.cs.capstone2.var.StaticVariables;

public class Login extends HttpServlet {
	/*
	 * request : 아이디, 비밀번호 response : 로그인 성공 여부
	 */

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// request 파라미터로 전송된 값 얻기
		String text_id = request.getParameter("id");
		String password = request.getParameter("password");

		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		try {
			
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select * from user where text_id='" 
					+ text_id + "' && password='" + password + "'"; //request로 들어온 id와 password가 일치하는 회원을 찾는 질의문
			rs = stmt.executeQuery(sql); //SQL Query Result
			
			//id와 password가 일치하는 user가 있을 경우 b_admin column을 확인하여 관리자 여부를 가린다
			if (rs.next()) {
				Boolean bAdmin = rs.getBoolean("b_admin");
				if (bAdmin == true)
					pw.println(StaticVariables.ADMIN); //관리자 
				else
					pw.println(StaticVariables.SUCCESS); //일반 사용자
			} else
				pw.println(StaticVariables.FAIL); //로그인 실패

		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			pw.println(StaticVariables.ERROR_MYSQL);
		} finally {
			try {
				pw.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
				pw.println(StaticVariables.ERROR_MYSQL);
			}
		}
	}
}
