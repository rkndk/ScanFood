package id.ac.unsyiah.scanfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.unsyiah.scanfood.DetailPartnerActivity;
import id.ac.unsyiah.scanfood.OrderMenuActivity;
import id.ac.unsyiah.scanfood.R;
import id.ac.unsyiah.scanfood.model.MenuMakanan;
import id.ac.unsyiah.scanfood.model.News;

/**
 * Created by include on 02/06/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<MenuMakanan> lisMenu;
    private Context context;

    public MenuAdapter(Context applicationContext, List<MenuMakanan> lisMenu){
        this.context = applicationContext;
        this.lisMenu = lisMenu;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_menu, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder viewHolder, int i) {
        viewHolder.menuName.setText(lisMenu.get(i).getName());
        viewHolder.menuPrice.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(lisMenu.get(i).getPrice()));

        //jika terdapat promo
        if(lisMenu.get(i).getPromoPrice() != null){
            //membuat tulisan dicoret
            viewHolder.menuPrice.setPaintFlags(viewHolder.menuPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            //ubah warna merah
            viewHolder.menuPrice.setTextColor(Color.RED);

            viewHolder.menuPromoPrice.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(lisMenu.get(i).getPromoPrice()));
            viewHolder.menuPromoPrice.setVisibility(View.VISIBLE);

            viewHolder.menuPromoTime.setText(lisMenu.get(i).getPromoTime());
            viewHolder.menuPromoTime.setVisibility(View.VISIBLE);

            viewHolder.menuPromoDesc.setText(lisMenu.get(i).getPromoDesc());
            viewHolder.infoContainer.setVisibility(View.VISIBLE);
        }

        Glide.with(context)
                .load(lisMenu.get(i).getPhoto1())
                .placeholder(R.drawable.rec_gray)
                .error(R.drawable.rec_gray)
                .dontAnimate()
                .into(viewHolder.menuPhoto);
    }

    @Override
    public int getItemCount() {
        return lisMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView menuName, menuPrice, menuPromoPrice, menuPromoTime, menuPromoDesc;
        private CircularImageView menuPhoto;
        private LinearLayout infoContainer;

        public ViewHolder(View view){
            super(view);
            menuName = (TextView) view.findViewById(R.id.menuName);
            menuPrice = (TextView) view.findViewById(R.id.menuPrice);
            menuPromoPrice = (TextView) view.findViewById(R.id.menuPromoPrice);
            menuPromoTime = (TextView) view.findViewById(R.id.menuPromoTime);
            menuPromoDesc = (TextView) view.findViewById(R.id.menuPromoDesc);
            menuPhoto = (CircularImageView) view.findViewById(R.id.menuPhoto);
            infoContainer = (LinearLayout) view.findViewById(R.id.infoContainer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        MenuMakanan clickedMenu = lisMenu.get(pos);

                        Gson gson = new Gson();
                        String jsonMenu = gson.toJson(clickedMenu);

                        Intent i = new Intent(context, OrderMenuActivity.class);
                        i.putExtra("jsonMenu", jsonMenu);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}
