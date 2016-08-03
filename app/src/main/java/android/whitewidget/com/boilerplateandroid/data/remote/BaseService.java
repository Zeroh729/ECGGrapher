package android.whitewidget.com.boilerplateandroid.data.remote;

import android.whitewidget.com.boilerplateandroid.data.model.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Admin on 3/8/16.
 */
public interface BaseService {
    String API_ENDPOINT = "http://readytoparent.com.ph";

    @GET("/articles/?json=get_tag_index")
    Call<List<Model>> listRepos();

    class Creator {
        public static BaseService newService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(BaseService.class);
        }
    }
}
