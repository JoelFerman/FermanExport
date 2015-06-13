package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderLeft, btnHeaderRight;
    TextView txtCenterTitle, txtUPUserID, txtUPUserRole;
    EditText txtUPFullname, txtUPUsername, txtUPEmail, txtUPPhoneNumber;

    int tagID = 1;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String USER_INFO_URL = "http://yaxstudio.host56.com/FEUserProfileWS.php";
    private static final String UPDATE_INFO_URL = "http://yaxstudio.host56.com/FEUpdateUserProfileWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERID = "ID_USER";
    private static final String TAG_FULLNAME = "FULLNAME_USER";
    private static final String TAG_USERNAME = "USERNAME_USER";
    private static final String TAG_EMAIL = "EMAIL_USER";
    private static final String TAG_PHONE = "PHONENUMBER_USER";
    private static final String TAG_ROLE = "ROLE_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnHeaderLeft = (ImageView) findViewById(R.id.btnHeaderLeft);
        txtCenterTitle = (TextView) findViewById(R.id.txtCenterTitle);
        btnHeaderRight = (ImageView) findViewById(R.id.btnHeaderRight);

        txtUPUserID = (TextView) findViewById(R.id.txtUPUserID);
        txtUPUserRole = (TextView) findViewById(R.id.txtUPUserRole);

        txtUPFullname = (EditText) findViewById(R.id.txtUPFullName);
        txtUPUsername = (EditText) findViewById(R.id.txtUPUsername);
        txtUPEmail = (EditText) findViewById(R.id.txtUPEmail);
        txtUPPhoneNumber = (EditText) findViewById(R.id.txtUPPhoneNumber);

        EditableText();

        btnHeaderLeft.setImageResource(R.drawable.ic_arrow_back);
        btnHeaderRight.setImageResource(R.drawable.ic_mode_edit);

        new GetUserInfo().execute();

        NonEditableText();

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnHeaderLeft:

                Intent intBackMainScreen = new Intent(UserProfileActivity.this, MainScreenActivity.class);
                EditableText();
                ClearUserInfo();
                finish();
                startActivity(intBackMainScreen);

                break;

            case R.id.btnHeaderRight:

                if (tagID == 1)
                {
                    btnHeaderRight.setImageResource(R.drawable.ic_save);

                    tagID++;

                    EditInfoMode();

                    break;
                }
                else if (tagID == 2)
                {
                    btnHeaderRight.setImageResource(R.drawable.ic_mode_edit);

                    tagID--;

                    new UpdateUserInfo().execute();

                    NonEditableText();

                    break;
                }

                break;
        }
    }

    public void NonEditableText()
    {
        txtUPFullname.setEnabled(false);
        txtUPUsername.setEnabled(false);
        txtUPEmail.setEnabled(false);
        txtUPPhoneNumber.setEnabled(false);

        txtUPFullname.setClickable(false);
        txtUPUsername.setClickable(false);
        txtUPEmail.setClickable(false);
        txtUPPhoneNumber.setClickable(false);
    }

    public void EditableText()
    {
        txtUPFullname.setEnabled(true);
        txtUPUsername.setEnabled(true);
        txtUPEmail.setEnabled(true);
        txtUPPhoneNumber.setEnabled(true);

        txtUPFullname.setClickable(true);
        txtUPUsername.setClickable(true);
        txtUPEmail.setClickable(true);
        txtUPPhoneNumber.setClickable(true);
    }

    public void ClearUserInfo()
    {
        txtUPUserID.setText("");
        txtUPUserRole.setText("");
        txtUPFullname.setText("");
        txtUPUsername.setText("");
        txtUPEmail.setText("");
        txtUPPhoneNumber.setText("");
    }

    public void EditInfoMode()
    {
        txtUPEmail.setEnabled(true);
        txtUPPhoneNumber.setEnabled(true);
    }

    class GetUserInfo extends AsyncTask<String, String, String>

    {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfileActivity.this);
            pDialog.setMessage("Getting User Information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;

            String Username = GlobalVars.GVUsername;

            try
            {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Username", Username));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(USER_INFO_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    txtUPUserID.setText(json.getString(TAG_USERID));
                    txtUPUserRole.setText(json.getString(TAG_ROLE));
                    txtUPFullname.setText(json.getString(TAG_FULLNAME));
                    txtUPUsername.setText(json.getString(TAG_USERNAME));
                    txtUPEmail.setText(json.getString(TAG_EMAIL));
                    txtUPPhoneNumber.setText(json.getString(TAG_PHONE));

                    return json.getString(TAG_MESSAGE);
                }
                else
                {
                    return json.getString(TAG_MESSAGE);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         * *
         */

        protected void onPostExecute(String message)
        {

            pDialog.dismiss();

            if (message != null)
            {
                Toast.makeText(UserProfileActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }


    class UpdateUserInfo extends AsyncTask<String, String, String>

    {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfileActivity.this);
            pDialog.setMessage("Getting User Information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // here Check for success tag
            int success;

            String Username = GlobalVars.GVUsername;
            String Email = txtUPEmail.getText().toString();
            String PhoneNumber = txtUPPhoneNumber.getText().toString();

            try
            {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Username", Username));
                params.add(new BasicNameValuePair("Email", Email));
                params.add(new BasicNameValuePair("PhoneNumber", PhoneNumber));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(UPDATE_INFO_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    return json.getString(TAG_MESSAGE);
                }
                else
                {
                    return json.getString(TAG_MESSAGE);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         * *
         */

        protected void onPostExecute(String message)
        {
            pDialog.dismiss();

            if (message != null)
            {
                Toast.makeText(UserProfileActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}