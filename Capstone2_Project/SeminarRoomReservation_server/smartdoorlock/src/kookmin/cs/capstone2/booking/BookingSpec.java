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

import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

/*
 * filename : ReservationSpec.java
 * 기능 : 선택된 예약의 예약 신청 내역을 보여준다.
 */
public class BookingSpec extends MyHttpServlet {
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
		//String requestString = StaticMethods.getBody(request);
		
		// request 파라미터에서 json 파싱
		//JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		//String reservationId = requestObject.get("id").toString(); // get reservation id
		// request 파라미터로 전송된 값 얻기
		String reservationId = request.getParameter("reservationId");
		PrintWriter pw = response.getWriter();
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select * from reservationinfo where id='" + reservationId + "';";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				subJsonObj.put("room", rs.getString("room_id"));
				subJsonObj.put("user", rs.getString("user_id"));
				subJsonObj.put("date", rs.getString("date"));
				subJsonObj.put("startTime", rs.getString("start_time"));
				subJsonObj.put("endTime", rs.getString("end_time"));
				subJsonObj.put("context", rs.getString("context"));
			}
			
			// 회의 참석자 정보
			sql = "select user.text_id from seminarmember, user "
					+ "where seminarmember.id = " + reservationId
					+ " and (seminarmember.user_id=user.id);";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				jsonArrayInfo = new JSONObject();
				jsonArrayInfo.put("member", rs.getString("text_id"));
				jsonArray.add(jsonArrayInfo);
			}
			
			subJsonObj.put("memberList", jsonArray);
			responseJsonObj.put("responseData", subJsonObj);
			
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			subJsonObj.put("result", StaticVariables.ERROR_MYSQL);
			responseJsonObj.put("responseData", subJsonObj);
		} finally {
			try {
				if (stmt != null) 
					stmt.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
				pw.println(responseJsonObj.toJSONString());
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}
}