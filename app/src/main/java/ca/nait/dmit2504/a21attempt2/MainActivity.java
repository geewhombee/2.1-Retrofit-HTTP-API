package ca.nait.dmit2504.a21attempt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName(); //get simple name  = MainActivity
    private TextView mJitterText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create an instance of the Menu inflator
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu
        inflater.inflate(R.menu.options_menu, menu);
        // Return true if the menu inflated OK
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Get the id of the selected menu item
        switch (item.getItemId()) {
            case R.id.menu_item_list_jitters:
                Intent listJittersIntent = new Intent(this, MainActivity.class);
                startActivity(listJittersIntent);
                return true;
            case R.id.menu_item_post_jitter:
                Intent postJittersIntent = new Intent(this, SendJitterActivity.class);
                startActivity(postJittersIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find textview
        mJitterText = findViewById(R.id.activity_jitters_text);

        //generate implementation of Retrofit interface
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.youcode.ca")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        YoucodeService youcodeService = retrofit.create(YoucodeService.class);

        //call a method in your service
        Call<String> getCall = youcodeService.listJSONServlet();
        getCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "Succesfully getting data");
                mJitterText.setText(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failure to get data");
            }
        });

    }
}
