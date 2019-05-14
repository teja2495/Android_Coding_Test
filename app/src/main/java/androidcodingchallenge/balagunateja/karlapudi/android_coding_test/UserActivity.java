package androidcodingchallenge.balagunateja.karlapudi.android_coding_test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


    }

    class okHttpAsync extends AsyncTask<String, Void, List<User>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<User> doInBackground(String... strings) {
            List<User> UserList;

            UserList = getAPIData(strings[0]);

            return UserList;
        }

        @Override
        protected void onPostExecute(List<User> UserList) {
            super.onPostExecute(UserList);
            RecyclerView.Adapter adapter = new UserAdapter(UserList);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            //count.setText("Displaying "+String.valueOf(UserList.size())+ " results");
            recyclerView.setAdapter(adapter);           //Adapter is set after retreiving the data
        }

    }

    List<User> getAPIData(String strUrl) {

        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line).append('\n');
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("demo", e.getMessage());
        } finally {
            connection.disconnect();
        }

        List<User> UserList = new ArrayList<>();

        // JSON Response Parsing using the result
        try {
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject UserJson = new JSONObject();
                UserJson = jsonArray.getJSONObject(i);
                User User = new User();
                User.setFirstName(UserJson.getString("first_name"));
                User.setLastName(UserJson.getString("last_name"));

                if (UserJson.has("avatar")) {
                    User.setImageURL(UserJson.getString("avatar"));
                }
                UserList.add(User);
            }
        } catch (JSONException e) {
            Log.d("demo", e.getMessage());
            e.printStackTrace();
        }
        return UserList;
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
