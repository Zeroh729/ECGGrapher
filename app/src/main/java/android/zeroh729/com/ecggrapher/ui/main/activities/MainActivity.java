package android.zeroh729.com.ecggrapher.ui.main.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.events.NetworkEvent;
import android.zeroh729.com.ecggrapher.data.model.Article;
import android.zeroh729.com.ecggrapher.data.model.Model;
import android.zeroh729.com.ecggrapher.data.model.Tag;
import android.zeroh729.com.ecggrapher.data.remote.BaseService;
import android.zeroh729.com.ecggrapher.presenters.DataListPresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseActivity;
import android.zeroh729.com.ecggrapher.ui.base.BaseAdapterRecyclerView;
import android.zeroh729.com.ecggrapher.ui.main.adapters.BasicGridRecyclerAdapter;
import android.zeroh729.com.ecggrapher.utils._;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements DataListPresenter.Screen{

    @Bean
    BaseService service;

    @ViewById(R.id.rv_data)
    RecyclerView rv_data;

    @Bean
    BasicGridRecyclerAdapter adapter;

    @AfterViews
    public void afterViews(){
        service.getArticle();

        rv_data.setLayoutManager(new GridLayoutManager(this, 2));
        rv_data.setAdapter(adapter);
        adapter.setListener(new BaseAdapterRecyclerView.ClickListener() {
            @Override
            public void onClick(int position) {
                _.showToast("Clicked " + position);
            }
        });
    }

    @Subscribe
    public void subscribeToNetworkEvent(NetworkEvent event){
        Toast.makeText(this, "Connected : " + event.isConnected(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void subscribeToTagList(Article article){
        for(Tag tag : article.getTags()) {
            Model model = new Model();
            model.setTitle(tag.getTitle());
            model.setSubtitle(tag.getSlug());
            model.setImage(imageurls[adapter.getItems().size()]);
            adapter.getItems().add(model);
        }
        update();
    }

    @UiThread
    public void update(){
        adapter.notifyDataSetChanged();
    }

    String[] imageurls = {
        "https://upload.wikimedia.org/wikipedia/commons/d/d4/Kim_Jong-Un_Photorealistic-Sketch.jpg",
        "http://www.gannett-cdn.com/-mm-/5404630eef7d376ec92188c28cfc031e2a4026a4/c=225-0-3775-2669&r=x408&c=540x405/local/-/media/2016/05/10/USATODAY/USATODAY/635984715851776795-AFP-551724097.jpg",
        "http://a3.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTIwNjA4NjM0MjAzODMzODY4.jpg",
        "http://i.telegraph.co.uk/multimedia/archive/03523/Kim_Jong_Un_3523321k.jpg",
        "http://cdn.newsapi.com.au/image/v1/ee8ed0d666012b8e518608e65c88b5fc",
        "http://cdnph.upi.com/sv/b/i/UPI-4061449758722/2015/1/14497588279875/Kim-Jong-Un-North-Korea-has-hydrogen-bomb.jpg",
        "http://i2.cdn.turner.com/cnnnext/dam/assets/140116003943-kim-jong-un-north-korea-profile-dictator-horizontal-large-gallery.jpg",
        "http://static3.businessinsider.com/image/534d72dbeab8ead93be334e3-480/north-korea-kim-jong-un.jpg",
        "https://i.ytimg.com/vi/50RZrJSC0Cs/maxresdefault.jpg",
        "http://i.onionstatic.com/onion/1887/6/16x9/600.jpg",
        "http://s1.ibtimes.com/sites/www.ibtimes.com/files/2016/05/07/kim-jong-un.jpg",
        "http://i2.mirror.co.uk/incoming/article7993673.ece/ALTERNATES/s615b/Kim-Jong-Un.jpg"
    };


}
