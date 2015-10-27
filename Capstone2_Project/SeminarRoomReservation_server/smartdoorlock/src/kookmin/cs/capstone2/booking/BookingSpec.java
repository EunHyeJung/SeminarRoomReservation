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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kookmin.cs.capstone2.common.StaticVariables;

/*
 * filename : ReservationSpec.java
 * 기능 : 선택된 예약의 예약 신청 내역을 보여준다.
 */
public class BookingSpec extends HttpServlet {
	/*
	 * request : 예약 고유 id
	 * response : 세미나실 id, 신청자 id, date, 예약 시작 시간, 예약 끝 시간, 예약 목적
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// request 파라미터로 전송된 값 얻기
		String id = request.getParameter("id");
		
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select * from reservationinfo where id='" + id + "';";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String room = rs.getString("room_id");
				String user = rs.getString("user_id");
				String date = rs.getString("date");
				String start = rs.getString("start_time");
				String end = rs.getString("end_time");
				String context = rs.getString("context");
				//모임 참석자 정보도 제공해야 함
				
				pw.println("room_id=" + room + "&user_id=" + user + "&date=" + date + "&start_time=" + start
						 + "&end_time=" + end +"&context=" + context);
			}
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
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
			}
		}
	}
}