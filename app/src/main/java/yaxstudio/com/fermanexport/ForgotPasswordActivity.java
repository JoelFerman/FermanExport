package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import java.util.Random;

public class ForgotPasswordActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderLeft, btnHeaderRight;
    TextView txtCenterTitle;
    EditText txtFPRecoverPasswordEmail;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String PASSWORD_RECOVERY_URL = "http://yaxstudio.host56.com/FEUserPasswordRecoveryWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERID = "ID_USER";
    private static final String TAG_USERNAME = "USERNAME_USER";
    private static final String TAG_ROLE = "ROLE_USER";
    private static final String TAG_PASSWORD = "PASSWORD_USER";

    String GVUsername, GVRole, GVUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);
        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        txtFPRecoverPasswordEmail = (EditText)findViewById(R.id.txtFPRecoverPasswordEmail);

        btnHeaderLeft.setImageResource(R.drawable.ic_arrow_back);
        btnHeaderRight.setImageResource(R.drawable.ic_send);

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnHeaderLeft:

                Intent intForgotPassword = new Intent(ForgotPasswordActivity.this, LoginScreenActivity.class);
                finish();
                startActivity(intForgotPassword);

                break;

            case R.id.btnHeaderRight:

                new RecoverMyPassword().execute();

                break;

            default:

                break;
        }
    }

    public void RecoverPassword()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(txtFPRecoverPasswordEmail.getText().toString(), null, "SMS Message Test! (Sent from FermanExport app)", null, null);

        Toast.makeText(ForgotPasswordActivity.this, "SMS SENT!", Toast.LENGTH_SHORT).show();
    }

    class RecoverMyPassword extends AsyncTask<String, String, String>

    {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute ()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPasswordActivity.this);
            pDialog.setMessage("Attempting for login...");
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

            String Email = txtFPRecoverPasswordEmail.getText().toString();

            try
            {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Email", Email));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(PASSWORD_RECOVERY_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);

                switch (success)
                {
                    case 0:
                    {
                        Log.d("Invalid Credentials", json.toString());

                        Toast.makeText(ForgotPasswordActivity.this, "TEST", Toast.LENGTH_SHORT).show();

                        return json.getString(TAG_MESSAGE);
                    }

                    case 1:
                    {
                        Log.d("Successful Login!", json.toString());

                        //Send Email with username and password information

                        return json.getString(TAG_MESSAGE);
                    }

                    default:

                        break;

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
         * **/

        protected void onPostExecute(String message)
        {

            pDialog.dismiss();

            if (message != null)
            {
                Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }
}
