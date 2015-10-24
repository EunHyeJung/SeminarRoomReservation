package kookmin.cs.capstone2.mypage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

/*
 * 일반 사용자의 예약 상황에 맞추어 스마트키 사용 여부를 알려준다.
 */
public class SmartKey extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// RequestBody to String
		String requestString = StaticMethods.getBody(request);

		// request 파라미터에서 json 파싱
		JSONObject jsonObject = (JSONObject) JSONValue.parse(requestString);
		String userId = jsonObject.get("userId").toString();

		Connection conn = null; // DB 연결을 위한 Connection 객체
		Statement stmt = null; // ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; // SQL Query 결과를 담을 테이블 형식의 객체

		// Json for result
		JSONObject resultObject = new JSONObject(); // 최종 완성될 JSONObject 선언
		JSONObject keyInfo = new JSONObject(); // 특정 유저의 스마트키 권한 정보를 담을 JSONObject
		
		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select room.room_id, date, start_time, end_time "
					+ "from reservationinfo, room "
					+ "where (reservationinfo.room_id=room.id) and date >= curdate() and "
					+ "reservationinfo.status=1 and not (date=curdate() and end_time<curtime()) "
					+ "and user_id=" + userId;
			
			rs = stmt.executeQuery(sql); //sql문 실행
			while (rs.next()) {
			
			}
		}
	}
}
