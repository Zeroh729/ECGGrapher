package android.whitewidget.com.boilerplateandroid.data.remote;

import android.whitewidget.com.boilerplateandroid.data.model.APIError;
import android.whitewidget.com.boilerplateandroid.data.model.Article;
import android.whitewidget.com.boilerplateandroid.utils.ErrorUtils;
import android.whitewidget.com.boilerplateandroid.utils.OttoBus;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

@EBean
public class BaseService {
    private static final String API_ENDPOINT = "http://readytoparent.com.ph";

    @Bean
    OttoBus bus;

    public interface BaseTask{
        @GET("/articles/?json=get_tag_index")
        Call<Article> listRepos();

        //Add more api links here
    }

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create());

    private static BaseTask newService() {
        return builder.build().create(BaseTask.class);
    }

    public static Retrofit retrofit(){
        return builder.build();
    }

    @Background
    public void getArticle(){
        try {
            Call<Article> call = newService().listRepos();
            Response<Article> response = call.execute();
            if(response.isSuccessful()) {
                Article article = response.body();
                bus.post(article);
            }else{
                APIError error = ErrorUtils.parseError(response);
                bus.post(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
