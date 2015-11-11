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
import org.json.simple.JSONValue;

import kookmin.cs.capstone2.common.StaticMethods;
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
		
		//RequestBody to String
		String requestString = StaticMethods.getBody(request);
		System.out.println(requestString);
		
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		String reservationId = requestObject.get("id").toString(); // get reservation id
		
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체
		
		// for Json
		JSONObject jsonObject = new JSONObject(); // 최종 완성될 JSONObject 선언
		JSONObject specObject = new JSONObject(); 
		JSONArray memberArray = new JSONArray(); // 아이디가 들어갈 배열
		JSONObject memberInfo = null; // 배열 정보 한 개가 들어갈 JSONObject
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select * from reservationinfo where id='" + reservationId + "';";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				specObject.put("room", rs.getString("room_id"));
				specObject.put("user", rs.getString("user_id"));
				specObject.put("date", rs.getString("date"));
				specObject.put("start_time", rs.getString("start_time"));
				specObject.put("end_time", rs.getString("end_time"));
				specObject.put("context", rs.getString("context"));
			}
			
			// 회의 참석자 정보
			sql = "select user.text_id from seminarmember, user "
					+ "where seminarmember.id = " + reservationId
					+ " and (seminarmember.user_id=user.id);";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				memberInfo = new JSONObject();
				memberInfo.put("member", rs.getString("text_id"));
				memberArray.add(memberInfo);
			}
			
			specObject.put("memberList", memberArray);
			jsonObject.put("responseData", specObject);
			
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			specObject.put("result", StaticVariables.ERROR_MYSQL);
			jsonObject.put("responseData", specObject);
		} finally {
			try {
				if (stmt != null) 
					stmt.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
				pw.println(jsonObject.toJSONString());
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}
}