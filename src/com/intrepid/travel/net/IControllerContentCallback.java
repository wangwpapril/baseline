package com.intrepid.travel.net;

public interface IControllerContentCallback {

	public void handleSuccess(String content);

	public void handleError(Exception e);
}
