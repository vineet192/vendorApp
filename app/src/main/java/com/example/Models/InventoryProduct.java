package com.example.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class InventoryProduct implements Parcelable
{

    @SerializedName("prod_id")
    private int productId;

    @SerializedName("prod_name")
    private String title;

    @SerializedName("prod_img")
    private String imageUrl;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("prod_price")
    private double productPrice;

    @SerializedName("prod_rating")
    private int productRating;

    @SerializedName("prod_desc")
    private String productDescription;

    private boolean check;

    public InventoryProduct(String title, String imageUrl)
    {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public InventoryProduct(int productId, String title, String imageUrl, int categoryId, String categoryName, double productPrice, int productRating, String productDescription, boolean check)
    {
        this.productId = productId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productPrice = productPrice;
        this.productRating = productRating;
        this.productDescription = productDescription;
        this.check = check;
    }

    protected InventoryProduct(Parcel in)
    {
        productId = in.readInt();
        title = in.readString();
        imageUrl = in.readString();
        categoryId = in.readInt();
        categoryName = in.readString();
        productPrice = in.readDouble();
        productRating = in.readInt();
        productDescription = in.readString();
        check = in.readByte() != 0;
    }

    public static final Creator<InventoryProduct> CREATOR = new Creator<InventoryProduct>()
    {
        @Override
        public InventoryProduct createFromParcel(Parcel in)
        {
            return new InventoryProduct(in);
        }

        @Override
        public InventoryProduct[] newArray(int size)
        {
            return new InventoryProduct[size];
        }
    };

    public String getTitle()
    {
        return title;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public double getProductPrice()
    {
        return productPrice;
    }

    public int getProductRating()
    {
        return productRating;
    }

    public String getProductDescription()
    {
        return productDescription;
    }

    public int getProductId()
    {
        return productId;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (!(obj instanceof InventoryProduct)) {
            return false;
        }
        InventoryProduct otherProduct = (InventoryProduct)obj;
        if(this.productId!=otherProduct.getProductId())
        {
            return false;
        }
        return true;
    }

    public boolean isCheck()
    {
        return check;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(productId);
        parcel.writeString(title);
        parcel.writeString(imageUrl);
        parcel.writeInt(categoryId);
        parcel.writeString(categoryName);
        parcel.writeDouble(productPrice);
        parcel.writeInt(productRating);
        parcel.writeString(productDescription);
        parcel.writeByte((byte) (check ? 1 : 0));
    }
}
