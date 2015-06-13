package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageDetailsActivity extends Activity implements OnClickListener
{
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String PACKAGE_INFO_URL = "http://yaxstudio.host56.com/FEGetPackageInfoWS.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PACKAGEID = "ID_PKG";
    private static final String TAG_SERVICECARRIER = "SERVICECARRIER_PKG";
    private static final String TAG_TRACKINGNUMBER = "TRACKINGNUMBER_PKG";
    private static final String TAG_PORTARRIVEDATE = "PORTARRIVEDATE_PKG";
    private static final String TAG_DELIVERYDATE = "DELIVERYDATE_PKG";
    private static final String TAG_SHIPPINGPRICE = "SHIPPINGPRICE_PKG";
    private static final String TAG_PAYMENTSTATUS = "PAYMENTSTATUS_PKG";
    private static final String TAG_DETAILS = "DETAIL_PKG";

    ImageView btnHeaderRight, btnHeaderLeft;
    TextView txtCenterTitle, txtPDPackageID, txtPDServiceCarrier, txtPDTrackingNumber, txtPDPortArriveDate, txtPDDeliveryDate;
    TextView txtPDShippingPrice, txtPDPaymentStatus, txtPDDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_admin);

        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);
        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        txtPDPackageID = (TextView)findViewById(R.id.txtPDPackageID);
        txtPDServiceCarrier = (TextView)findViewById(R.id.txtPDServiceCarrier);
        txtPDTrackingNumber = (TextView)findViewById(R.id.txtPDTrackingNumber);
        txtPDPortArriveDate = (TextView)findViewById(R.id.txtPDPortArriveDate);
        txtPDDeliveryDate = (TextView)findViewById(R.id.txtPDDeliveryDate);
        txtPDShippingPrice = (TextView)findViewById(R.id.txtPDShippingPrice);
        txtPDPaymentStatus = (TextView)findViewById(R.id.txtPDPaymentStatus);
        txtPDDetails = (TextView)findViewById(R.id.txtPDDetails);

        btnHeaderRight.setImageResource(R.drawable.ic_exit_to_app);
        btnHeaderLeft.setImageResource(R.drawable.ic_arrow_back);

        txtCenterTitle.setText(GlobalVars.GVUsername);

        new GetPackageInfo().execute();

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnHeaderLeft:

                Intent intPackageDetails = new Intent(PackageDetailsActivity.this, MainScreenActivity.class);
                finish();
                startActivity(intPackageDetails);

                break;

            case R.id.btnHeaderRight:

                Intent intSignOut = new Intent(PackageDetailsActivity.this, LoginScreenActivity.class);
                finish();
                startActivity(intSignOut);

                Toast.makeText(PackageDetailsActivity.this, "You are now signed out!", Toast.LENGTH_SHORT).show();

                break;

            default:

                break;
        }
    }

    class GetPackageInfo extends AsyncTask<String, String, String>

    {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(PackageDetailsActivity.this);
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

                JSONObject json = jsonParser.makeHttpRequest(PACKAGE_INFO_URL, "POST", params);

                // checking  log for json response
                Log.d("Login attempt", json.toString());

                // success tag for json
                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    txtPDPackageID.setText(json.getString(TAG_PACKAGEID));
                    txtPDServiceCarrier.setText(json.getString(TAG_SERVICECARRIER));
                    txtPDTrackingNumber.setText(json.getString(TAG_TRACKINGNUMBER));
                    txtPDPortArriveDate.setText(json.getString(TAG_PORTARRIVEDATE));
                    txtPDDeliveryDate.setText(json.getString(TAG_DELIVERYDATE));
                    txtPDShippingPrice.setText(json.getString(TAG_SHIPPINGPRICE));
                    txtPDPaymentStatus.setText(json.getString(TAG_PAYMENTSTATUS));
                    txtPDDetails.setText(json.getString(TAG_DETAILS));

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
         *
         * Once the background process is done we need to  Dismiss the progress dialog asap
         *
         **/

        protected void onPostExecute(String message)
        {

            pDialog.dismiss();

            if (message != null)
            {
                Toast.makeText(PackageDetailsActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }
}