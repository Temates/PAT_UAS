package com.example.pat_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    TextView name,mail;
    Button logout,checkin, checkout;
    ListView mylv,mylv1;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    String val;
    String MyData[],otherData[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        final SwipeRefreshLayout pullToRefresh1 = findViewById(R.id.pullToRefresh1);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        logout = findViewById(R.id.logout);
        checkin = findViewById(R.id.checkin);
        checkout = findViewById(R.id.checkout);
        mylv = (ListView) findViewById(R.id.lv1);
        mylv1 = (ListView) findViewById(R.id.lv2);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(account != null){
                    String Name = account.getDisplayName();
                    String Mail = account.getEmail();
                    name.setText("Welcome, "+Name);
                    mail.setText(Mail);
                    HTTPReqTask httpReqTask = new HTTPReqTask();
                    httpReqTask.execute();
                }

                pullToRefresh.setRefreshing(false);
            }
        });
        pullToRefresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(account != null){
                    String Name = account.getDisplayName();
                    String Mail = account.getEmail();
                    name.setText("Welcome, "+Name);
                    mail.setText(Mail);
                    HTTPReqTask httpReqTask = new HTTPReqTask();
                    httpReqTask.execute();
                }

                pullToRefresh1.setRefreshing(false);
            }
        });

        if(account != null){
            String Name = account.getDisplayName();
            String Mail = account.getEmail();
            name.setText("Welcome, "+Name);
            mail.setText(Mail);
            HTTPReqTask httpReqTask = new HTTPReqTask();
            httpReqTask.execute();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpcheckin httpCheckin = new httpcheckin();
                httpCheckin.execute();

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpcheckout httpCheckout = new httpcheckout();
                httpCheckout.execute();
            }
        });
    }
    private class httpcheckin extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                PostCheckin();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class httpcheckout extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                PostCheckout();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HTTPReqTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String val = new String();
            try {

                    PostSignIn();
                    Reqtable();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void PostSignIn() throws IOException, JSONException {
        String url = "http://192.168.0.9:3000/login_android";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST"); //PUT / DELETE
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("Username", account.getDisplayName());
        jsonParam.put("Email", account.getEmail());

        System.out.println(jsonParam.toString());//
        byte[] jsData = jsonParam.toString().getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(jsData);
        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuffer response = new StringBuffer();
        while ((input=in.readLine())!=null){
            response.append(input);
        }
        in.close();
        val = response.toString();
        System.out.println("Response : \n" +response.toString());
//        JSONArray myArray = new JSONArray(response.toString());
//        for (int i=0; i < myArray.length();i++){
//            JSONObject arrObj = myArray.getJSONObject(i);
//            System.out.println("Data Title : ");
//            System.out.println(arrObj);
//            JOptionPane.showMessageDialog(null,arrObj);
//        }
        os.flush();
        os.close();
    }

    private void PostCheckin() throws IOException, JSONException {
        String url = "http://192.168.0.9:3000/postattend";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST"); //PUT / DELETE
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        //con.setDoOutput(true);
        //con.setDoInput(true);
        //con.connect();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("check", "checkin");
        jsonParam.put("Email", account.getEmail());
        System.out.println(jsonParam.toString());//
        byte[] jsData = jsonParam.toString().getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(jsData);
        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuffer response = new StringBuffer();
        while ((input=in.readLine())!=null){
            response.append(input);
        }
        in.close();

        System.out.println("Response : \n" +response.toString());
//        JSONArray myArray = new JSONArray(response.toString());
//        for (int i=0; i < myArray.length();i++){
//            JSONObject arrObj = myArray.getJSONObject(i);
//            System.out.println("Data Title : ");
//            System.out.println(arrObj);
//            JOptionPane.showMessageDialog(null,arrObj);
//        }
        os.flush();
        os.close();
        String toast_response = new String(response.toString());
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), toast_response, Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void PostCheckout() throws IOException, JSONException {
        String url = "http://192.168.0.9:3000/postattend";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST"); //PUT / DELETE
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        //con.setDoOutput(true);
        //con.setDoInput(true);
        //con.connect();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("check", "checkout");
        jsonParam.put("Email", account.getEmail());
        System.out.println(jsonParam.toString());//
        byte[] jsData = jsonParam.toString().getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(jsData);
        int responseCode = con.getResponseCode();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuffer response = new StringBuffer();
        while ((input=in.readLine())!=null){
            response.append(input);
        }
        in.close();

        System.out.println("Response : \n" +response.toString());
//        JSONArray myArray = new JSONArray(response.toString());
//        for (int i=0; i < myArray.length();i++){
//            JSONObject arrObj = myArray.getJSONObject(i);
//            System.out.println("Data Title : ");
//            System.out.println(arrObj);
//            JOptionPane.showMessageDialog(null,arrObj);
//        }
        os.flush();
        os.close();
        String toast_response = new String(response.toString());
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), toast_response, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void Reqtable() throws Exception {

        String url = "http://192.168.0.9:3000/list_user/"+val;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        con.connect();
        String responseMessage = con.getResponseMessage();
        System.out.println("Send Get Request to : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String input;
        StringBuilder response = new StringBuilder();
        while ((input=in.readLine())!=null){
            response.append(input);
        }
        in.close();
        Log.d("SQL", response.toString());
        JSONObject jObj = new JSONObject(response.toString());
        //JSONObject jAttend = jObj.getJSONObject("attend");
        JSONArray myArray = jObj.getJSONArray("checkin");

        //  JSONArray myArray = new JSONArray(response.toString());
        ArrayList<String> resultin = new ArrayList<>();
        if (myArray.length() == 0) {
            resultin.add("Check In:");
            resultin.add("\n");
        }
        else {
            resultin.add("Check In:");
            for (int i = 0; i < myArray.length(); i++) {
                JSONObject arrObj = myArray.getJSONObject(i);
                System.out.println("DATA : " + myArray);
                //String No_attend = arrObj.getString("No");
                String Date_in = arrObj.getString("Date");
                String Time_in = arrObj.getString("Time");
                //String Data = "No. Absen: "+No_attend+"\n"+"Tanggal: "+Date_attend+ "\n"+ "Jam: " + Time_attend;
                String Data = "Tanggal: " + Date_in + "\n" + "Jam: " + Time_in;
                resultin.add(Data);
//            result.add(arrObj.getString("Date_attend"));
//            result.add(arrObj.getString("Time_attend"));
//            result.add(arrObj.getString("No"));
//            result.add(arrObj.getString("Date_attend"));
//            result.add(arrObj.getString("Time_attend"));
                //result.add(arrObj.getString("title"));
            }

        }

        ArrayList<String> resultout = new ArrayList<>();
        JSONArray Arr = jObj.getJSONArray("checkout");
        if (Arr.length() == 0) {
            resultout.add("Check Out:");
            resultout.add("\n");
        }else{
            System.out.println("DATA : " + Arr);
            resultout.add("Check Out:");
            for (int i = 0; i < Arr.length(); i++) {
                JSONObject arrObje = Arr.getJSONObject(i);
                System.out.println("DATA : " + Arr);
                //String No_salary = arrObje.getString("No");
                String Date_out = arrObje.getString("Date");
                String Time_out = arrObje.getString("Time");
                //String Data = "No. Gaji: "+No_salary+"\n"+"Tanggal: "+Date_salary+ "\n"+ "Jam: " + Time_salary;
                String Data = "Tanggal: " + Date_out + "\n" + "Jam: " + Time_out;
                resultout.add(Data);
//            result.add(arrObj.getString("Date_attend"));
//            result.add(arrObj.getString("Time_attend"));
//            result.add(arrObj.getString("No"));
//            result.add(arrObj.getString("Date_attend"));
//            result.add(arrObj.getString("Time_attend"));
                //result.add(arrObj.getString("title"));

            }
        }

        //System.out.println(result_salary);
        MyData = resultin.toArray(new String[0]);
        otherData = resultout.toArray(new String[0]);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                CustomAdapter cAdapter =
                        new CustomAdapter(getApplicationContext(),MyData);
                CustomAdapter bAdapter =
                        new CustomAdapter(getApplicationContext(),otherData);
                mylv.setAdapter(cAdapter);
                mylv1.setAdapter(bAdapter);
            }
        });

//        mylv = (ListView) findViewById(R.id.lv1);
//        CustomAdapter cAdapter =
//                new CustomAdapter(getApplicationContext(),MyData);
//        mylv.setAdapter(cAdapter);

//        return MyData + otherData;
    }

    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }

}