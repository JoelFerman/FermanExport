package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainScreenClientUSAActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderRight, btnHeaderLeft;
    TextView txtCenterTitle, txtLSTID, txtLSTReceiver, txtLSTSender;
    ListView lstMSUSAPackage;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String GET_USER_PACKAGES_URL = "http://yaxstudio.host56.com/FEShowPackagesUSAWS.php";

    //JSON Node Names
    private static final String TAG_ARRAYTITLE = "UserPackagesUSA";
    private static final String TAG_PACKAGEIDUSA = "ID_pkgusa";
    private static final String TAG_RECEIVER = "Receiver_pkghn";
    private static final String TAG_SENDER = "Sender_pkghn";

    JSONArray arrayUserPackagesUSA = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_admin);

        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);
        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        btnHeaderRight.setImageResource(R.drawable.ic_exit_to_app);
        btnHeaderLeft.setImageResource(R.drawable.ic_account_circle);

        txtCenterTitle.setText(GlobalVars.GVUsername);

        new getUserPackagesUSA().execute();

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

                Intent intUserProfile = new Intent(MainScreenClientUSAActivity.this, UserProfileActivity.class);
                this.finish();
                startActivity(intUserProfile);

                break;

            case R.id.btnHeaderRight:

                Intent intSignOut = new Intent(MainScreenClientUSAActivity.this, LoginScreenActivity.class);
                finish();
                startActivity(intSignOut);

                Toast.makeText(MainScreenClientUSAActivity.this, "You are now signed out!", Toast.LENGTH_SHORT).show();

                break;

            default:

                break;
        }
    }

    private class getUserPackagesUSA extends AsyncTask<String, String, JSONObject>
    {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            txtLSTID = (TextView)findViewById(R.id.txtPackageLine1);
            txtLSTReceiver = (TextView)findViewById(R.id.txtPackageLine2);
            txtLSTSender = (TextView)findViewById(R.id.txtPackageLine3);

            pDialog = new ProgressDialog(MainScreenClientUSAActivity.this);
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
                arrayUserPackagesUSA = json.getJSONArray(TAG_ARRAYTITLE);

                for(int i = 0; i < arrayUserPackagesUSA.length(); i++)
                {
                    JSONObject c = arrayUserPackagesUSA.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String IDPKGUSA = c.getString(TAG_PACKAGEIDUSA);
                    String Receiver = c.getString(TAG_RECEIVER);
                    String Sender = c.getString(TAG_SENDER);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_PACKAGEIDUSA, IDPKGUSA);
                    map.put(TAG_RECEIVER, Receiver);
                    map.put(TAG_SENDER, Sender);

                    oslist.add(map);
                    lstMSUSAPackage = (ListView)findViewById(R.id.lstMSUSAPackage);

                    ListAdapter adapter = new SimpleAdapter(MainScreenClientUSAActivity.this, oslist, R.layout.list_v, new String[] { TAG_PACKAGEIDUSA,TAG_RECEIVER, TAG_SENDER }, new int[] {R.id.txtPackageLine1,R.id.txtPackageLine2, R.id.txtPackageLine3});

                    lstMSUSAPackage.setAdapter(adapter);

                    lstMSUSAPackage.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {

                            Intent intLSTItemDetails = new Intent(MainScreenClientUSAActivity.this, PackageDetailsActivity.class);
                            finish();
                            startActivity(intLSTItemDetails);

                            Toast.makeText(MainScreenClientUSAActivity.this, "You clicked at " + oslist.get(+position).get(TAG_PACKAGEIDUSA), Toast.LENGTH_SHORT).show();
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
}