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

import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CancelMyBooking extends HttpServlet {
	/*
	 * request : mode(nothing or 날짜별) & (nothing or date(yyyy-MM-dd))
	 * response : 대기 중인 예약 내역 중 시간이 지나지 않은 내역 또는 날짜별 내역을 보여준다
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		response.setContentType("text/html;charset=utf-8");
		
		//RequestBody to String
		String requestString = StaticMethods.getBody(request);
		System.out.println(requestString);
		
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		String reservationId = requestObject.get("id").toString(); // get reservation id
		
		System.out.println("CancelMyBooking : " + reservationId);

		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		//for Json
		JSONObject jsonObject = new JSONObject(); //최종 완성될 JSONObject 선언
		JSONObject resultJson = new JSONObject(); //예약 신청 내역 한 개의 정보가 들어갈 JSONObject
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "delete from reservationinfo where id=" + reservationId;
			
			int result = stmt.executeUpdate(sql);

			if(result != 0)
				resultJson.put("result", StaticVariables.SUCCESS);
			else
				resultJson.put("result", StaticVariables.FAIL);
			
			//전체의 JSONObejct에 responseData란 이름으로 sql문 결과 저장
			jsonObject.put("responseData", resultJson);
			pw.println(jsonObject.toString());
			
			System.out.println("CancelMyBooking : " + jsonObject.toJSONString());
		
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
				pw.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}
}

