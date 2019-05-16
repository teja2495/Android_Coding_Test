package androidcodingchallenge.balagunateja.karlapudi.android_coding_test;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateUserActivity extends AppCompatActivity {

    Button submit;
    EditText name, job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        submit = findViewById(R.id.buttonSubmit);
        name = findViewById(R.id.editTextName);
        job = findViewById(R.id.editTextJob);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isConnected()) {
                    Toast.makeText(CreateUserActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                } else if(name.getText().toString().isEmpty() || job.getText().toString().isEmpty()) {
                    Toast.makeText(CreateUserActivity.this, "Missing Values!", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONObject jsonPostBody = new JSONObject();

                        try {
                            String url = "https://reqres.in/api/users";
                            jsonPostBody.put("name", name.getText().toString());
                            jsonPostBody.put("job", job.getText().toString());
                            postData(url, jsonPostBody.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

            }
        });
    }

    void postData(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(CreateUserActivity.this, "Error, User creating Failed! Try Again.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    String msg = "User " + json.getString("name") + " created!";
                    runOnMainThread(CreateUserActivity.this, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnMainThread(CreateUserActivity.this, "Error, User creating Failed! Try Again.");
                }
            }
        });
    }

    void runOnMainThread(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(CreateUserActivity.this, UserActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            });
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI &&
                        networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
