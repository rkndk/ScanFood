package me.rkndika.scanfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import me.rkndika.scanfood.DetailPartnerActivity;
import me.rkndika.scanfood.R;
import me.rkndika.scanfood.model.News;

/**
 * Created by include on 02/06/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> news;
    private Context context;

    public NewsAdapter(Context applicationContext, List<News> newsArrayList){
        this.context = applicationContext;
        this.news = newsArrayList;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.partnerName.setText(news.get(i).getPartner().getName());
        viewHolder.newsDate.setText(news.get(i).getCreatedAt());
        viewHolder.newsContent.setText(news.get(i).getContent());
        viewHolder.partnerRating.setRating(news.get(i).getPartner().getRating());

        Glide.with(context)
                .load(news.get(i).getPartner().getPhoto())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(viewHolder.partnerPhoto);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView partnerName, newsDate, newsContent;
        private SimpleRatingBar partnerRating;
        private CircularImageView partnerPhoto;

        public ViewHolder(View view){
            super(view);
            partnerName = (TextView) view.findViewById(R.id.partnerName);
            newsDate = (TextView) view.findViewById(R.id.newsDate);
            newsContent = (TextView) view.findViewById(R.id.newsContent);
            partnerRating = (SimpleRatingBar) view.findViewById(R.id.partnerRating);
            partnerPhoto = (CircularImageView) view.findViewById(R.id.partnerPhoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        News clickedNews = news.get(pos);
                        Intent i = new Intent(context, DetailPartnerActivity.class);
                        i.putExtra("id", clickedNews.getPartner().getId());
                        i.putExtra("name", clickedNews.getPartner().getName());
                        i.putExtra("photo", clickedNews.getPartner().getPhoto());
                        i.putExtra("address", clickedNews.getPartner().getAddress());
                        i.putExtra("cover", clickedNews.getPartner().getCover());
                        i.putExtra("desc", clickedNews.getPartner().getDesc());
                        i.putExtra("email", clickedNews.getPartner().getEmail());
                        i.putExtra("phone", clickedNews.getPartner().getPhone());
                        i.putExtra("rating", clickedNews.getPartner().getRating());
                        i.putExtra("open_time", clickedNews.getPartner().getOpenTime());
                        i.putExtra("close_time", clickedNews.getPartner().getCloseTime());
                        i.putExtra("latitude", clickedNews.getPartner().getLatitude());
                        i.putExtra("longitude", clickedNews.getPartner().getLongitude());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        //Toast.makeText(v.getContext(), "You clicked " + clickedNews.getPartner().getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
