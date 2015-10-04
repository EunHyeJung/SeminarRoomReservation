package kookmin.cs.capstone2.booking;

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

import org.json.simple.JSONObject;

import kookmin.cs.capstone2.var.StaticVariables;

//관리자가 예약 내역을 승인 또는 거절 할 때 DB 업데이트 해준다

public class BookingFilter extends HttpServlet {
	
	/*
	 * request : reservationId(id), command
	 * response : DB update 성공 여부
	 */
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// request 파라미터로 전송된 값 얻기
		String reservationId = request.getParameter("id");
		String command = request.getParameter("command");
		
		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();

		JSONObject jsonObject = new JSONObject();
		
		try {
			
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			
			String sql = "update reservationinfo set status=" + command + " where id=" + reservationId +";";
			int result = stmt.executeUpdate(sql);// return the row count for SQL DML statements
			if(result != 0)
				jsonObject.put("result", StaticVariables.SUCCESS);
			else
				jsonObject.put("result", StaticVariables.FAIL);
		}catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			jsonObject.put("result", StaticVariables.ERROR_MYSQL);
		} finally {
			try {
				if (stmt != null) 
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
				jsonObject.put("result", StaticVariables.ERROR_MYSQL);
			}
			pw.println(jsonObject);
		}
	}
}
