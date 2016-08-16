package android.whitewidget.com.boilerplateandroid.views.main.activities;

import android.util.Log;
import android.whitewidget.com.boilerplateandroid.R;
import android.whitewidget.com.boilerplateandroid.data.events.NetworkEvent;
import android.whitewidget.com.boilerplateandroid.data.model.Article;
import android.whitewidget.com.boilerplateandroid.data.remote.BaseService;
import android.whitewidget.com.boilerplateandroid.views.base.BaseActivity;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Bean
    BaseService baseService;

    @AfterViews
    public void afterViews(){
        baseService.getArticle();
    }

    @Subscribe
    public void subscribeToNetworkEvent(NetworkEvent event){
        Toast.makeText(this, "Connected : " + event.isConnected(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void subscribeTagList(Article article){
        Log.d("TEST", "Article size: " + article.getCount());
    }
}
