package kookmin.cs.capstone2.mypage;

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

import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

//일반 사용자가 자신이 포함된 예약 내역을 볼 수 있다.

public class MyBookingList extends MyHttpServlet{
	
	/*
	 * request : 사용자 고유 아이디
	 * response : 해당 사용자가 신청한 예약 내역
	 */
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		super.service(request, response);
		
		//RequestBody to String
		System.out.println(requestString);
		
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		System.out.println("MyBookingList : " + requestObject.toJSONString());
		String date = requestObject.get("date").toString(); // get date
		String userId = requestObject.get("Id").toString(); // get userId
		
		PrintWriter pw = response.getWriter();
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select id, room_id, date, start_time, end_time, status "
					+ "from reservationinfo "
					+ "where (user_id=" + userId + " "
					+ "or id in (select id from seminarmember where user_id=" + userId +"))" ;
			if (date.equals("ALL")) //숫자 0
				sql += ";";
			else
				sql = "select * from (" + sql + ") as sub_result where date='" + date + "';";
			System.out.println("MyBookingList : " + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				jsonArrayInfo = new JSONObject();
				jsonArrayInfo.put("reservationId", rs.getInt("id"));
				jsonArrayInfo.put("roomId", rs.getInt("room_id"));
				jsonArrayInfo.put("date", rs.getString("date"));
				jsonArrayInfo.put("startTime", rs.getString("start_time"));
				jsonArrayInfo.put("endTime", rs.getString("end_time"));
				jsonArrayInfo.put("status", rs.getInt("status"));
				jsonArray.add(jsonArrayInfo); //Array에 Object 추가
			}
			
			subJsonObj.put("requestList", jsonArray);
			
			//전체의 JSONObejct에 status란 이름으로 JSON정보로 구성된 Array value 입력
			responseJsonObj.put("responseData", subJsonObj);
			
			//requestList로 한 번 더 감싸기
			pw.println(responseJsonObj.toString());
			System.out.println(responseJsonObj.toJSONString());
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
