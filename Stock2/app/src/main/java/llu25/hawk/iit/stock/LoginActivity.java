package llu25.hawk.iit.stock;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import llu25.hawk.iit.stock.adpater.AuthenticateDBHandler;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private AuthenticateDBHandler dbHandler;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //this.deleteDatabase("user.db");
        Log.d(TAG, "onCreate: ");
        dbHandler = new AuthenticateDBHandler(this);
        //dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 1, 1);

        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("userName", null);
        if (restoredText != null) {
            String user_name = prefs.getString("userName", null);//"No name defined" is the default value.
            String user_email = prefs.getString("userEmail", null); //0 is the default value.
            String user_passwd = prefs.getString("userPasswd", null);
            Log.d(TAG, "onCreate: " + user_email + " " + user_passwd);
            _emailText.setText(user_email);
            _passwordText.setText(user_passwd);
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        Log.d(TAG, "------------------------------------------login:  + password" + password);

        // TODO: Implement your own authentication logic here.
        String[] result = new String[3];
        try { result = dbHandler.searchUser(email); }
        catch (Exception e) { onLoginFailed(); }

        Log.d(TAG, "---------------------------------login: " + result[0]);

        if(!result[0].equals(password))
        {
            onLoginFailed();
            progressDialog.dismiss();
            return;
        }
        else
        {
            String user_name = result[1];
            String user_email = result[2];
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("userName", user_name);
            editor.putString("userEmail", user_email);
            editor.putString("userPasswd", password);
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", user_name);
            intent.putExtra("userEmail", user_email);
            startActivity(intent);
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}