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

public class UsingStatus extends HttpServlet {
	/*
	 * request : date(yyyy-MM-dd) 
	 * response : 예약 고유 id(id), 세미나실 id(room_id), 예약시작 시간(start_time), 예약 끝 시간(end_time)
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// request 파라미터로 전송된 값 얻기
		String date = request.getParameter("date");
		System.out.println(date);
		
		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		//for Json
		JSONObject jsonObject = new JSONObject(); //최종 완성될 JSONObject 선언
		JSONArray statusArray = new JSONArray(); //예약 내역의 정보를 담을 Array
		JSONObject statusInfo = new JSONObject(); //예약 내역 한 개의 정보가 들어갈 JSONObject
		
		
		try {
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select * from reservationinfo where date='" + date + "';";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				statusInfo = new JSONObject();
				statusInfo.put("id", rs.getInt("id"));
				statusInfo.put("room_id", rs.getInt("room_id"));
				statusInfo.put("start_time", rs.getString("start_time"));
				statusInfo.put("end_time", rs.getString("end_time"));
				
				statusArray.add(statusInfo); //Array에 Object 추가
/*				int id = rs.getInt("id");
				int room_id = rs.getInt("room_id");
				String start_time = rs.getString("start_time");
				String end_time = rs.getString("end_time");
				pw.println("id=" + id + "&room_id=" + room_id + "&start_time=" + start_time
						+ "&end_time=" + end_time);*/
			}
			
			//전체의 JSONObejct에 status란 이름으로 JSON정보로 구성된 Array value 입력
			jsonObject.put("status", statusArray); 
			pw.println(jsonObject.toString());
			System.out.println(jsonObject.toJSONString());
			System.out.println(jsonObject.toString());
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
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}
}