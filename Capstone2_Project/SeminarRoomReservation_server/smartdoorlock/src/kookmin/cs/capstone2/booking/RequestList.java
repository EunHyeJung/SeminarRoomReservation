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

import kookmin.cs.capstone2.GCM.GcmSender;
import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RequestList extends HttpServlet {
	/*
	 * request : mode(nothing or 날짜별) & (nothing or date(yyyy-MM-dd))
	 * response : 대기 중인 예약 내역 중 시간이 지나지 않은 내역 또는 날짜별 내역을 보여준다
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		//RequestBody to String
		String requestString = StaticMethods.getBody(request);
		
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		
		String date = requestObject.get("date").toString(); // get date
		System.out.println("requestlist : " + requestObject.toString());

		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		//for Json
		JSONObject jsonObject = new JSONObject(); //최종 완성될 JSONObject 선언
		JSONObject arrayObject = new JSONObject();
		JSONArray reqListArray = new JSONArray(); //예약 신청 내역의 정보를 담을 Array
		JSONObject listInfo = new JSONObject(); //예약 신청 내역 한 개의 정보가 들어갈 JSONObject
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select reservationinfo.id, room.room_id, user.text_id, date, start_time, end_time, reservationinfo.status "
					+ "from reservationinfo, user, room "
					+ "where (reservationinfo.user_id=user.id) "
					+ "and (reservationinfo.room_id=room.id)";
			
			if(date.equals("ALL")) //전체 예약 신청 내역 중 신청 시간이 지나지 않은 것을 가져온다
				sql += " and date >= curdate() and reservationinfo.status=2 and not (date=curdate() and end_time<curtime());";
			else
				sql += " and reservationinfo.status=2 and not (date=curdate() and end_time<curtime()) and date='" + date + "';";

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				listInfo = new JSONObject();
				listInfo.put("reservationId", rs.getInt("id"));
				listInfo.put("roomId", rs.getString("room_id"));
				listInfo.put("userId", rs.getString("text_id"));
				listInfo.put("date", rs.getString("date"));
				listInfo.put("startTime", rs.getString("start_time"));
				listInfo.put("endTime", rs.getString("end_time"));
				listInfo.put("status", rs.getInt("status"));
				
				reqListArray.add(listInfo); //Array에 Object 추가
			}
			arrayObject.put("requestList", reqListArray);
			//전체의 JSONObejct에 status란 이름으로 JSON정보로 구성된 Array value 입력
			jsonObject.put("responseData", arrayObject);
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			pw.println(jsonObject.toString());
			System.out.println(jsonObject.toJSONString());
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
