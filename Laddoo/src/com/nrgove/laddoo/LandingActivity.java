package com.nrgove.laddoo;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;
import com.nrgove.signin.GoogleSignIn;


public class LandingActivity extends ActionBarActivity {

	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

	private String mSelectedEmail;

	private final static String GPLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

	private SignInButton mGoogleSignIn;
	
	
	
	

	/**
	 * This method is a hook for background threads and async tasks that need to
	 * provide the user a response UI when an exception occurs.
	 * @param e exception which was thrown
	 * @version Build 1.0 July 22,15
	 * @author Nitin Gove
	 */
	public void handleException(final Exception e) {
		// Because this call comes from the AsyncTask, we must ensure that the
		// following
		// code instead executes on the UI thread.
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (e instanceof UserRecoverableAuthException) {
					// Unable to authenticate, such as when the user has not yet
					// granted
					// the app access to the account, but the user can fix this.
					// Forward the user to an activity in Google Play services.
					Intent intent = ((UserRecoverableAuthException) e)
							.getIntent();
					startActivityForResult(intent,
							REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
				}
			}
		});
	}

	/**
	 * This method prompts user to select one of the google accounts configured on his device. It also allows him 
	 * to add new account.
	 * @version Build 1.0 July 22,15
	 * @author Nitin Gove
	 */
	private void pickUserAccount() {
		
	    String[] accountTypes = new String[]{"com.google"};
	    Intent intent = AccountPicker.newChooseAccountIntent(null, null,
	            accountTypes, false, null, null, null, null);
	    startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);

		mGoogleSignIn = (SignInButton) findViewById(R.id.sign_in_button);
	
		mGoogleSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pickUserAccount();
			}
		});
		
		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub

		if (arg0 == REQUEST_CODE_PICK_ACCOUNT) {
			if (arg1 == RESULT_OK) {
				mSelectedEmail = arg2
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

				// display selected account mail id
				Toast.makeText(getApplicationContext(), mSelectedEmail,
						Toast.LENGTH_LONG).show();

				// fetch token based on selected mail id
				if (isDeviceOnline()) {
					new GetUserprofileTask(LandingActivity.this,
							mSelectedEmail, GPLUS_SCOPE).execute();
				} else {
					Toast.makeText(this, R.string.not_online, Toast.LENGTH_LONG)
							.show();
				}
				
			} else if (arg1 == RESULT_CANCELED) {
				// ask user to select atleast one account
				Toast.makeText(getApplicationContext(), R.string.pick_account,
						Toast.LENGTH_LONG).show();
			}
		}else if ((arg0 == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
	            && arg1 == RESULT_OK) {
	        // Receiving a result that follows a GoogleAuthException, try auth again
	        getUserprofile();
	    }

	}
	
	/**
	 * Attempts to retrieve the username.
	 * If the account is not yet known, invoke the picker. Once the account is known,
	 * start an instance of the AsyncTask to get the auth token and do work with it.
	 */
	@SuppressWarnings("unchecked")
	private void getUserprofile() {
	    if (mSelectedEmail == null) {
	        pickUserAccount();
	    } else {
	        if (isDeviceOnline()) {
	        	new GetUserprofileTask(LandingActivity.this,
						mSelectedEmail, GPLUS_SCOPE).execute();
	        } else {
	            Toast.makeText(this, R.string.not_online, Toast.LENGTH_LONG).show();
	        }
	    }
	}
	
	/** Checks whether the device currently has a network connection */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.landing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("rawtypes")
	public  class GetUserprofileTask extends AsyncTask
	{
		private Activity mActivity;
		private String mEmail;
		private String mScope;

		public GetUserprofileTask(Activity activity, String email, String scope) {
			// TODO Auto-generated constructor stub

			mActivity = activity;
			mEmail = email;
			mScope = scope;

		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				String token = GoogleSignIn.getGoogleAuthToken(mActivity,
						mEmail, mScope); // ### fetching OAuth token here
				Log.d("Token ", token + " is the token");

				if (token == null) {
					// error has already been handled in fetchToken()
					return null;
				}

				GoogleSignIn.fetchUserProfile(token, mActivity);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		
	}	
		
}
