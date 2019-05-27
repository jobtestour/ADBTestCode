package exemplefortest.adb_test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener, OnMapReadyCallback {

    ListView SubjectListView;
    LinearLayout Infoscreen;
    TextView InfoTxt;
    Button Back;
    SwipeRefreshLayout pullToRefresh;
    ProgressBar progressBarSubject;
    String ServerURL = "https://jsonplaceholder.typicode.com/users/";
    String SaveName="CacheJson";
    ArrayList<String> UserInfoArrayList = new ArrayList<String>();
    String GlobalString,UserName,Email,Street,Suite,City,ZipCode,Phone,Website,CompagnyName,catchPhrase,Bs,StringLat,StringLng;

    GoogleMap mGoogleMap;
    float Lat,Lng;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }





    @Override
    protected void onStart() {
        super.onStart();
        CreateScene();
        CreateMap();
        new JsonTask().execute(ServerURL);
    }


/**Create scene : Manage all scene elements : PullToRefresh, List view, Button etc **/

    private void CreateScene()
    {

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        SubjectListView = (ListView)findViewById(R.id.listview1);

        progressBarSubject = (ProgressBar)findViewById(R.id.progressBar);

        Infoscreen=(LinearLayout)findViewById(R.id.InfoLayout);

        InfoTxt=(TextView)findViewById(R.id.InfoText);


        Back=(Button)findViewById(R.id.Back);

        Back.setOnClickListener(MainActivity.this);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                new JsonTask().execute(ServerURL);


            }

        });


        SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AssignData(position);
                DisplayInfo();

                if(pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }

            }
        });

    }




    /**CreateMap : Init Google Map**/
    private void CreateMap()
    {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MainActivity.this);
    }


    /**Jsontask : Read Online Json peut it into a textFile and Save it locally to use it later
     * NB: For this exemple i didn't manage possible arrors (exept try cactch)**/

    private class JsonTask extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();

            if(!pullToRefresh.isRefreshing()) {
                progressBarSubject.setVisibility(View.VISIBLE);
            }

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarSubject.setVisibility(View.GONE);

            SubjectListView.setVisibility(View.VISIBLE);

           if(result != null)
           {

               Refraichlistview(result);

         }
            pullToRefresh.setRefreshing(false);
        }
    }


    /**Refraichlistview : simple code to give to the list view every name to display it**/
    private void Refraichlistview(String JsonResult)
    {


        Parsedata(JsonResult);
        //save file in a cache

        CacheJson.writeToFile(getApplicationContext(),SaveName,JsonResult);


      ListAdapterClass adapter = new ListAdapterClass(MainActivity.this,UserInfoArrayList);

      SubjectListView.setAdapter(adapter);



    }


    /**Parsedata : get every names and put them in an array (this array will be use with the list view**/
    private void Parsedata(String JsonData)
    {


        try {

            JSONArray   jsonObj = new JSONArray(JsonData);
            for (int i=0; i<jsonObj.length(); i++)
            {

                JSONObject Item= jsonObj.getJSONObject(i);
                UserInfoArrayList.add(Item.getString("name"));


            }



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }



    /**AssignData : get every Data we need to display suer info these data are read from the previously saved cache data
     * For this exemple i didn't manage possible arrors (exept try cactch)**/
    private void AssignData(int Id)
    {

        try {

        String JsonFile=ReadJson.JsonReader(getApplicationContext(),SaveName);

        JSONArray   jsonObj = new JSONArray(JsonFile);

            JSONObject GloabaItem= jsonObj.getJSONObject(Id);

            UserName =GloabaItem.getString("username");
            Email=GloabaItem.getString("email");
            Phone=GloabaItem.getString("phone");
            Website=GloabaItem.getString("website");

            JSONObject Adresse= GloabaItem.getJSONObject("address");

            Street=Adresse.getString("street");
            Suite=Adresse.getString("suite");
            City=Adresse.getString("city");
            ZipCode=Adresse.getString("zipcode");

            JSONObject Geo= Adresse.getJSONObject("geo");
            StringLat=Geo.getString("lat")+"f";
            Lat=Float.parseFloat(StringLat);
            StringLng=Geo.getString("lng")+"f";
            Lng=Float.parseFloat(StringLng);


            JSONObject Company= GloabaItem.getJSONObject("company");
            CompagnyName=Company.getString("name");
            catchPhrase=Company.getString("catchPhrase");
            Bs=Company.getString("bs");


            GlobalString=UserInfoArrayList.get(Id)+"\nUser Name : "+UserName+"\n Email : "+Email+"\n Adresse : \n Street : "+Street+"\n Suite : "+Suite+"\n City : "+City+
                    "\n ZipCode : "+ZipCode+"\n Phone :  "+Phone+"\n WebSite : "+Website+"\n Compagny : \n Compagny Name : "+CompagnyName+"\n catchPhrase : "+catchPhrase+"\n Bs : "+Bs;




        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }

    /**DisplayInfo & BackToList: UI Navigation between 2 screens**/
    private void DisplayInfo()
    {


        pullToRefresh.setVisibility(View.GONE);
        InfoTxt.setText(GlobalString);
        Infoscreen.setVisibility(View.VISIBLE);
        RefraichLocation();



    }


    private void BackToList()
    {

        pullToRefresh.setVisibility(View.VISIBLE);
        Infoscreen.setVisibility(View.GONE);


    }


    /**manage button back**/

    @Override
    public void onClick(final View v) {


        switch (v.getId()) {



            case(R.id.Back):
                BackToList();
                break;
        }
    }



    /**onMapReady Init map, i catch google map here to get control after**/

    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


    /**RefraichLocation : here i ... refresh user's location on map**/
    void RefraichLocation()
    {

        mGoogleMap.clear();
        LatLng latLng = new LatLng(Lat,Lng);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                .title(UserName));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }







}