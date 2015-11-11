package kookmin.cs.capstone2.mypage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

// 특정 예약 내역에 대한 키 제어

public class SpecificKey extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// RequestBody to String
		String requestString = StaticMethods.getBody(request);
		System.out.println("specificKey: " + requestString);

		// request 파라미터에서 json 파싱
		JSONObject jsonObject = (JSONObject) JSONValue.parse(requestString);
		String reservationId = jsonObject.get("id").toString();
		String userId = jsonObject.get("userId").toString();
		String command = jsonObject.get("command").toString();

		Connection conn = null; // DB 연결을 위한 Connection 객체
		Statement stmt = null; // ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; // SQL Query 결과를 담을 테이블 형식의 객체

		// Json for result
		JSONObject resultObject = new JSONObject(); // 최종 완성될 JSONObject 선언
		JSONObject resultInfo = new JSONObject(); // 서블릿 실행 결과 반환
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select date, room_id, start_time, end_time, status "
					+ "from reservationinfo "
					+ "where id = " + reservationId;
			
			rs = stmt.executeQuery(sql); //sql문 실행
			String date = "", startTime = "", endTime = "";
			int status = -1, roomId=-1;
			
			SQLException e = new SQLException(); // for SQLException
			
			if (rs.next()) {
				date = rs.getString("date");
				startTime = rs.getString("start_time");
				endTime = rs.getString("end_time");
				roomId = rs.getInt("room_id");
				status = rs.getInt("status");
			}
			if(status != StaticVariables.SUCCESS)
				throw e;
			if(StaticMethods.checkTime(date, startTime, endTime) != StaticVariables.SUCCESS)
				throw e;
			
			conn.setAutoCommit(false);// 오토커밋을 false로 지정하여 트랜잭션 조건을 맞춘다
			sql = "update room set status=" + command + " where id=" + roomId + ";"; // 방 잠금 상태 변경
			int updateResult = stmt.executeUpdate(sql);// return the row count for SQL DML statements
			if (updateResult != 1) { // update 실패했을 경우
				conn.rollback(); // 방 잠금 상태 변경 취소
				throw e;
			}
			
			// 출입 기록 삽입
			sql = "insert into roomhistory (room_id, user_id, command) values "
					+ "(" + roomId + ", " + userId + ", " + command + ");";
			int insertResult = stmt.executeUpdate(sql); // roomhistory에 기록 추가
			if (insertResult != 1){
				conn.rollback();
				throw e;
			}
			int result = StaticMethods.rasberrySocket(command, roomId); // 라즈베리파이에 요청 보내기
			if (result == StaticVariables.SUCCESS) {
				resultInfo.put("result", StaticVariables.SUCCESS);
				conn.commit();
			} else {
				resultInfo.put("result", StaticVariables.FAIL);
				conn.rollback();
			}
		} catch (ParseException pe){ // for StaticMethods.checkTime() 
			pe.printStackTrace();
			resultInfo.put("result", StaticVariables.FAIL);
		} catch (SQLException e) { // for SQL ERROR
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultInfo.put("result", StaticVariables.ERROR_MYSQL);
		} finally {
			resultObject.put("responseData", resultInfo);
			pw.println("SpecificKey : " + resultObject.toString());
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
