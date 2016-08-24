package android.whitewidget.com.boilerplateandroid.ui.main.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.whitewidget.com.boilerplateandroid.data.model.Model;
import android.whitewidget.com.boilerplateandroid.ui.base.BaseAdapterRecyclerView;
import android.whitewidget.com.boilerplateandroid.ui.base.ViewWrapper;
import android.whitewidget.com.boilerplateandroid.ui.main.views.viewholders.BasicRowView;
import android.whitewidget.com.boilerplateandroid.ui.main.views.viewholders.BasicRowView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class BasicRowRecyclerAdapter extends BaseAdapterRecyclerView<Model, BasicRowView>{
    @RootContext
    Context context;

    @Override
    protected BasicRowView onCreateItemView(ViewGroup parent, int viewType) {
        return BasicRowView_.build(context);
    }

    @Override
    public void onBindViewHolder(final ViewWrapper<BasicRowView> holder, int position) {
        holder.getView().bind(items.get(position), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(holder.getAdapterPosition());
            }
        });
    }
}
