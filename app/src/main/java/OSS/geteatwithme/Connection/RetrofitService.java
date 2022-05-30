package OSS.geteatwithme.Connection;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(){
        initializeRefrofit();
    }
    public void initializeRefrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://15.164.147.90:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
