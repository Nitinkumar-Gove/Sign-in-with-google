package com.nrgove.model;

import android.util.Log;

public class User {
	
	/*{ #JSON_FORMAT
	 * "picture":"https:\/\/lh4.googleusercontent.com\/-zr5-Oh-Gk28\/AAAAAAAAAAI\/AAAAAAAAC9Y\/xfxS3lMVt2s\/photo.jpg","id":"106417723679517659585",
		"locale":"en",
		"link":"https:\/\/plus.google.com\/+NitinKumarGove",
		"name":"NitinKumar Gove",
		"gender":"male",
		"family_name":"Gove",
		"given_name":"NitinKumar"
		}*/

	// create a member variable for each attribute in json
	public String picture;
	public String locale;
	public String link;
	public String name;
	public String gender;
	public String family_name;
	public String given_name;
	
	public void printUserProfile()
	{
		Log.d("User Profile Information\n", 
		  		  "chai moment !!! \n"+	
		  		 
				  "Name :" + name + "\n" +
				  "Gender :" + gender + "\n" +
				  "Given name :" + given_name + "\n" + 
				  "Link :" + link+ "\n" +
				  "locale :" + locale+"\n"+
				  "family name :" +family_name+"\n"+
				  "picture :" + picture + "\n");
	}
	
	
	
}
