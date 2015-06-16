package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AddPackageActivity extends Activity implements OnClickListener
{
    private ProgressDialog pDialog;

    JSONParser JSONParser = new JSONParser();

    private static String ADD_PACKAGE_URL = "http://yaxstudio.host56.com/FEAddPackageWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    EditText txtAPTitle, txtAPServiceCarrier, txtAPTrackingNumber, txtAPDetail;
    ImageView btnHeaderLeft, btnHeaderRight;
    TextView txtCenterTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);
        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        txtAPTitle = (EditText)findViewById(R.id.txtAPTitle);
        txtAPServiceCarrier = (EditText)findViewById(R.id.txtAPServiceCarrier);
        txtAPTrackingNumber = (EditText)findViewById(R.id.txtAPTrackingNumber);
        txtAPDetail = (EditText)findViewById(R.id.txtAPDetail);

        btnHeaderLeft.setImageResource(R.drawable.ic_arrow_back);
        btnHeaderRight.setImageResource(R.drawable.ic_file_upload);

        txtCenterTitle.setText("Add Package");

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
    }

    public void ClearFields()
    {
        txtAPServiceCarrier.setText("");
        txtAPTrackingNumber.setText("");
        txtAPDetail.setText("");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnHeaderLeft:

                Intent intAddPackageBack = new Intent(AddPackageActivity.this, MainScreenActivity.class);
                finish();
                startActivity(intAddPackageBack);

                break;

            case R.id.btnHeaderRight:

                new AddPackageHN().execute();

                break;

            default:

                break;
        }

    }

    class AddPackageHN extends AsyncTask<String, String, String>
    {
        boolean failure = false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPackageActivity.this);
            pDialog.setMessage("Adding Package...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // Check for success tag

            GlobalMethods GVM = new GlobalMethods();
            GVM.RandomizeAlphaNumeric();
            String idTokenHN = GlobalVars.GVTokenHN;

            int success, min = 10000, max = 99999, i1;

            Random r = new Random();

            i1 = r.nextInt(max - min + 1) + min;

            String PackageIDHN = "PKGHN-" + i1;
            String Title = txtAPTitle.getText().toString();
            String ServiceCarrier = txtAPServiceCarrier.getText().toString();
            String TrackingNumber = txtAPTrackingNumber.getText().toString();
            String Details = txtAPDetail.getText().toString();
            String StatusPKG = "0";
            String ShippingPrice = "0";
            String PaymentStatus = "0";
            String TokenHN = idTokenHN.toString();
            String UserID = GlobalVars.GVUserID;

            try
            {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("PackageIDHN", PackageIDHN));
                params.add(new BasicNameValuePair("Title", Title));
                params.add(new BasicNameValuePair("ServiceCarrier", ServiceCarrier));
                params.add(new BasicNameValuePair("TrackingNumber", TrackingNumber));
                params.add(new BasicNameValuePair("Details", Details));
                params.add(new BasicNameValuePair("StatusPKG", StatusPKG));
                params.add(new BasicNameValuePair("ShippingPrice", ShippingPrice));
                params.add(new BasicNameValuePair("PaymentStatus", PaymentStatus));
                params.add(new BasicNameValuePair("TokenHN", TokenHN));
                params.add(new BasicNameValuePair("UserID", UserID));

                Log.d("request!", "starting");
                Log.d("INSERT String -> ", "ID: " + PackageIDHN + "Title: " + Title + "Carrier: " + ServiceCarrier + "Tracking: " + TrackingNumber + "Details: " + Details + "Status: " + StatusPKG + "Shipping: " + ShippingPrice + "Payment: " + PaymentStatus + "TokenHN: " + TokenHN + "ID_User: " + UserID );

                //Posting user data to script
                JSONObject json = JSONParser.makeHttpRequest(ADD_PACKAGE_URL, "POST", params);

                // full json response
                Log.d("Add Package attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    Log.d("Package Added!", json.toString());

                    return json.getString(TAG_MESSAGE);
                }
                else
                {

                    Log.d("Add Package Failure!", json.getString(TAG_MESSAGE));

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
                Toast.makeText(AddPackageActivity.this, file_url, Toast.LENGTH_LONG).show();
                ClearFields();
            }
        }
    }
}