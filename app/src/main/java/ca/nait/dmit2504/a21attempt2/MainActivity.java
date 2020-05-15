package ca.nait.dmit2504.a21attempt2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView mJitterListView;
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
        //mJitterText = findViewById(R.id.activity_jitters_text);
        mJitterListView = findViewById(R.id.jitters_listview);
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
                String responseBodyText = response.body();
                String[] responseBodyArray = responseBodyText.split("\r\n");
                final int size = responseBodyArray.length;

                ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
                for (int i = 0; i < size; i++)
                {
                    String temp = responseBodyArray[i];
                    String[] tempArray = temp.split("[*][*][*]|from");
                    //String test = tempArray[0] + " " + tempArray[1] + " " + tempArray[2];
                    //create Hashmap for custom listview?
                    //Toast.makeText(getApplicationContext(),test + " ", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),temp + "", Toast.LENGTH_SHORT).show();
                    //do something with i


                    HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
                    hashMap.put("date",tempArray[0]);
                    hashMap.put("sender",tempArray[1]+"");
                    hashMap.put("text",tempArray[2]);
                    arrayList.add(hashMap);//add the hashmap into arrayList

                }
                String[] from={"date","sender","text"};//string array
                int[] to={R.id.date_list_view,R.id.sender_list_view,R.id.message_list_view};//int array of views id's
                SimpleAdapter simpleAdapter=new SimpleAdapter(getApplicationContext(),arrayList,R.layout.list_view,from,to);//Create object and set the parameters for simpleAdapter
                mJitterListView.setAdapter(simpleAdapter);//sets the adapter for listView

                //ArrayAdapter<String> jittersAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, responseBodyArray);
                //mJitterListView.setAdapter(jittersAdapter);
                Log.i(TAG, "Succesfully getting data" + responseBodyText);

               //mJitterText.setText(response.body());


                //split response

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failure to get data");
            }
        });

    }
}
