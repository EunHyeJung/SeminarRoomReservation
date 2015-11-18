package kookmin.cs.capstone2.history;

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
import kookmin.cs.capstone2.common.StaticVariables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 * filename : RoomHistory.java
 * 기능 : 날짜 정보와 방이름을 받고 출입 기록 내역을 제공한다.
 */
public class RoomHistory extends MyHttpServlet {

	/*
	 * request : 날짜, 방이름
	 * response : 날짜와 해당 방의 출입 기록 내역 제공 (방 이름, user ID, 날짜와시간, 명령 종류)
	 */

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String date = "";
		String room = "";
		
		// request 파라미터로 전송된 값 얻기
		date = request.getParameter("date");
		room = request.getParameter("room");
		
		System.out.println(date + " " + room);

		PrintWriter pw = response.getWriter();
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			
			String sql = "";
			sql = "select room.room_id, user.text_id, roomhistory.time_stamp, roomhistory.command "
					+ "from roomhistory, room, user where (roomhistory.room_id=room.id) and (roomhistory.user_id=user.id) and date(time_stamp)='" + date + "'";
			
			//string room이 ALL일때는 방과 관계없이 해당 날짜 기록을 모두 보여준다
			if(!room.equals("ALL")){
				sql += "and room.room_id='" + room + "'";
			}
			sql += ";";
			
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				jsonArrayInfo = new JSONObject();
				jsonArrayInfo.put("roomId", rs.getInt("room_id"));
				jsonArrayInfo.put("textId", rs.getString("text_id"));
				jsonArrayInfo.put("timeStamp", rs.getString("time_stamp"));
				
				if(rs.getBoolean("command"))
					jsonArrayInfo.put("command", "open");
				else
					jsonArrayInfo.put("command", "close");
				
				jsonArray.add(jsonArrayInfo); //Array에 Object 추가
			}
			subJsonObj.put("history", jsonArray);
			//전체의 JSONObejct에 history란 이름으로 JSON정보로 구성된 Array value 입력
			responseJsonObj.put("responseData", subJsonObj); 
			pw.println(responseJsonObj);
			System.out.println(responseJsonObj);
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
