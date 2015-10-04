package kookmin.cs.capstone2.room;

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

/*
 * filename : RoomStatus.java
 * 기능 : 
 * 	세미나실이 선택되면 그 세미나실 잠금 장치의 잠김/풀림 상태 정보를 제공한다.
 */
public class RoomStatus extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// request 파라미터로 전송된 값 얻기
		String roomName = request.getParameter("roomName");
				
		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		JSONObject jsonObject = new JSONObject();
		
		try {
			
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select status from room where room_id='" + roomName + "';"; 
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String status = rs.getString("status");
				jsonObject.put("status", status);
				pw.println(jsonObject); //response room status
			}
			
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			pw.println(StaticVariables.ERROR_MYSQL);
		} finally {
			try {
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
