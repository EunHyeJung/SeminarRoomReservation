package kookmin.cs.capstone2.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

public class StaticMethods {

	/*
	 * 함수명 : getBody 기능 : 서블릿의 POST 메서드를 사용하면서 request 헤더의 CONTENT-TYPE이
	 * "application/json" 혹은 "multipart/form-data"형식일 때, 요청 값을 request 바디에 직접
	 * 쓴다고 하여 이를 RequestBody post data 라고 한다. 이렇게 전송된 값은
	 * Request.getInputStream() 혹은 Request.getReader()를 통해 직접 읽어와야 한다.
	 */
	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	// 라즈베리파이로 명령 보내기
	public static int rasberrySocket(String command, String roomId) {

		int result = StaticVariables.FAIL;
		
		try {
			String outStr = "command=" + command;
			String serverIp = StaticVariables.room1Url; 
			OutputStreamWriter osw = null;

			Socket socket = new Socket(serverIp, 80);
			System.out.println("소켓 생성 성공");
			
			// 소켓의 입력 스트림과 Reader를 얻는다.
			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String echo = "";

			try {
				osw = new OutputStreamWriter(socket.getOutputStream());
				System.out.println("소켓 생성 후 OutputStreamWriter 생성 성공");
				osw.write(outStr, 0, 9);
				osw.flush(); // 데이터 전송
				System.out.println("데이터 전송");
				
				String tmp;
				while ((tmp = reader.readLine()) != null)
					echo += tmp;
				System.out.println("\tServerEcho : " + echo);
				
				result = StaticVariables.SUCCESS;
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

		} catch (IOException e) {
			System.out.println("IOException이 발생했습니다.");
			e.printStackTrace();
		}
		return result;
	}
	
	// 매개변수로 전달된 날짜(yyyy-MM-dd)와 시작 시간, 끝 시간(hh:mm:ss) 사이에 현재 시간이 포함되는지 여부 판단
	
	public static int checkTime(String date, String startTime, String endTime) throws ParseException{
		
		Date today = new Date(); //오늘 날짜
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //날짜 형식
		String strToday = sdf.format(today);
		
		System.out.println("오늘 날짜 strToday : " + strToday);
		
		String start = date + " " + startTime;
		String end = date + " " + endTime;
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startT = null, endT = null;
		startT = timeFormat.parse(start);
		endT = timeFormat.parse(end);
		
		System.out.println("startT :" + startT.getTime() + ", endT : " + endT.getTime() + ", today.getTime :"
				+ today.getTime()); 
				
		if(!strToday.equals(date))
			return StaticVariables.FAIL;
		if(startT.getTime() > today.getTime())
			return StaticVariables.FAIL;
		if(endT.getTime() < today.getTime())
			return StaticVariables.FAIL;
		
		return StaticVariables.SUCCESS;
	}
}
