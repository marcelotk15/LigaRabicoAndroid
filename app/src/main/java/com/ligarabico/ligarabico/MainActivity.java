package com.ligarabico.ligarabico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    //TAG FOR LOGS
    private static final String LOG = "VOLLEY-SAMPLE";
    private static final Object TAG = new Object();

    private RequestQueue requestQueue;
    private ListView listView;
    private ArrayList<HashMap<String, String>> playersList;
    private String nextUrl;

    //API URL
    static String JsonURL;

    private void startLoadingAnim()
    {
        Log.i(LOG, "===== start loading");
        RotateLoading rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        rotateLoading.start();
    }

    private void stopLoadingAdnim()
    {
        Log.i(LOG, "===== stop loading");
        RotateLoading rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        rotateLoading.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playersList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.player_list);
        listView.setClickable(true);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        startLoadingAnim();

        JsonURL = "http://ligarabico.com/api/players";
        refreshDatas(JsonURL);

        listView.setOnScrollListener(new InfiniteScrollListener(1) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                refreshDatas(nextUrl);
                //listView.notifyDataSetChanged();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HashMap<String, String> item = (HashMap<String, String>) listView.getItemAtPosition(position);
//                Log.d("TAG", item.get("playerId"));
//
//                Intent intent = new Intent(getBaseContext(), PlayerActivity.class);
//                Bundle extra = new Bundle();
//                extra.putString("PLAYER_ID", item.get("playerId"));
//                intent.putExtras(extra);
//                startActivity(intent);
//
//                onPause();
//            }
//        });

    }

    private void refreshDatas(String JsonURL)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
            JsonURL,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        parseJson(response);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            },

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("TAG", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        );
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void parseJson(JSONObject jsonArray)
    {
        try {
            nextUrl = jsonArray.getString("next_page_url");

            JSONArray players = jsonArray.getJSONArray("data");
            for (int i = 0; i < players.length(); i++) {
                JSONObject player = players.getJSONObject(i);

                String clubImage = "http://ligarabico.com/clubImages/130000.png";
                if (!player.isNull("teamAPI")){
                    JSONObject team = player.getJSONObject("teamAPI");
                    clubImage = "http://ligarabico.com/clubImages/" + team.getString("eaId") + ".png";
                }

                HashMap<String, String> playerL = new HashMap<>();
                playerL.put("avatar", "http://ligarabico.com/futHeadImages/"+ player.getString("baseId") +".png");
                playerL.put("name", player.getString("name"));
                playerL.put("overall", player.getString("rating"));
                playerL.put("position", player.getString("position"));
                playerL.put("realClubName", player.getString("realClubName"));
                playerL.put("clubImage", clubImage);
                playerL.put("playerId", player.getString("id"));

                playersList.add(playerL);
            }
        } catch (final JSONException e) {
            stopLoadingAdnim();
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                MainActivity.this,
                playersList,
                R.layout.players_list,
                new String[]{"avatar", "name", "overall", "position", "realClubName", "clubImage", "playerId"},
                new int[]{R.id.avatar, R.id.name, R.id.rating, R.id.position, R.id.club, R.id.clubImage});

        adapter.setViewBinder( new CustomViewBinder());
        listView.setAdapter(adapter);
        stopLoadingAdnim();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }
}