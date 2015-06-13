package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderRight, btnHeaderLeft;
    TextView txtCenterTitle;
    ImageButton btnAddPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        btnHeaderRight = (ImageView)findViewById(R.id.btnHeaderRight);
        btnHeaderLeft = (ImageView)findViewById(R.id.btnHeaderLeft);

        txtCenterTitle = (TextView)findViewById(R.id.txtCenterTitle);

        btnAddPackage = (ImageButton)findViewById(R.id.btnAddPackage);

        btnHeaderRight.setImageResource(R.drawable.ic_exit_to_app);
        btnHeaderLeft.setImageResource(R.drawable.ic_account_circle);

        txtCenterTitle.setText(GlobalVars.GVUsername);

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
                Intent intUserProfile = new Intent(MainScreenActivity.this, UserProfileActivity.class);
                finish();
                startActivity(intUserProfile);

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
}