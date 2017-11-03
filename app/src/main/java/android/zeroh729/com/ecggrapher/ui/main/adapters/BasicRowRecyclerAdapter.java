package android.zeroh729.com.ecggrapher.ui.main.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.zeroh729.com.ecggrapher.data.model.Model;
import android.zeroh729.com.ecggrapher.ui.base.BaseAdapterRecyclerView;
import android.zeroh729.com.ecggrapher.ui.base.ViewWrapper;
import android.zeroh729.com.ecggrapher.ui.main.views.viewholders.BasicRowView;
import android.zeroh729.com.ecggrapher.ui.main.views.viewholders.BasicRowView_;

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
