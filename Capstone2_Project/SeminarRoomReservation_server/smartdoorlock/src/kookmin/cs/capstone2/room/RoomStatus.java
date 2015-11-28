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

import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticVariables;

/*
 * filename : RoomStatus.java
 * 기능 : 
 * 	세미나실이 선택되면 그 세미나실 잠금 장치의 잠김/풀림 상태 정보를 제공한다.
 */
public class RoomStatus extends MyHttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// request 파라미터로 전송된 값 얻기
		String roomId = request.getParameter("id");
		System.out.println(roomId);
				
		PrintWriter pw = response.getWriter();

		try {
			
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select status from room where id='" + roomId + "';"; 
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String status = rs.getString("status");
				responseJsonObj.put("status", status);
			}
			
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			responseJsonObj.put("status", StaticVariables.ERROR_MYSQL);
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
				responseJsonObj.put("status", StaticVariables.ERROR_MYSQL);
			}
			
			//response room status
			pw.println(responseJsonObj);
			System.out.println(responseJsonObj);
			pw.close();
		}
	}
}
