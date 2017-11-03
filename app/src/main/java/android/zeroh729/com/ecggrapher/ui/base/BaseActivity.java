package android.zeroh729.com.ecggrapher.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.utils.OttoBus;
import android.zeroh729.com.ecggrapher.utils.OttoBus_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity
public abstract class BaseActivity extends AppCompatActivity{

    @Bean
    OttoBus bus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = OttoBus_.getInstance_(this);
        bus.register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(findViewById(R.id.toolbar) != null) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            getSupportActionBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

}
