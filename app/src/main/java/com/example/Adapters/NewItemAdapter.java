package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewItemAdapter extends RecyclerView.Adapter<NewItemAdapter.ViewHolder>  {

    private LayoutInflater mInflater;
    private List<ProductDescriptionResponse> myItemList;
    private Context context;
    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void OnItemChecked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemName;
        TextView itemCategory;
        TextView itemPrice;
        CheckBox myCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            myCheck = itemView.findViewById(R.id.acceptThisItem);
        }
    }

    public NewItemAdapter(Context context, List myItemList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.myItemList=myItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_new_in_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String path = "https://lit-earth-71252.herokuapp.com/"+myItemList.get(position).getProd_img();

        Picasso.get()
                .load(path)
                .resize(100, 100)
                .centerCrop()
                .into(holder.itemImage);

        holder.itemName.setText(myItemList.get(position).getProd_name());
        holder.itemCategory.setText(myItemList.get(position).getCategory_name());
        holder.itemPrice.setText(myItemList.get(position).getProd_price().toString());

        holder.myCheck.setVisibility(View.INVISIBLE);

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

}
