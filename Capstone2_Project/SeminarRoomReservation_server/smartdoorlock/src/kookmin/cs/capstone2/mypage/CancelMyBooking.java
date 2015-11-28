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

public class CancelMyBooking extends MyHttpServlet {
	/*
	 * request : mode(nothing or 날짜별) & (nothing or date(yyyy-MM-dd))
	 * response : 대기 중인 예약 내역 중 시간이 지나지 않은 내역 또는 날짜별 내역을 보여준다
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		super.service(request, response);
		
		//RequestBody to String
		System.out.println(requestString);
		
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		String reservationId = requestObject.get("id").toString(); // get reservation id
		
		System.out.println("CancelMyBooking : " + reservationId);

		PrintWriter pw = response.getWriter();
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "delete from reservationinfo where id=" + reservationId;
			
			int result = stmt.executeUpdate(sql);

			if(result != 0)
				subJsonObj.put("result", StaticVariables.SUCCESS);
			else
				subJsonObj.put("result", StaticVariables.FAIL);
			
			//전체의 JSONObejct에 responseData란 이름으로 sql문 결과 저장
			responseJsonObj.put("responseData", subJsonObj);
			pw.println(responseJsonObj.toString());
			
			System.out.println("CancelMyBooking : " + responseJsonObj.toJSONString());
		
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

