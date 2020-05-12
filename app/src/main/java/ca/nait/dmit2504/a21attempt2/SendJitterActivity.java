package ca.nait.dmit2504.a21attempt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SendJitterActivity extends AppCompatActivity {

    private static final String TAG = SendJitterActivity.class.getSimpleName();
    private EditText mNameText;
    private EditText mMessageText;
    private Button mSendButton;

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
        setContentView(R.layout.activity_send_jitter);

        //find all view in layout
        mNameText = findViewById(R.id.activity_send_jitter_name_text);
        mMessageText = findViewById(R.id.activity_send_jitter_message_text);
        mSendButton = findViewById(R.id.activity_send_jitter_send_button);

        //add a click event handler to send button using java8 lambda expression
        mSendButton.setOnClickListener((View view) -> {
            //generate implementation of Retrofit interface
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.youcode.ca")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            YoucodeService youcodeService = retrofit.create(YoucodeService.class);

            //call a method in your service
            Call<String> getCall = youcodeService.postJitter(mNameText.getText().toString(),mMessageText.getText().toString());
            getCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i(TAG, "Post was successful");
                    mNameText.setText("");
                    mMessageText.setText("");
                    Toast.makeText(SendJitterActivity.this, "Post was successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "Post failed");
                }
            });
        });



    }

}
