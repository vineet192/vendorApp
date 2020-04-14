package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.MyHistoryItems;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductDescriptionResponse> myItemList;
    private Context context;
    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public OngoingAdapter(Context context, List myItemList) {
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String path = myItemList.get(position).getProd_img();

        Picasso.get()
                .load(path)
                .resize(100, 100)
                .centerCrop()
                .into(holder.itemImage);

        holder.itemName.setText(myItemList.get(position).getProd_name());
        holder.itemDate.setText("30-09-2019 02:00 pm");
        holder.itemPrice.setText(myItemList.get(position).getProd_price().toString());
        holder.dispatch.setVisibility(View.GONE);

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
        TextView dispatch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            dispatch = itemView.findViewById(R.id.status);
        }
    }
}
