package android.whitewidget.com.boilerplateandroid.ui.main.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.whitewidget.com.boilerplateandroid.R;
import android.whitewidget.com.boilerplateandroid.data.model.Model;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BasicGridRecyclerViewAdapter extends RecyclerView.Adapter<BasicGridRecyclerViewAdapter.BasicGridViewHolder>{
    private Context context;
    private ArrayList<Model> models;
    private static ClickListener clickListener;

    public BasicGridRecyclerViewAdapter(Context context, ArrayList<Model> models, ClickListener clickListener) {
        this.context = context;
        this.models = models;
        this.clickListener = clickListener;
    }

    @Override
    public BasicGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BasicGridViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_basic, parent, false));
    }

    @Override
    public void onBindViewHolder(BasicGridViewHolder holder, int position) {
        Model model = models.get(position);
        holder.tv_title.setText(model.getTitle());
        Glide.with(context).load(Uri.parse(model.getImage())).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class BasicGridViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public ImageView iv_image;

        public BasicGridViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(v, getLayoutPosition());
                }
            });
        }
    }
}
