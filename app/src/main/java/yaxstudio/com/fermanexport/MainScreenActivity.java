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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreenActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderRight, btnHeaderLeft;
    TextView txtCenterTitle, txtLSTTrackingNumber, txtLSTServiceCarrier, txtLSTTitle, txtOngoingPackagesHN;
    ImageButton btnAddPackage;
    ListView lstMSUserPackage;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //URL to get JSON Array
    private static String GET_USER_PACKAGES_URL = "http://yaxstudio.host56.com/FEShowPackagesHNWS.php";
    private static String GET_USER_ONGOING_PACKAGES_URL = "http://yaxstudio.host56.com/FECountPackagesHNWS.php";
    private static String GET_USER_READYTODELIVER_PACKAGES_URL = "http://yaxstudio.host56.com/FEShowPackagesHNWS.php";

    //JSON Node Names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ONGOINGCOUNTHN = "ongoingpkgcounthn";
    private static final String TAG_ARRAYTITLE = "UserPackages";
    private static final String TAG_TITLEPKG = "Title_pkghn";
    private static final String TAG_SERVICECARRIER = "ServiceCarrier_pkghn";
    private static final String TAG_TRACKINGNUMBER = "TrackingNumber_pkghn";

    JSONArray arrayUserPackages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);
        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        txtOngoingPackagesHN = (TextView)findViewById(R.id.txtOngoingPackagesHN);

        btnAddPackage = (ImageButton)findViewById(R.id.btnAddPackage);

        lstMSUserPackage = (ListView)findViewById(R.id.lstMSUserPackage);

        btnHeaderRight.setImageResource(R.drawable.ic_exit_to_app);
        btnHeaderLeft.setImageResource(R.drawable.ic_account_circle);

        txtCenterTitle.setText(GlobalVars.GVUsername);

        new getUserPackages().execute();

        btnHeaderLeft.setOnClickListener(this);
        btnHeaderRight.setOnClickListener(this);
        btnAddPackage.setOnClickListener(this);
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
            {

                new getUserPackagesCount().execute();
//                Intent intUserProfile = new Intent(MainScreenActivity.this, UserProfileActivity.class);
//                finish();
//                startActivity(intUserProfile);

                break;
            }

            case R.id.btnHeaderRight:
            {
                Intent intSignOut = new Intent(MainScreenActivity.this, LoginScreenActivity.class);
                finish();
                startActivity(intSignOut);

                Toast.makeText(MainScreenActivity.this, "You are now signed out!", Toast.LENGTH_SHORT).show();

                break;
            }

            case R.id.btnAddPackage:
            {
                Intent intAddPackage = new Intent(MainScreenActivity.this, AddPackageActivity.class);
                finish();
                startActivity(intAddPackage);

                break;
            }

            default:

                break;
        }

    }

    private class getUserPackages extends AsyncTask<String, String, JSONObject>
    {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            txtLSTTitle = (TextView)findViewById(R.id.txtPackageTitle);
            txtLSTServiceCarrier = (TextView)findViewById(R.id.txtPackageServiceCarrier);
            txtLSTTrackingNumber = (TextView)findViewById(R.id.txtPackageTrackingNumber);

            pDialog = new ProgressDialog(MainScreenActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            JSONParser jParser = new JSONParser();

            String IDUser = GlobalVars.GVUserID;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("IDUser", IDUser));

            // Getting JSON from URL
            JSONObject json = jParser.makeHttpRequest(GET_USER_PACKAGES_URL, "POST", params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json)
        {
            pDialog.dismiss();

            try
            {
                // Getting JSON Array from URL
                arrayUserPackages = json.getJSONArray(TAG_ARRAYTITLE);

                for(int i = 0; i < arrayUserPackages.length(); i++)
                {
                    JSONObject c = arrayUserPackages.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String title = c.getString(TAG_TITLEPKG);
                    String serviceCarrier = c.getString(TAG_SERVICECARRIER);
                    String trackingNumber = c.getString(TAG_TRACKINGNUMBER);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_TITLEPKG, title);
                    map.put(TAG_SERVICECARRIER, serviceCarrier);
                    map.put(TAG_TRACKINGNUMBER, trackingNumber);

                    oslist.add(map);
                    lstMSUserPackage=(ListView)findViewById(R.id.lstMSUserPackage);

                    ListAdapter adapter = new SimpleAdapter(MainScreenActivity.this, oslist, R.layout.list_v, new String[] { TAG_TITLEPKG,TAG_SERVICECARRIER, TAG_TRACKINGNUMBER }, new int[] {R.id.txtPackageTitle,R.id.txtPackageServiceCarrier, R.id.txtPackageTrackingNumber});

                    lstMSUserPackage.setAdapter(adapter);

                    lstMSUserPackage.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {

                            Intent intLSTItemDetails = new Intent(MainScreenActivity.this, PackageDetailsActivity.class);
                            finish();
                            startActivity(intLSTItemDetails);

                            Toast.makeText(MainScreenActivity.this, "You clicked at " + oslist.get(+position).get("Title_pkghn"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    class getUserPackagesCount extends AsyncTask<String, String, String>
    {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute ()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainScreenActivity.this);
            pDialog.setMessage("Getting Packages Count...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            // TODO Auto-generated method stub
            // here Check for success tag
//            int success;
//
//            String ongoingpkghn;
//
//            String IDUser = GlobalVars.GVUserID;

            try
            {
                //int success;

                String ongoingpkghn;

                String IDUser = GlobalVars.GVUserID;

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("IDUser", IDUser));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(GET_USER_ONGOING_PACKAGES_URL, "POST", params);

                // checking  log for json response
                Log.d("Count attempt", json.toString());

                // success tag for json
                //success = json.getInt(TAG_SUCCESS);
                ongoingpkghn = json.getString(TAG_ONGOINGCOUNTHN);

                Log.d("Package Count!", json.toString());

                txtOngoingPackagesHN.setText(ongoingpkghn);

                Toast.makeText(MainScreenActivity.this, GlobalVars.GVUsername + " || " + GlobalVars.GVUserID, Toast.LENGTH_SHORT).show();

                //return json.getString(TAG_MESSAGE);
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
                Toast.makeText(MainScreenActivity.this, message, Toast.LENGTH_LONG).show();
            }

        }
    }
}