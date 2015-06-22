package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenManagerActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderRight, btnHeaderLeft;
    TextView txtCenterTitle;

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

                Intent intUserProfile = new Intent(MainScreenManagerActivity.this, UserProfileActivity.class);
                this.finish();
                startActivity(intUserProfile);

                break;

            case R.id.btnHeaderRight:

                Intent intSignOut = new Intent(MainScreenManagerActivity.this, LoginScreenActivity.class);
                finish();
                startActivity(intSignOut);

                Toast.makeText(MainScreenManagerActivity.this, "You are now signed out!", Toast.LENGTH_SHORT).show();

                break;

            default:

                break;
        }
    }
}
