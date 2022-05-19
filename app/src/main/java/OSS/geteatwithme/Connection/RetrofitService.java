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
                .baseUrl("http://192.168.191.219:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
