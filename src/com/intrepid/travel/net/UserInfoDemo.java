package com.intrepid.travel.net;

import java.io.Serializable;

import org.json.JSONException;

import com.google.mitijson.annotations.SerializedName;

public class UserInfoDemo implements IContentParms,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6549582935618961748L;
	
	@SerializedName("user")
	private UserDemo user;

	public UserDemo getUser() {
		return user;
	}

	public void setUser(UserDemo user) {
		this.user = user;
	}

	@Override
	public String getparmStr() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
	
}