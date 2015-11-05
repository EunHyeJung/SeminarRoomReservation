package kookmin.cs.capstone2.GCM;


public class GcmSender{

	String API_KEY = "AIzaSyB4L3iqk93D8Z4T2OZYlWO4p0SqhSodPDY";
	
	public void sendPush(){
		System.out.println("Sending POST to GCM");
		
		Content content = createContent();
		
		POST2GCM.post(API_KEY, content);
	}
	
	 public static Content createContent(){

	        Content c = new Content();

	        c.addRegId("dptmTBOZX-Y:APA91bH1ADWENUzXftIFmwd2RE7Tx0s8NQaw8dO69MBV2PddAf274KTN75HTataPf_2fEL4wVcqYu0I3VPeksVFE8hl9PM2mPi-ac0GmzAIKpMZPZbI2utFdckAncZeJC0P3JcGLrOAs");
	        c.createData("Test Title", "It's ME!");

	        return c;
	    }
}
