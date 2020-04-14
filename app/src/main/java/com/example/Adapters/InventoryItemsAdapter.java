package com.example.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.Models.InventoryProduct;
import com.example.vendor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryItemsAdapter extends RecyclerView.Adapter<InventoryItemsAdapter.ViewHolder> implements Filterable
{


    private List<InventoryProduct> products;
    //Required for filtering
    private List<InventoryProduct> productsFull;
    private List<String> availableProductsTitles;
    private boolean isEdit = false;

    private OnProductToggleChangedListener listener;


    public InventoryItemsAdapter(List<InventoryProduct> products)
    {
        this.products = products;
        productsFull = new ArrayList<>(products);
        this.notifyDataSetChanged();
    }

    public void setProducts(List<InventoryProduct> newProducts)
    {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item, parent, false);
        return new InventoryItemsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemsAdapter.ViewHolder holder, int position)
    {
        String title = products.get(position).getTitle();
        if (!isEdit)
        {
            holder.productToggle.setVisibility(View.INVISIBLE);
            holder.productToggle.setOnCheckedChangeListener(null);
        } else
        {

            holder.productToggle.setVisibility(View.VISIBLE);

            holder.productToggle.setOnCheckedChangeListener(null);
            if (availableProductsTitles.contains(products.get(position).getTitle()))
            {
                holder.productToggle.setChecked(true);
            } else
            {
                holder.productToggle.setChecked(false);
            }

        }

        holder.productToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                listener.onChange(position, b);
            }
        });

        holder.title.setText(products.get(position).getTitle());
        Picasso.get()
                .load("https://gocoding.azurewebsites.net" + products.get(position).getImageUrl())
                .placeholder(R.drawable.default_fruit)
                .fit()
                .into(holder.productImage);
    }

    @Override
    public int getItemCount()
    {
        return this.products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView productImage;
        public TextView title;
        public Switch productToggle;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productImage = itemView.findViewById(R.id.inventory_image);
            this.title = itemView.findViewById(R.id.inventory_product_title);
            productToggle = itemView.findViewById(R.id.product_toggle);
        }
    }

    private Filter productFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            List<InventoryProduct> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(productsFull);
            } else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InventoryProduct item : productsFull)
                {
                    if (item.getTitle().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            products.clear();
            products.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void setListener(OnProductToggleChangedListener listener)
    {
        this.listener = listener;
    }

    public interface OnProductToggleChangedListener
    {
        void onChange(int position, boolean isChecked);
    }

    @Override
    public Filter getFilter()
    {
        return productFilter;
    }


    public List<InventoryProduct> getProducts()
    {
        return products;
    }

    public void setEditMode(boolean toggleVal)
    {
        this.isEdit = toggleVal;
    }

    public void setAvailableProductsTitles(List<String> availableProductsTitles)
    {
        this.availableProductsTitles = availableProductsTitles;
    }

}
