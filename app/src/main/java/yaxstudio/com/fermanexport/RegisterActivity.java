package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegisterActivity extends Activity implements OnClickListener, OnItemSelectedListener {
    private ProgressDialog pDialog;

    JSONParser JSONParser = new JSONParser();

    private static String INSERT_URL = "http://yaxstudio.host56.com/FESignInWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    EditText txtSIFullName, txtSIUsername, txtSIPassword, txtSIPassword2, txtSIEmail, txtSIPhoneNumber;
    TextView txtCenter;
    ImageView btnHeaderLeft, btnHeaderRight;
    Button btnSIClearFields, btnSIRegister;
    Spinner spinnerClientCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtSIFullName = (EditText)findViewById(R.id.txtSIFullName);
        txtSIUsername = (EditText)findViewById(R.id.txtSIUsername);
        txtSIPassword = (EditText)findViewById(R.id.txtSIPassword);
        txtSIPassword2 = (EditText)findViewById(R.id.txtSIPassword2);
        txtSIEmail = (EditText)findViewById(R.id.txtSIEmail);
        txtSIPhoneNumber = (EditText)findViewById(R.id.txtSIPhoneNumber);

        txtCenter = (TextView)findViewById(R.id.txtCenterTitle);

        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);
        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);

        btnSIClearFields = (Button)findViewById(R.id.btnSIClearFields);
        btnSIRegister = (Button)findViewById(R.id.btnSIRegister);

        btnHeaderLeft.setImageResource(R.drawable.ic_arrow_back);
        btnHeaderRight.setImageResource(R.drawable.ic_person_add);

        txtCenter.setText("Sign Up");

        spinnerClientCountry = (Spinner) findViewById(R.id.spinnerClientCountry);

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
        btnSIClearFields.setOnClickListener(this);
        btnSIRegister.setOnClickListener(this);
        spinnerClientCountry.setOnItemSelectedListener(this);
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

                Intent intRegisterBack = new Intent(RegisterActivity.this, LoginScreenActivity.class);
                finish();
                startActivity(intRegisterBack);

                break;

            case R.id.btnHeaderRight:

                if (txtSIPassword2.getText().toString().equals(txtSIPassword.getText().toString()))
                {
                    new CreateUser().execute();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Password Fields Don't Match!", Toast.LENGTH_SHORT).show();

                    ClearSensitiveFields();
                }

                break;

            case R.id.btnSIClearFields:

                ClearFields();

                break;

            case R.id.btnSIRegister:

                if (txtSIPassword2.getText().toString().equals(txtSIPassword.getText().toString()))
                {
                    new CreateUser().execute();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Password Fields Don't Match!", Toast.LENGTH_SHORT).show();

                    ClearSensitiveFields();
                }

                break;

            default:

                break;
        }

    }

    private void ClearFields()
    {
        txtSIFullName.setText("");
        txtSIUsername.setText("");
        txtSIPassword.setText("");
        txtSIPassword2.setText("");
        txtSIEmail.setText("");
        txtSIPhoneNumber.setText("");

        txtSIFullName.requestFocus();
    }

    private void ClearSensitiveFields()
    {
        txtSIPassword.setText("");
        txtSIPassword2.setText("");

        txtSIPassword.requestFocus();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getItemAtPosition(position).toString().equals("Honduras"))
        {
            GlobalVars.GVAddRole = "CLIENTHN";
        }
        else if (parent.getItemAtPosition(position).toString().equals("United States"))
        {
            GlobalVars.GVAddRole = "CLIENTUSA";
        }
        //GlobalVars.GVAddRole = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), GlobalVars.GVAddRole, Toast.LENGTH_LONG).show();
        //Toast.makeText(parent.getContext(), "Selected Country is " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    class CreateUser extends AsyncTask<String, String, String>
    {
        boolean failure = false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // Check for success tag

            int success, min = 10, max = 999, i1;

            Random r = new Random();

            i1 = r.nextInt(max - min + 1) + min;

            String UserID = "CLIENT-00" + i1;
            String FullName = txtSIFullName.getText().toString();
            String Username = txtSIUsername.getText().toString();
            String Password = txtSIPassword.getText().toString();
            String Email = txtSIEmail.getText().toString();
            String Phone = txtSIPhoneNumber.getText().toString();
            String Role = GlobalVars.GVAddRole;

            try
            {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("UserID", UserID));
                params.add(new BasicNameValuePair("FullName", FullName));
                params.add(new BasicNameValuePair("Username", Username));
                params.add(new BasicNameValuePair("Password", Password));
                params.add(new BasicNameValuePair("Email", Email));
                params.add(new BasicNameValuePair("Phone", Phone));
                params.add(new BasicNameValuePair("Role", Role));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = JSONParser.makeHttpRequest(INSERT_URL, "POST", params);

                // full json response
                Log.d("Register attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    Log.d("User Created!", json.toString());

                    return json.getString(TAG_MESSAGE);
                }
                else
                {

                    Log.d("Register Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once product deleted
            pDialog.dismiss();

            if (file_url != null)
            {
                Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
                ClearFields();
            }
        }
    }
}