package com.example.papound.chatapps;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_Friend extends Activity implements OnClickListener{

    private EditText user, fname;
    private Button  add_friend;
    //public EditText friend;
    //private Login login = new Login();

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php register script

    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String REGISTER_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

    //testing on Emulator:
    private static final String REGISTER_URL = "http://128.199.117.135/webservice/addfriend.php";

    //testing from a real server:
    //private static final String REGISTER_URL = "http://www.mybringback.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        //user = (EditText)findViewById(R.id.username);
        fname = (EditText)findViewById(R.id.f_username);


        add_friend = (Button)findViewById(R.id.add_friends_btn_2);
        add_friend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
                new Addfriend().execute();
    }

    public void friendlist(View v){
        int id = v.getId();
        if(id == R.id.friend_list_btn) {
            Intent j = new Intent(this, Friend_List.class);
            startActivity(j);
        }
    }

    //public String  getUser_name(){
    //Intent x = getIntent();
    //String user_name = x.getStringExtra("username");
    //return user_name;
    //}

    class Addfriend extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Add_Friend.this);
            pDialog.setMessage("Adding Friends...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            Intent i = getIntent();
            String username = i.getStringExtra("username");
            //Log.d("finding username!", login.user_name_ori);
            String fname_str = fname.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                System.out.print(username);
                params.add(new BasicNameValuePair("f_username", fname_str));

                Log.d("request!", "starting");

                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Adding Friends, Please Wait!", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Adding Friends! Complete!", json.toString());
                    //friend = (EditText)findViewById(R.id.f_username);
                    //friend.setText(" ");
                    finish();


                    return json.getString(TAG_MESSAGE);



                }else if(success == 0) {
                    Log.d("Adding Friends Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);
                }else if(success == 2) {
                    Log.d("You guys are already friends!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);
                }else if(success == 3) {

                    Log.d("You guys are alike 555!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Add_Friend.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}
