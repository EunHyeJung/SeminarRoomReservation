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

import kookmin.cs.capstone2.common.StaticMethods;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UsingStatus extends HttpServlet {
	/*
	 * request : date(yyyy-MM-dd) 
	 * response : 예약 고유 id(id), 세미나실 id(roomId), 예약시작 시간(startTime), 예약 끝 시간(endTime), 예약 상태(status)
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
		JSONObject reqObject = (JSONObject)JSONValue.parse(requestString);
		JSONArray reqJSONArray = (JSONArray)reqObject.get("sendingData");
		String date = reqObject.get("date").toString();
		System.out.println(reqObject);
		//String roomId = reqJSONArray.get("roomId").toString();
		//String userId = reqJSONArray.get("userId").toString();
		
		// request 파라미터로 전송된 값 얻기
		//String date = request.getParameter("date");
		//System.out.println(date);
		
		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		//for Json
		JSONObject jsonObject = new JSONObject(); //최종 완성될 JSONObject 선언
		JSONObject arrayObject = new JSONObject(); //배열을 담을 JSONObject
		JSONArray statusArray = new JSONArray(); //예약 내역의 정보를 담을 Array
		JSONObject statusInfo = new JSONObject(); //예약 내역 한 개의 정보가 들어갈 JSONObject
		
		try {
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			  
			//status!=0, 즉 거절상태가 아닌 예약 내역 정보를 가져온다
			String sql = "select reservationinfo.id, room.id as room_id, reservationinfo.start_time, reservationinfo.end_time, reservationinfo.status "
					+ "from reservationinfo, room where (reservationinfo.room_id=room.id) and date='" + date + "' and reservationinfo.status != 0 "
							+ "Order by room.room_id asc;";
			
			rs = stmt.executeQuery(sql);
			if(!rs.next()){
				jsonObject.put("responseData", null);
			}else {
				while (rs.next()) {
					statusInfo = new JSONObject();
					statusInfo.put("reservationId", rs.getInt("id"));
					statusInfo.put("roomId", rs.getInt("room_id"));
					statusInfo.put("startTime", rs.getString("start_time"));
					statusInfo.put("endTime", rs.getString("end_time"));
					statusInfo.put("reservationStatus", rs.getString("status"));
					
					statusArray.add(statusInfo); //Array에 Object 추가
				}
				arrayObject.put("reservation", statusArray);
				
				//전체의 JSONObejct에 status란 이름으로 JSON정보로 구성된 Array value 입력
				jsonObject.put("responseData", arrayObject); 
			}
			pw.println(jsonObject);
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