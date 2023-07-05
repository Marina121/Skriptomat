package ba.sum.fpmoz.skriptomat.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import ba.sum.fpmoz.skriptomat.DetailsNewsActivity;
import ba.sum.fpmoz.skriptomat.R;
import ba.sum.fpmoz.skriptomat.model.NewsItem;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private Context context;
    private List<NewsItem> newsList;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {

        NewsItem newsItem = newsList.get(position);
        holder.bind(newsItem);

        holder.newsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsNewsActivity.class);

                intent.putExtra("title" , newsItem.getTitle());
                intent.putExtra("alias", newsItem.getAlias());
                intent.putExtra("image",newsItem.getImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsAlias;
        ImageView imgUrl;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
           // newsAlias = itemView.findViewById(R.id.newsAlias);
            imgUrl = itemView.findViewById(R.id.imgUrl);
        }
        public void bind(NewsItem newsItem) {
            newsTitle.setText(newsItem.getTitle());
          //  newsAlias.setText(newsItem.getAlias());

            Picasso.get()
                    .load(newsItem.getImage())
                    .error(R.drawable.logo_sum)
                    .into(imgUrl);
        }
    }

}

