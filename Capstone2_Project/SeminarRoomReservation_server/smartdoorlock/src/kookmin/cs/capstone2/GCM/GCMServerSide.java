package kookmin.cs.capstone2.GCM;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import com.google.android.gcm.server.Message;

import com.google.android.gcm.server.MulticastResult;

import com.google.android.gcm.server.Result;

import com.google.android.gcm.server.Sender;

public class GCMServerSide {

	public void sendMessage() throws IOException {

		Sender sender = new Sender("AIzaSyB4L3iqk93D8Z4T2OZYIWO4p0SqhSodPDY");

		String regId = "";

		//실제로 보내는 메시지를 정하는 코드, msg라는 값 안에 push nofify를 넣어서 전달
		Message message = new Message.Builder().addData("msg", "push notify").build();

		List<String> list = new ArrayList<String>();

		list.add(regId);

		MulticastResult multiResult = sender.send(message, list, 5);

		if (multiResult != null) {

			List<Result> resultList = multiResult.getResults();

			for (Result result : resultList) {

				System.out.println(result.getMessageId());

			}

		}

	}

	public static void main(String[] args) throws Exception {

		GCMServerSide s = new GCMServerSide();

		s.sendMessage();

	}

}