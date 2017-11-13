package android.zeroh729.com.ecggrapher.ui.main.views.viewholders;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.model.Model;
import android.zeroh729.com.ecggrapher.ui.main.views.SquareLayout;
import android.widget.ImageView;
import android.widget.TextView;


import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.grid_basic)
public class BasicGridView extends SquareLayout {
    @ViewById(R.id.iv_image)
    ImageView iv_image;

    @ViewById(R.id.tv_title)
    TextView tv_title;

    public BasicGridView(Context context) {
        super(context);
    }

    public void bind(Model model, View.OnClickListener listener){
        tv_title.setText(model.getTitle());
        setOnClickListener(listener);
    }
}
