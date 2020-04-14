package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.MyHistoryItems;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductDescriptionResponse> myItemList;

    public HistoryAdapter(Context context, List myItemList) {
        this.mInflater = LayoutInflater.from(context);
        this.myItemList=myItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_history_in_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String path = "https://lit-earth-71252.herokuapp.com/" + myItemList.get(position).getProd_img();

        Picasso.get()
                .load(path)
                .resize(100, 100)
                .centerCrop()
                .into(holder.itemImage);

        holder.itemName.setText(myItemList.get(position).getProd_name());
        holder.itemDate.setText("23-04-2019 09:00");
        holder.itemPrice.setText(Integer.toString(myItemList.get(position).getProd_price()));
        holder.itemStatus.setVisibility(View.INVISIBLE);
//        holder.itemStatus.setText(String.valueOf(myItemList.get(position).isCheck()));

        holder.getAdapterPosition();

    }

    @Override
    public int getItemCount() {
            return myItemList.size();
    }

    public void filteredlists(ArrayList<ProductDescriptionResponse> filteredItems) {
        myItemList=filteredItems;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemName;
        TextView itemDate;
        TextView itemPrice;
        TextView itemStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemStatus = itemView.findViewById(R.id.status);
        }
    }
}
