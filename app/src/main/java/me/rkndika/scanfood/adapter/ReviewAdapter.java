package me.rkndika.scanfood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import me.rkndika.scanfood.R;
import me.rkndika.scanfood.model.Review;

/**
 * Created by include on 02/06/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> listReview;
    private Context context;

    public ReviewAdapter(Context applicationContext, List<Review> listReview){
        this.context = applicationContext;
        this.listReview = listReview;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_review, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.userName.setText(listReview.get(i).getUser().getName());
        viewHolder.reviewDate.setText(listReview.get(i).getCreatedAt());
        viewHolder.reviewContent.setText(listReview.get(i).getContent());
        viewHolder.waiterName.setText("Dilayani oleh : #" + listReview.get(i).getWaiterId());
        viewHolder.userRating.setRating(listReview.get(i).getRating());

        Glide.with(context)
                .load(listReview.get(i).getUser().getPhoto())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(viewHolder.userPhoto);
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userName, reviewDate, reviewContent, waiterName;
        private SimpleRatingBar userRating;
        private CircularImageView userPhoto;

        public ViewHolder(View view){
            super(view);
            userName = (TextView) view.findViewById(R.id.userName);
            reviewDate = (TextView) view.findViewById(R.id.reviewDate);
            reviewContent = (TextView) view.findViewById(R.id.reviewContent);
            waiterName = (TextView) view.findViewById(R.id.waiterName);
            userRating = (SimpleRatingBar) view.findViewById(R.id.userRating);
            userPhoto = (CircularImageView) view.findViewById(R.id.userPhoto);
        }
    }
}
