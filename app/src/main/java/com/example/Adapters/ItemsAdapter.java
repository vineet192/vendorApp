package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter  extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ProductDescriptionResponse> myItemList;
    public boolean state;
    public OnStateChangeListner onStateChangeListner;
    private Context context;

    public interface OnStateChangeListner{
        void newstateChanged(int position);
    }

    public void setOnStateChangeListner(OnStateChangeListner onStateChangeListner){
        this.onStateChangeListner=onStateChangeListner;
    }

    public ItemsAdapter(Context context, List myItemList, boolean state) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.myItemList=myItemList;
        this.state=state;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_in_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String path = "https://fathomless-journey-91846.herokuapp.com/" + myItemList.get(position).getProd_img();

        Picasso.get()
                .load(path)
                .resize(100, 100)
                .centerCrop()
                .into(holder.itemImage);

        holder.itemName.setText(myItemList.get(position).getProd_name());
        holder.itemCategory.setText(myItemList.get(position).getProd_id().toString());
        holder.itemPrice.setText(myItemList.get(position).getProd_price().toString());
        holder.itemChecked.setChecked(myItemList.get(position).isCheck());

        if(state){
            holder.itemChecked.setVisibility(View.VISIBLE);
        }
        else{
            holder.itemChecked.setVisibility(View.INVISIBLE);
        }

        holder.itemChecked.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onStateChangeListner != null) {
                    Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();
                    onStateChangeListner.newstateChanged(position);
                }
                else{
                    Toast.makeText(context, "onStateChangeListner is NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        TextView itemCategory;
        TextView itemPrice;
        CheckBox itemChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemChecked = itemView.findViewById(R.id.itemCheck);
        }
    }


}
