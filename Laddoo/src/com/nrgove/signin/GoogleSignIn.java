package com.nrgove.signin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;


import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.gson.Gson;
import com.nrgove.laddoo.LandingActivity;
import com.nrgove.model.User;
import com.nrgove.model.UserContainer;

import android.app.Activity;
import android.util.Log;

public class GoogleSignIn {
	
	
	/**
     * Parses the response and returns the first name of the user.
     * @throws JSONException if the response is not JSON or if first name does not exist in response
     */
    public static void printUserProfile(String jsonResponse) throws JSONException {
      
     /* ### parsing using JSON object
      * JSONObject profile = new JSONObject(jsonResponse);
      
     Log.d("profile","profile" + profile);
      
      Log.d("User Profile Information\n", 
    		  		  "chai moment !!! \n"+	
    		  		 
    				  "Name :" + profile.getString("name") + "\n" +
    				  "Gender :" + profile.getString("gender") + "\n" +
    				  "Given name :" + profile.getString("given_name") + "\n" + 
    				  "Link :" + profile.getString("link") + "\n" +
    				  "locale :" + profile.getString("locale")+"\n"+
    				  "family name :" +profile.getString("family_name")+"\n"+
    				  "picture :" + profile.getString("picture") + "\n");*/
    	
    	
    	// ### parsing using GSON
    	
    	Gson mUserBuilder = new Gson();
    	
    	User mUser = UserContainer.getUser() ;
    	
    	mUser = mUserBuilder.fromJson(jsonResponse, User.class);
    	
    	if(mUser !=null)
    	{
    		mUser.printUserProfile();
    	}
    }
    
    
    /**
	 * Get Google Authentication Token
	 * @return token - google authentication token
	 * @throws IOException
	 */
	public static String getGoogleAuthToken(Activity mActivity, String mEmail, String mScope) throws IOException {
		try {
			return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
		}catch (UserRecoverableAuthException userRecoverableException) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
			((LandingActivity) mActivity).handleException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
           
        }

		return null;
	}
	
	/**
	 * 
	 * @param token google OAuth2.0 token for 
	 * @param mActivity
	 * @throws MalformedURLException 
	 */
	public static void fetchUserProfile(String token, Activity mActivity) 
	{
		try
		{
			URL url = new URL(
					"https://www.googleapis.com/oauth2/v1/userinfo?access_token="
							+ token);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			int sc = con.getResponseCode();
			if (sc == 200) {
				InputStream is = con.getInputStream();
	
				printUserProfile(readResponse(is));
	
				is.close();
				
			} else if (sc == 401) {
				GoogleAuthUtil.invalidateToken(mActivity, token);
	
				Log.d("Error ",
						"Server auth error: "
								+ readResponse(con.getErrorStream()));
	
			} else {
				Log.d("Server returned the following error code: " + sc,
						"Error");
	
			}
		}
		catch(MalformedURLException ex)
		{
			ex.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	/**
     * Reads the response from the input stream and returns it as a string.
     */
    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }
        
        Log.d("User Profile Data",new String(bos.toByteArray(), "UTF-8"));
        
        return new String(bos.toByteArray(), "UTF-8");
    }

}
