package OSS.geteatwithme.Connection;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KaKaofitService {
    private Retrofit retrofit;

    public KaKaofitService(){
        initializeKakao();
    }
    public void initializeKakao(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public Retrofit getKakaoFit(){
        return retrofit;
    }
}
