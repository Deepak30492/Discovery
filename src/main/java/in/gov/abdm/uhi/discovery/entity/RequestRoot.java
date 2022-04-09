package in.gov.abdm.uhi.discovery.entity;

import in.gov.abdm.uhi.discovery.service.beans.Context;
import in.gov.abdm.uhi.discovery.service.beans.Message;

public class RequestRoot {
	private Message message;
	private Context context;
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	
}
