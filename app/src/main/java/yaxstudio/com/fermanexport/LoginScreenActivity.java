package yaxstudio.com.fermanexport;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreenActivity extends Activity implements OnClickListener
{
    private EditText user, pass;
    private TextView txtTitle, btnRegisterNewUser, btnForgotPassword;
    private Button btnLogin;
    private ImageView btnHeaderLeft;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://yaxstudio.host56.com/FELoginWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERID = "ID_USER";
    private static final String TAG_FULLNAME = "FULLNAME_USER";
    private static final String TAG_USERNAME = "USERNAME_USER";
    private static final String TAG_PASSWORD = "PASSWORD_USER";
    private static final String TAG_EMAIL = "EMAIL_USER";
    private static final String TAG_PHONENUMBER = "PHONENUMBER_USER";
    private static final String TAG_ROLE = "ROLE_USER";

    String GVUserID, GVFullName, GVUsername, GVEmail, GVPhoneNumber, GVRole ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        user = (EditText)findViewById(R.id.txtUsername);
        pass = (EditText)findViewById(R.id.txtPassword);

        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnRegisterNewUser = (TextView)findViewById(R.id.btnRegisterNewUser);
        txtTitle = (TextView)findViewById(R.id.txtCenterTitle);
        btnForgotPassword = (TextView)findViewById(R.id.btnForgotPassword);

        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);


        txtTitle.setText("Login Screen");

        btnHeaderLeft.setImageResource(R.drawable.ic_person_add);

        btnLogin.setOnClickListener(this);
        btnHeaderLeft.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnRegisterNewUser.setOnClickListener(this);
    }

    public static long back_pressed;
    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.btnHeaderLeft:

                Intent intLSbtnRegister = new Intent(LoginScreenActivity.this, RegisterActivity.class);
                finish();
                startActivity(intLSbtnRegister);

                break;

            case R.id.btnForgotPassword:

                Intent intLSbtnForgotPassword = new Intent(LoginScreenActivity.this, ForgotPasswordActivity.class);
                finish();
                startActivity(intLSbtnForgotPassword);

                break;

            case R.id.btnRegisterNewUser:

                Intent intLSlinkRegister = new Intent(LoginScreenActivity.this, RegisterActivity.class);
                finish();
                startActivity(intLSlinkRegister);

                break;

            case R.id.btnLogin:

                new AttemptLogin().execute();

                break;

            default:

                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String>
    {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute ()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginScreenActivity.this);
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

            String Username = user.getText().toString();
            String Password = pass.getText().toString();

            try
            {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Username", Username));
                params.add(new BasicNameValuePair("Password", Password));


                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // Get and Store Data From JSON
                success = json.getInt(TAG_SUCCESS);
                GVUserID = json.getString(TAG_USERID);
                GVFullName = json.getString(TAG_FULLNAME);
                GVUsername = json.getString(TAG_USERNAME);
                GVEmail = json.getString(TAG_EMAIL);
                GVPhoneNumber = json.getString(TAG_PHONENUMBER);
                GVRole = json.getString(TAG_ROLE);

                // Variables To Compare Credentials
                String passCompare = json.getString(TAG_PASSWORD);
                String userCompare = json.getString(TAG_USERNAME);

                    switch (success)
                    {
                        case 0:

                            Toast.makeText(LoginScreenActivity.this, "TEST", Toast.LENGTH_SHORT).show();

                            Log.d("Invalid Credentials", json.toString());

                            break;

                        case 1:

                            if (pass.getText().toString().contentEquals(passCompare) && user.getText().toString().contentEquals(userCompare))
                            {
                                Log.d("Successful Login!", json.toString());

                                switch (GVRole)
                                {
                                    case "ADMIN":

                                        // Add User Information on Global Variables
                                        GlobalVars.GVUserID = GVUserID;
                                        GlobalVars.GVFullName = GVFullName;
                                        GlobalVars.GVUsername = GVUsername;
                                        GlobalVars.GVEmail = GVEmail;
                                        GlobalVars.GVPhoneNumber = GVPhoneNumber;
                                        GlobalVars.GVRole = GVRole;

                                        Intent intMainScreenAdmin = new Intent(LoginScreenActivity.this, MainScreenAdminActivity.class);
                                        LoginScreenActivity.this.finish();
                                        startActivity(intMainScreenAdmin);

                                        break;

                                    case "CLIENTHN":

                                        // Add User Information on Global Variables
                                        GlobalVars.GVUserID = GVUserID;
                                        GlobalVars.GVFullName = GVFullName;
                                        GlobalVars.GVUsername = GVUsername;
                                        GlobalVars.GVEmail = GVEmail;
                                        GlobalVars.GVPhoneNumber = GVPhoneNumber;
                                        GlobalVars.GVRole = GVRole;

                                        Intent intMainScreenClientHN = new Intent(LoginScreenActivity.this, MainScreenActivity.class);
                                        LoginScreenActivity.this.finish();
                                        startActivity(intMainScreenClientHN);

                                        break;

                                    case "CLIENTUSA":

                                        // Add User Information on Global Variables
                                        GlobalVars.GVUserID = GVUserID;
                                        GlobalVars.GVFullName = GVFullName;
                                        GlobalVars.GVUsername = GVUsername;
                                        GlobalVars.GVEmail = GVEmail;
                                        GlobalVars.GVPhoneNumber = GVPhoneNumber;
                                        GlobalVars.GVRole = GVRole;

                                        Intent intMainScreenClientUSA = new Intent(LoginScreenActivity.this, MainScreenClientUSAActivity.class);
                                        LoginScreenActivity.this.finish();
                                        startActivity(intMainScreenClientUSA);

                                        break;

                                    case "MANAGER":

                                        // Add User Information on Global Variables
                                        GlobalVars.GVUserID = GVUserID;
                                        GlobalVars.GVFullName = GVFullName;
                                        GlobalVars.GVUsername = GVUsername;
                                        GlobalVars.GVEmail = GVEmail;
                                        GlobalVars.GVPhoneNumber = GVPhoneNumber;
                                        GlobalVars.GVRole = GVRole;

                                        Intent intMainScreenManager = new Intent(LoginScreenActivity.this, MainScreenManagerActivity.class);
                                        LoginScreenActivity.this.finish();
                                        startActivity(intMainScreenManager);

                                        break;

                                    case "DELIVERYTEAM":

                                        // Add User Information on Global Variables
                                        GlobalVars.GVUserID = GVUserID;
                                        GlobalVars.GVFullName = GVFullName;
                                        GlobalVars.GVUsername = GVUsername;
                                        GlobalVars.GVEmail = GVEmail;
                                        GlobalVars.GVPhoneNumber = GVPhoneNumber;
                                        GlobalVars.GVRole = GVRole;

                                        Intent intMainScreenDeliveryTeam = new Intent(LoginScreenActivity.this, MainScreenDeliveryTeamActivity.class);
                                        LoginScreenActivity.this.finish();
                                        startActivity(intMainScreenDeliveryTeam);

                                        break;

                                    default:

                                        break;
                                }

                                return json.getString(TAG_MESSAGE);
                            }
                            else if (!pass.getText().toString().contentEquals(passCompare) && user.getText().toString().contentEquals(userCompare))
                            {
                                Toast.makeText(LoginScreenActivity.this, "Credentials don't match", Toast.LENGTH_LONG).show();

                                break;
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
                Toast.makeText(LoginScreenActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }
}