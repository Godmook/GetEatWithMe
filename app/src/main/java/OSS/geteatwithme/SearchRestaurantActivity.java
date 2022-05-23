package OSS.geteatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.BuildConfig;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import OSS.geteatwithme.Connection.KaKaofitService;
import OSS.geteatwithme.Connection.ResultSearchKeyword;
import OSS.geteatwithme.Connection.UserProfileAPI;
import OSS.geteatwithme.MapInfo.MapData;
import OSS.geteatwithme.PostInfo.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRestaurantActivity extends AppCompatActivity {
    private class GetMarker extends AsyncTask<Call,Void, ResultSearchKeyword> {
        @Override
        protected ResultSearchKeyword doInBackground(Call... calls) {
            try{
                Call<ResultSearchKeyword> call=calls[0];
                Response<ResultSearchKeyword> response=call.execute();
                return response.body();
            }catch(IOException e){

            }
            return null;
        }
    }
    EditText search_word;
    Button search_start, search_cancel, search_end;
    String tmp_String;
    MapView mapView;
    ResultSearchKeyword resultSearchKeyword=new ResultSearchKeyword();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);
        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_find);
        mapViewContainer.addView(mapView);
        Post tmp_post = new Post();
        search_word=(EditText) findViewById(R.id.search_restaurant_text);
        search_start=(Button)findViewById(R.id.btn_search_start);
        search_cancel=(Button)findViewById(R.id.btn_search_cancel);
        search_end=(Button)findViewById(R.id.btn_search_end);
        ArrayList<MapData> mapData=new ArrayList<>();
        search_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Query = search_word.getText().toString();
                if (Query.matches(""))
                    Toast.makeText(SearchRestaurantActivity.this, "장소를 입력하세요!", Toast.LENGTH_SHORT).show();
                else {
                    mapView.removeAllPOIItems();
                    mapData.clear();
                    KaKaofitService kaKaofitService = new KaKaofitService();
                    UserProfileAPI userProfileAPI = kaKaofitService.getKakaoFit().create(UserProfileAPI.class);
                    SharedPreferences auto = getSharedPreferences("LoginSource", Activity.MODE_PRIVATE);
                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(auto.getString("latitude", null)), Double.parseDouble(auto.getString("longitude", null)));
                    mapView.setMapCenterPoint(mapPoint, true);
                    Call<ResultSearchKeyword> call = userProfileAPI.GetSearchKeyword("KakaoAK f9e3926b054ba1f5d08a2672f49e8869", Query, auto.getString("longitude", null), auto.getString("latitude", null), "10000");
                    try {
                        resultSearchKeyword = new GetMarker().execute(call).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < resultSearchKeyword.documentList.size(); i++) {
                        MapData ds = new MapData();
                        ds.setName(resultSearchKeyword.documentList.get(i).getPlace_name());
                        ds.setAddress_name(resultSearchKeyword.documentList.get(i).getRoad_address_name());
                        ds.setX(resultSearchKeyword.documentList.get(i).getX());
                        ds.setY(resultSearchKeyword.documentList.get(i).getY());
                        ds.setRestaurant_id(Integer.parseInt(resultSearchKeyword.documentList.get(i).getId()));
                        mapData.add(ds);
                    }
                    ArrayList<MapPOIItem> markerarr = new ArrayList<>();
                    for (MapData m : mapData) {
                        MapPOIItem mymarker = new MapPOIItem();
                        mymarker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(m.getY()), Double.parseDouble(m.getX())));
                        mymarker.setItemName(m.getName());
                        markerarr.add(mymarker);
                    }
                    mapView.addPOIItems(markerarr.toArray(new MapPOIItem[markerarr.size()]));
                }
            }
        });
        ScrollView scrollView = findViewById(R.id.map_scroll);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                }
                mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
                    @Override
                    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                        String res=mapPOIItem.getItemName();
                        TextView text=(TextView)findViewById(R.id.searching_place_name);
                        text.setText(mapPOIItem.getItemName());
                        tmp_post.setRestaurant(mapPOIItem.getItemName());
                        MapPoint.GeoCoordinate tmps=mapPOIItem.getMapPoint().getMapPointGeoCoord();
                        tmp_post.setLatitude(tmps.latitude);
                        tmp_post.setLongitude(tmps.longitude);
                        for(MapData m:mapData){
                            if(m.getName().matches(mapPOIItem.getItemName())) {
                                tmp_post.setRestaurant_id(m.getRestaurant_id());
                                tmp_String=m.getAddress_name();
                            }
                        }
                    }

                    @Override
                    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

                    }

                    @Override
                    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

                    }

                    @Override
                    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

                    }
                });
                return false;
            }
        });
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchRestaurantActivity.this, PostingActivity.class);
                myIntent.putExtra("place", tmp_post.getRestaurant());
                myIntent.putExtra("placeX", tmp_post.getLongitude());
                myIntent.putExtra("placeY", tmp_post.getLatitude());
                myIntent.putExtra("place_address", tmp_String);
                myIntent.putExtra("place_id",tmp_post.getRestaurant_id());
                setResult(Activity.RESULT_OK, myIntent);
                finish();
            }
        });
    }
}