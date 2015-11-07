package kookmin.cs.capstone2.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;

public class MyHttpServlet extends HttpServlet{
	
	//변수
	Connection conn = null; //DB 연결을 위한 Connection 객체
	Statement stmt = null; //ready for DB Query result
	ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

}
