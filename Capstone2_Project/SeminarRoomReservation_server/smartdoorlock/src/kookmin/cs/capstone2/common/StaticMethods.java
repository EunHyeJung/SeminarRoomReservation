package kookmin.cs.capstone2.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

public class StaticMethods {

	/*
	 * 함수명 : getBody
	 * 기능 : 서블릿의 POST 메서드를 사용하면서 request 헤더의 CONTENT-TYPE이 "application/json" 혹은
	 *     "multipart/form-data"형식일 때, 요청 값을 request 바디에 직접 쓴다고 하여 이를 RequestBody post data
	 *     라고 한다. 이렇게 전송된 값은 Request.getInputStream() 혹은 Request.getReader()를 통해 직접
	 *     읽어와야 한다.
	 */
	public static String getBody(HttpServletRequest request) throws IOException {
		
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead= -1;
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
}
