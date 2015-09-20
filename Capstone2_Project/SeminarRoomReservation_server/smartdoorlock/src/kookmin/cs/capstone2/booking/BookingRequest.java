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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kookmin.cs.capstone2.var.StaticVariables;

public class BookingRequest extends HttpServlet {
	
	/*
	 * request : 세미나실 id, 신청자 id, 날짜(yyyy-MM-dd), 시작시간, 끝시간, 예약 목적
	 * response : 회원 등록 성공 여부
	 */
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// request 파라미터로 전송된 값 얻기
		String room_id = request.getParameter("room_id");
		String user_id = request.getParameter("user_id");
		String date = request.getParameter("date");
		String startTime = request.getParameter("start_time");
		String endTime = request.getParameter("end_time");
		String context = request.getParameter("context");

		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		try {
			
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "insert into reservationinfo(room_id, user_id, date, start_time, end_time, context) values ("+ room_id + ", " + user_id + ", '" + date + "', '" + startTime + "', '" + endTime + "', '" + context + "');";
			int n = stmt.executeUpdate(sql);// return the row count for SQL DML statements 
			
			//response to client
			if (n == 1) {
				pw.println(StaticVariables.SUCCESS);
			} else
				pw.println(StaticVariables.FAIL);
			
		} catch (SQLException se) {
			
			System.out.println(se.getMessage());
			pw.println(StaticVariables.ERROR_MYSQL);
		
		} finally {
			pw.close();
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		
		}
	}
}
