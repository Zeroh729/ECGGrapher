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

public class BasicRowRecyclerViewAdapter extends RecyclerView.Adapter<BasicRowRecyclerViewAdapter.BasicRowViewHolder>{
    private Context context;
    private ArrayList<Model> models;
    private static ClickListener clickListener;

    public BasicRowRecyclerViewAdapter(Context context, ArrayList<Model> models, ClickListener clickListener) {
        this.context = context;
        this.models = models;
        this.clickListener = clickListener;
    }

    @Override
    public BasicRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BasicRowViewHolder(LayoutInflater.from(context).inflate(R.layout.row_basic, parent, false));
    }

    @Override
    public void onBindViewHolder(BasicRowViewHolder holder, int position) {
        Model model = models.get(position);
        holder.tv_title.setText(model.getTitle());
        holder.tv_subtitle.setText(model.getSubtitle());
        Glide.with(context).load(Uri.parse(model.getImage())).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class BasicRowViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_subtitle;

        public BasicRowViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
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
