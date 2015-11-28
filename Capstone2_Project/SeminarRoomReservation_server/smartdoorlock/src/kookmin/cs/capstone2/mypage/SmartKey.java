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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/*
 * 일반 사용자의 예약 상황에 맞추어 스마트키 사용 여부를 알려준다.
 */
public class SmartKey extends MyHttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		super.service(request, response);

		// RequestBody to String
		String requestString = StaticMethods.getBody(request);
		System.out.println("smartkey: " + requestString);

		// request 파라미터에서 json 파싱
		JSONObject requestJsonObj = (JSONObject) JSONValue.parse(requestString);
		String userId = requestJsonObj.get("Id").toString();
		System.out.println("smartkey : " + userId);
		PrintWriter pw = response.getWriter();
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select * from ("
					+ "select room_id, date, start_time, end_time, status from reservationinfo "
					+ "where user_id=" + userId + " "
					+ "or id in ("
					+ "select id from seminarmember where user_id=" + userId +")) as sub_result "
					+ "where date >= curdate() "
					+ "and status=1 "
					+ "and not (date=curdate() and end_time<curtime()) "
					+ "order by date ASC, start_time ASC limit 1;";
			
			rs = stmt.executeQuery(sql); //sql문 실행
			if (rs.next()) {
				String date, startTime, endTime;
				date = rs.getString("date");
				startTime = rs.getString("start_time");
				endTime = rs.getString("end_time");
				
				//현재 날짜와 DB에서 얻어온 Date/start_time을 같은 형식으로 맞춰주기
				Date today = new Date(); //오늘 날짜
				Date resultStartDate, resultEndDate;
				String strStartDate = date + " " + startTime;
				String strEndDate = date + " " + endTime;
				
				resultStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDate);
				resultEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strEndDate);
				
				subJsonObj.put("roomId", rs.getInt("room_id"));
				subJsonObj.put("date", date);
				subJsonObj.put("startTime", startTime);
				subJsonObj.put("endTime",endTime);
				
				if(resultStartDate.getTime() <= today.getTime() && resultEndDate.getTime() >= today.getTime())// 현재 시간이 예약 시간 사이일 때
					subJsonObj.put("key", StaticVariables.SUCCESS);
				else 
					subJsonObj.put("key", StaticVariables.RESERVATION);
			} else { // 예약 내역이 없음
				subJsonObj.put("key", StaticVariables.FAIL);
			}
			responseJsonObj.put("responseData", subJsonObj);
			System.out.println(responseJsonObj.toJSONString());
			
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) { // for error of date parsing
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pw.println(responseJsonObj.toString());
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
