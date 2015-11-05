package kookmin.cs.capstone2.room;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
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
public class DoorControl extends HttpServlet { // 1.httpservlet 상속
												// 2.drivermanager상속 두 가지 방법

	String command = null;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("UTF-8");
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

		if(command!=null){
		try{
		sendPost(command);
		}catch (Exception e){
			System.out.println("!!");
			e.printStackTrace();
		}
	}
		
		try {

			conn = DriverManager.getConnection(StaticVariables.JOCL); // 커넥션 풀에서대기상태인커넥션을얻는다
			stmt = conn.createStatement(); // DB에 SQL문을 보내기 위한 Statement를 생성

			conn.setAutoCommit(false);// 오토커밋을 false로 지정하여 트랜잭션 조건을 맞춘다

			String sql = // 방 잠금 장치 상태 변경 명령
			"update room set status=" + command + " where id=" + roomId + ";";
			int updateResult = stmt.executeUpdate(sql);// return the row count for SQL DML statements

			if (updateResult == 1) { // update 성공했을 경우

				// 출입 기록 삽입
				sql = "insert into roomhistory (room_id, user_id, command) values "
						+ "(" + roomId + ", " + userId + ", " + command + ");";
				int insertResult = stmt.executeUpdate(sql); // roomhistory에 기록
															// 추가

			/*	if (insertResult == 1) {
					String result = sendPost(command);
					if (result.equals(command)) {
						jsonObject.put("result", StaticVariables.SUCCESS);
						conn.commit();
					} else {
						jsonObject.put("result", StaticVariables.FAIL);
						conn.rollback();
					}
				}*/
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
	private int sendPost(String command){
		
		int result = -1;
		try{
			String outStr = "command=" + command;
			String serverIp = "203.246.112.200";
			OutputStreamWriter osw=null;
			
			Socket socket = new Socket(serverIp, 80);
			System.out.println("소켓 생성 성공");
			// 소켓의 입력 스트림과 Reader를 얻는다.
			//////////////////////
			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String echo="";
			////////////////////////
			Timer t = new Timer();
			TimerTask task1 = new MyTimeTask();
			
			try {
				osw=new OutputStreamWriter(socket.getOutputStream());
				System.out.println("소켓 생성 후 OutputStreamWriter 생성 성공");
				osw.write(outStr, 0, 9);
			    osw.flush(); //데이터 전송
			    String tmp;
			    while((tmp=reader.readLine())!=null)
			    	echo += tmp;
			    System.out.println("\tServerEcho : " + echo);

			    System.out.println("데이터 전송");
			} catch (IOException e) {
				System.out.println("OutputSteamWriter 생성 실패");
				System.exit(-1);
				e.printStackTrace();
			}

			System.out.println("연결을 종료합니다.");		
			
			 try {
			    osw.close();
			    in.close();
			    reader.close();
			    socket.close();
			    System.out.println("소켓 닫음");
			 } catch (IOException e) {
			    System.out.println("소켓을 닫는데 실패했습니다.");
			 }
			 
		}catch (IOException e) {
			System.out.println("IOException이 발생했습니다.");
			e.printStackTrace();
		}
		return result;
	}
	
	public class MyTimeTask extends TimerTask {
		@Override
		public void run(){
			System.out.println("이것은 TimeTask 작업입니다.");
		}
	}
}
