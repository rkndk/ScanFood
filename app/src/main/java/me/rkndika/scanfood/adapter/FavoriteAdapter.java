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
import me.rkndika.scanfood.model.Favorite;

/**
 * Created by include on 02/06/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Favorite> favorites;
    private Context context;

    public FavoriteAdapter(Context applicationContext, List<Favorite> favorites){
        this.context = applicationContext;
        this.favorites = favorites;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_favorite, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder viewHolder, int i) {
        viewHolder.partnerName.setText(favorites.get(i).getPartner().getName());
        viewHolder.partnerTime.setText("Buka : "+favorites.get(i).getPartner().getOpenTime()+" - "+favorites.get(i).getPartner().getCloseTime());
        viewHolder.partnerRating.setRating(favorites.get(i).getPartner().getRating());

        Glide.with(context)
                .load(favorites.get(i).getPartner().getPhoto())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(viewHolder.partnerPhoto);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView partnerName, partnerTime;
        private SimpleRatingBar partnerRating;
        private CircularImageView partnerPhoto;

        public ViewHolder(View view){
            super(view);
            partnerName = (TextView) view.findViewById(R.id.partnerName);
            partnerTime = (TextView) view.findViewById(R.id.partnerTime);
            partnerRating = (SimpleRatingBar) view.findViewById(R.id.partnerRating);
            partnerPhoto = (CircularImageView) view.findViewById(R.id.partnerPhoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Favorite clickedFavorite = favorites.get(pos);
                        Intent i = new Intent(context, DetailPartnerActivity.class);
                        i.putExtra("id", clickedFavorite.getPartner().getId());
                        i.putExtra("name", clickedFavorite.getPartner().getName());
                        i.putExtra("photo", clickedFavorite.getPartner().getPhoto());
                        i.putExtra("address", clickedFavorite.getPartner().getAddress());
                        i.putExtra("cover", clickedFavorite.getPartner().getCover());
                        i.putExtra("desc", clickedFavorite.getPartner().getDesc());
                        i.putExtra("email", clickedFavorite.getPartner().getEmail());
                        i.putExtra("phone", clickedFavorite.getPartner().getPhone());
                        i.putExtra("rating", clickedFavorite.getPartner().getRating());
                        i.putExtra("open_time", clickedFavorite.getPartner().getOpenTime());
                        i.putExtra("close_time", clickedFavorite.getPartner().getCloseTime());
                        i.putExtra("latitude", clickedFavorite.getPartner().getLatitude());
                        i.putExtra("longitude", clickedFavorite.getPartner().getLongitude());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        //Toast.makeText(v.getContext(), "You clicked " + clickedNews.getPartner().getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
