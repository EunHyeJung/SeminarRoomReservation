package kookmin.cs.capstone2.GCM;


public class GcmSender{

	String API_KEY = "AIzaSyB4L3iqk93D8Z4T2OZYlWO4p0SqhSodPDY";
	
	public void sendPush(String gcmId, String message){
		System.out.println("Sending POST to GCM");
		
		Content content = createContent(gcmId, message);
		
		POST2GCM.post(API_KEY, content);
	}
	
	public static Content createContent(String gcmId, String message){

        Content c = new Content();

        c.addRegId(gcmId);
	    c.createData("SmartDoorLock", message);

	    return c;
	}
}
