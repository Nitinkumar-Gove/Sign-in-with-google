package com.nrgove.model;

public class UserContainer {

	private static User mUser;

	private UserContainer() {
	}

	public static User getUser() {
		if (mUser == null) {
			synchronized (UserContainer.class) { //### double-checked locking on synchronized object, makes this thread safe
				if(mUser == null)
				{
					mUser = new User();
				}
			}
		}
		return mUser;
	}

}
