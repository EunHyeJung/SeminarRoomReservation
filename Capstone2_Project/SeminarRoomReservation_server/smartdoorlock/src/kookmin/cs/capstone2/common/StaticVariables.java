package kookmin.cs.capstone2.common;

public class StaticVariables {

	public static final int FAIL = -1;
	public static final int REJECTION = 0;
	public static final int SUCCESS = 1;
	public static final int ADMIN_SUCCESS = 2;
	public static final int RESERVATION = 2;
	public static final int ERROR_MYSQL = 3;
	public static final String JOCL = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
	
	//각 방에 따른 ip 주소
	public static final String room1Url = "203.246.112.200";
}
