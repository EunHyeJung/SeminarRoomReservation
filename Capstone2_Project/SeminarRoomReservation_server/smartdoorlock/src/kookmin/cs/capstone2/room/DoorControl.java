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

import kookmin.cs.capstone2.var.StaticVariables;

/*
 * filename : DoorControl.java
 * 기능 : 
 * 	요청된 세미나실 문을 열고 닫는다.
 */
public class DoorControl extends HttpServlet { //1. httpservlet 상속 2. drivermanager상속 두 가지 방법

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

/*		try {
			catchService(request, response);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} */
		
		// request 파라미터로 전송된 값 얻기
		String id = request.getParameter("id");
		String roomName = request.getParameter("roomName");
		String command = request.getParameter("command");

		System.out.println(id + " " + roomName + " " + command);
		
		String jocl = "jdbc:apache:commons:dbcp:/pool1"; // 커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; // DB 연결을 위한 Connection 객체
		Statement stmt = null; // ready for DB Query result
		PrintWriter pw = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		try {

			conn = DriverManager.getConnection(jocl); // 커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); // DB에 SQL문을 보내기 위한 Statement를 생성
			
			//오토커밋을 false로 지정하여 트랜잭션 조건을 맞춘다
			conn.setAutoCommit(false);
			
			//sql문 update 결과 확인을 위한 변수
			int updateResult1, updateResult2;
			
			//라즈베리파이에 먼저 요청을 보내서 response가 온 후에! DB를 변경해 주어야 한다
			
			//방 잠금 장치 상태 변경 명령
			String sql = "update room set status=" + command + " where room_id='" + roomName +"';";// + orderFlag + " where id=" + roomId + ";";
			updateResult1 = stmt.executeUpdate(sql);// return the row count for SQL DML statements
			
			//방 이름으로 방 아이디 얻기
			ResultSet roomRs = stmt.executeQuery("select id from room where room_id='" + roomName + "';");
			if (roomRs.next()){
				int roomId;
				roomId = roomRs.getInt("id");
			
				//룸 히스토리 추가
				sql = "insert into roomhistory (room_id, user_id, command) values (" + roomId + ", " + id + ", " +  command + ");";
				updateResult2 = stmt.executeUpdate(sql);
				
				if (updateResult1 == 1 && updateResult2 == 1){
					conn.commit();
					jsonObject.put("result", StaticVariables.SUCCESS);
				} else {
					jsonObject.put("result", StaticVariables.FAIL);
					conn.rollback();
				}
			}
		} catch (SQLException e) { //sql이 잘못됫거나.. 
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			if(conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			e.printStackTrace();
			jsonObject.put("result", StaticVariables.ERROR_MYSQL);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage()); //주로 커넥션 문제 
				jsonObject.put("result", StaticVariables.ERROR_MYSQL);
			}
			pw.println(jsonObject);
		}
	}

/*	private void catchService(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, ClassNotFoundException,
			SQLException {
		URL url = new URL("http://203.246.112.200:6666"); // 요청을 보낼 URL
		int send = 1;// 지역변수..이용
		HttpURLConnection con = null;

		try {
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.connect();

			// 데이터 송신
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.write(send);
			int resCode = con.getResponseCode();
			if (resCode == HttpURLConnection.HTTP_OK) {
				String result = read(con);
				ServletOutputStream out = res.getOutputStream();
				System.out.println(result);
			} else {
				throw new IOException("ERROR : Communication Error\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconnect();
		}
	}

	*//**
	 * 수신하는 부분
	 * 
	 * @param p_con
	 * @throws IOException
	 *//*
	private String read(HttpURLConnection con) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String strData = null;
		StringBuffer sb = new StringBuffer();
		while ((strData = br.readLine()) != null) {
			sb.append(strData);
		}
		return new String(sb.toString().getBytes());
	}*/
}
