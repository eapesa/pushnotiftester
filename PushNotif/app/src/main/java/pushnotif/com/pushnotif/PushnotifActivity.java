package pushnotif.com.pushnotif;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;

public class PushnotifActivity extends Activity {

    Button getRegistrationIdButton, clearRegistrationButton;
    EditText registrationIdEditText;

    public static final String TAG = PushnotifActivity.class.getSimpleName();
    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushnotif);

        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d(TAG, "ANDROID ID: " + android_id);
        registrationIdEditText = (EditText) findViewById(R.id.activity_pushnotif_regid_result);

        getRegistrationIdButton = (Button) findViewById(R.id.activity_pushnotif_button_reg);
        getRegistrationIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getGCMRegistrationId().execute();
            }
        });

        clearRegistrationButton = (Button) findViewById(R.id.activity_pushnotif_button_clear);
        clearRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationIdEditText.setText("");
            }
        });
    }

    private class getGCMRegistrationId extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... postParameters) {
            String REG_ID = gcmRegister();
            return REG_ID;
        }

        protected void onProgressUpdate() {

        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "On POST EXEC");
            if (result != null) {
                registrationIdEditText.setText(result);
            } else {
                registrationIdEditText.setText("Please try again...");
            }
        }

        public String gcmRegister() {
            String SENDER_ID = Constants.SENDER_ID;

            Log.d(TAG, "SENDER_ID: " + SENDER_ID);
            try {
                String REG_ID = gcm.register(SENDER_ID);
                Log.d(TAG, "REG ID: " + REG_ID);

                if (REG_ID != null) {
                    return REG_ID;
                } else {
                    return null;
                }
            } catch (IOException e) {
                Log.d(TAG, "EXCEPTION: " + e);
                return null;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pushnotif, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
