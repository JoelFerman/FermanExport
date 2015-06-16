package yaxstudio.com.fermanexport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ForgotPasswordActivity extends Activity implements OnClickListener
{
    ImageView btnHeaderLeft, btnHeaderRight;
    TextView txtCenterTitle;
    EditText txtFPRecoverPasswordEmail;

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

                RecoverPassword();

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
}
