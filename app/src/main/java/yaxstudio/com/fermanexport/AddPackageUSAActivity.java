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
import android.widget.ImageButton;
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

public class AddPackageUSAActivity extends Activity implements OnClickListener
{
    private ProgressDialog pDialog;

    JSONParser JSONParser = new JSONParser();

    private static String INSERT_PKGUSA_URL = "http://yaxstudio.host56.com/FEInsertPKGUSAWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    ImageView btnHeaderLeft, btnHeaderRight;
    TextView txtCenterTitle;
    EditText txtAPUSAPersonTo, txtAPUSAPersonFrom, txtAPUSADetail, txtAPUSAPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package_usa);

        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);
        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        txtAPUSAPersonTo = (EditText)findViewById(R.id.txtAPUSAPersonTo);
        txtAPUSAPersonFrom = (EditText)findViewById(R.id.txtAPUSAPersonFrom);
        txtAPUSADetail = (EditText)findViewById(R.id.txtAPUSADetail);
        txtAPUSAPrice = (EditText)findViewById(R.id.txtAPUSAPrice);

        btnHeaderLeft.setImageResource(R.drawable.ic_arrow_back);
        btnHeaderRight.setImageResource(R.drawable.ic_file_upload);

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnHeaderLeft:

                Intent intAddPackageUSA = new Intent(AddPackageUSAActivity.this, MainScreenClientUSAActivity.class);
                finish();
                startActivity(intAddPackageUSA);

                break;

            case R.id.btnHeaderRight:

                new AddPackageUSA().execute();

                break;

            default:

                break;
        }
    }

    private void ClearFields()
    {
        txtAPUSAPersonTo.setText("");
        txtAPUSAPersonFrom.setText("");
        txtAPUSADetail.setText("");
        txtAPUSAPrice.setText("");

        txtAPUSAPersonTo.requestFocus();
    }

    class AddPackageUSA extends AsyncTask<String, String, String>
    {
        boolean failure = false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPackageUSAActivity.this);
            pDialog.setMessage("Adding Package USA...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // Check for success tag

            int success, min = 10, max = 9999999, i1;

            Random r = new Random();

            i1 = r.nextInt(max - min + 1) + min;

            String ID_PKG = "PKGUSA-00" + i1;
            String PersonTo = txtAPUSAPersonTo.getText().toString();
            String PersonFrom = txtAPUSAPersonFrom.getText().toString();
            String Detail = txtAPUSADetail.getText().toString();
            String Price = txtAPUSAPrice.getText().toString();

            try
            {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("ID_PKG", ID_PKG));
                params.add(new BasicNameValuePair("PersonTo", PersonTo));
                params.add(new BasicNameValuePair("PersonFrom", PersonFrom));
                params.add(new BasicNameValuePair("Detail", Detail));
                params.add(new BasicNameValuePair("Price", Price));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = JSONParser.makeHttpRequest(INSERT_PKGUSA_URL, "POST", params);

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
                Toast.makeText(AddPackageUSAActivity.this, file_url, Toast.LENGTH_LONG).show();
                ClearFields();
            }
        }
    }
}
