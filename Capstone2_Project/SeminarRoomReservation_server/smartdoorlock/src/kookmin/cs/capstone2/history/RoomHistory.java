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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 * filename : RoomHistory.java
 * 기능 : 날짜 정보와 방이름을 받고 출입 기록 내역을 제공한다.
 */
public class RoomHistory extends HttpServlet {

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

		// request 파라미터로 전송된 값 얻기
		String date = request.getParameter("date");
		String room = request.getParameter("room");
		
		System.out.println(date);

		String jocl = "jdbc:apache:commons:dbcp:/pool1"; // 커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; // DB 연결을 위한 Connection 객체
		Statement stmt = null; // ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		// for Json
		JSONObject jsonObject = new JSONObject(); // 최종 완성될 JSONObject 선언
		JSONArray historyArray = new JSONArray(); // 예약 내역의 정보를 담을 Array
		JSONObject historyInfo = new JSONObject(); // 예약 내역 한 개의 정보가 들어갈 JSONObject
		
		try {
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			
			String sql = "";
			
			//string room이 ALL일때는 방과 관계없이 해당 날짜 기록을 모두 보여준다
			if(room=="ALL")
				sql = "select room.room_id, user.text_id, roomhistory.time_stamp, roomhistory.command from roomhistory, room, user where (roomhistory.room_id=room.id) and (roomhistory.user_id=user.id) and date(time_stamp)='" + date + "';";
			else
				sql = "select room.room_id, user.text_id, roomhistory.time_stamp, roomhistory.command from roomhistory, room, user where (roomhistory.room_id=room.id) and (roomhistory.user_id=user.id) and date(time_stamp)='" + date + "' and room.room_id='" + room + "';";
			
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				historyInfo = new JSONObject();
				historyInfo.put("room_id", rs.getInt("room_id"));
				historyInfo.put("text_id", rs.getString("text_id"));
				historyInfo.put("time_stamp", rs.getString("time_stamp"));
				
				if(rs.getBoolean("command"))
					historyInfo.put("command", "open");
				else
					historyInfo.put("command", "close");
				
				historyArray.add(historyInfo); //Array에 Object 추가
			}
			
			//전체의 JSONObejct에 history란 이름으로 JSON정보로 구성된 Array value 입력
			jsonObject.put("history", historyArray); 
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
