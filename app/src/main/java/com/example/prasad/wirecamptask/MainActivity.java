package com.example.prasad.wirecamptask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    Context mContext;
    RecyclerAdapter mRecyclerAdapter;
    RequestQueue requestQueue;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mContext = this;
        requestQueue = Volley.newRequestQueue(mContext);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("WireCampTask");
        }

        if (isNetworkConnected())
        {
            String url = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%" +
                    "7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&" +
                    "wbptterms=description&gpssearch=Sachin+T&gpslimit=10";

            GetAsyncTask getAsyncTask = new GetAsyncTask(MainActivity.this,
                    url,
                    new GetAsyncTask.getResponse() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void response(String s, StringBuilder response) {
                            try {
                                System.out.println("errror"+s +String.valueOf(response));
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                JSONObject jsonObject1 = jsonObject.getJSONObject("query");
                                JSONArray jsonArray = jsonObject1.getJSONArray("pages");

                                mRecyclerAdapter.setRecyclerAdapter(parsedata(jsonArray));
                            }
                            catch (JSONException E)
                            {
                                E.printStackTrace();
                            }
                        }
                    });
            getAsyncTask.execute();
        }

        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerAdapter = new RecyclerAdapter(mContext, MainActivity.this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<DataModel> parsedata(JSONArray jsonarray) throws JSONException {
        ArrayList<DataModel> arraylist = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            DataModel mDataModel = new DataModel();
            JSONObject jsonObject = jsonarray.getJSONObject(i);

                mDataModel.setPageId(jsonObject.getString("pageid"));
                mDataModel.setTitle(jsonObject.getString("title"));
                JSONArray jsonArray1 = jsonObject.getJSONObject("terms").getJSONArray("description");

                for (int q = 0; q < jsonArray1.length(); q++)
                {
                    mDataModel.setDescriptions(jsonArray1.getString(q));
                }
                if (jsonObject.has("thumbnail"))
                {
                    System.out.println("its true");
                    mDataModel.setThumbnail(jsonObject.getJSONObject("thumbnail").getString("source"));
                }
//
                arraylist.add(mDataModel);


        }
        return arraylist;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
