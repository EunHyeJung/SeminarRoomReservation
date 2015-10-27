package kookmin.cs.capstone2.room;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import sun.rmi.runtime.Log;
import kookmin.cs.capstone2.common.StaticVariables;

/*
 * filename : DoorControl.java
 * 기능 : 요청된 세미나실 문을 열고 닫는다.
 */
public class DoorControl extends HttpServlet { // 1.httpservlet 상속  2.drivermanager상속 두 가지 방법

	String command = null;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// request 파라미터로 전송된 값 얻기 => json으로 수정해야 함
		// ******************************* //송미랑도 맞춰야함
		String userId = request.getParameter("id"); // 사용자 고유 id
		String roomId = request.getParameter("roomId");
		command = request.getParameter("command");
		System.out.println(userId + " " + roomId + " " + command);

		Connection conn = null; // DB 연결을 위한 Connection 객체
		Statement stmt = null; // ready for DB Query result
		PrintWriter pw = response.getWriter();
		JSONObject jsonObject = new JSONObject();

		try {

			conn = DriverManager.getConnection(StaticVariables.JOCL); // 커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); // DB에 SQL문을 보내기 위한 Statement를 생성

			conn.setAutoCommit(false);// 오토커밋을 false로 지정하여 트랜잭션 조건을 맞춘다

			String sql = // 방 잠금 장치 상태 변경 명령
			"update room set status=" + command + " where id=" + roomId + ";";
			int updateResult = stmt.executeUpdate(sql);// return the row count for SQL DML statements

			if (updateResult == 1) { // update 성공했을 경우
				sql = "insert into roomhistory (room_id, user_id, command) values " // 출입
																					// 기록
																					// insert
						+ "(" + roomId + ", " + userId + ", " + command + ");";
				int insertResult = stmt.executeUpdate(sql); // roomhistory에 기록 추가

				if (insertResult == 1) {
					String result = sendPost(command);
					if (result.equals(command)) {
						jsonObject.put("result", StaticVariables.SUCCESS);
						conn.commit();
					} else {
						jsonObject.put("result", StaticVariables.FAIL);
						conn.rollback();
					}
				}
			}
		} catch (SQLException e) { // sql이 잘못됫거나..
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();

			if (conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			jsonObject.put("result", StaticVariables.ERROR_MYSQL);

		} catch (ClassNotFoundException e) { // catchService 실패했을 경우
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonObject.put("result", StaticVariables.FAIL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage()); // 주로 커넥션 문제
				jsonObject.put("result", StaticVariables.ERROR_MYSQL);
			}
			pw.println(jsonObject);
		}
	}

	// 라즈베리파이로 명령 보내기
	private String sendPost(String command) throws Exception{
		getServletContext().log("1");
		URL url = new URL("http://www.naver.com"); // 요청을 보낼 URL
		HttpURLConnection con = null;
		String result = null; // 통신 결과

		// add request header
		String urlParameters ="";// "order=" + command + "";

		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		
		// Send post request
		con.setDoOutput(true); // to use the url connection for output
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url.toString());
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		return response.toString();
	}
}
