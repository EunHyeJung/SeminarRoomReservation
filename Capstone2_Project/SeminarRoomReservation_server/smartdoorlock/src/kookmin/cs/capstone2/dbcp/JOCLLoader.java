package kookmin.cs.capstone2.dbcp;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/*
 * 파일명 : JOCLLoader.java
 * 기능 : web.xml에 JOCLLoader를 선언해줬기 때문에 사용자가 요청하지 않아도 메모리에 load되면서 init() 메서드가 호출되어 jocl의 내용을 
 * 읽어 초기화시키게 된다.
 */
public class JOCLLoader extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		String driver = config.getServletContext().getInitParameter("jdbc");
		try{
			Class.forName(driver); //web.xml에 <context-param>으로 선언된 것 중 파라미터명이 jdbc인 것의 value를 리턴받는다
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
