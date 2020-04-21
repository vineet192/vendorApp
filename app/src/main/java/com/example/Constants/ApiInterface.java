package com.example.Constants;

import com.example.Models.CompleteOrderListResponse;
import com.example.Models.DeleiveryBoy;
import com.example.Models.ItemSavingResponse;
import com.example.Models.LoginRequest;
import com.example.Models.LoginResponse;
import com.example.Models.OrderHistoryResponse;
import com.example.Models.ResponseServerToken;
import com.example.Models.StatusResponse;
import com.example.Models.TokenKeySaveRequest;
import com.example.vendor.LoginRequestVendor;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface
ApiInterface {

    @POST("vendor/check/")
    Call<LoginResponse> getLogin(@Body LoginRequestVendor request);

    @POST("vendor/fcm_token_save/")
    Call<ResponseServerToken> saveFcmToken(@Body TokenKeySaveRequest request);

    @POST("delivery/check/")
    Call<LoginResponse> getLoginDelivery(@Body LoginRequest request);

    @GET("vendor/get_products/")
    Call<CompleteOrderListResponse> getallProducts();

    @FormUrlEncoded
    @POST("vendor/get_prev_products/")
    Call<CompleteOrderListResponse> getservingProducts(@Field("vendor_phone") String phone_no);

    @FormUrlEncoded
    @POST("vendor/save_products/")
    Call<ItemSavingResponse> saveMyItems(@Field("vendor_phone") String phone_no, @Field("products") ArrayList<Integer> productId);

    @FormUrlEncoded
    @POST("vendor/history/")
    Call<OrderHistoryResponse> getMyHistory(@Field("vendor_phone") String phone_no);

    @FormUrlEncoded
    @POST("vendor/ongoing/")
    Call<OrderHistoryResponse> getNewOrders(@Field("vendor_phone") String phone_no);

    @FormUrlEncoded
    @POST("vendor/delivery_details/")
    Call<DeleiveryBoy> getDeleiveryBoy(@Field("vendor_phone") String phone_no, @Field("order_id") String order_id);


    @POST("vendor/dispatch/")
    Call<ItemSavingResponse> dispatchVendorOrder(@Body JsonObject order);

    @POST("vendor/dispatch/")
    Call<ItemSavingResponse> dispatchOrder(@Field("vendor_phone") String phone_no, @Field("order_id") String order_id);

    @POST("vendor/prepared/")
    Call<JsonObject> prepareOrder(@Body JsonObject order);

    @POST("suscriptionresponse/")
    Call<JsonObject> getSubscriptionResponse(@Body JsonObject order);

    @POST("vendorresponse/")
    Call<JsonObject> getProductResponse(@Body JsonObject order);

    @POST("vendor/activate/")
    Call<StatusResponse> setStatus(@Field("vendor_phone") String phone_no, @Field("status") String status);

    @POST("vendor/activate/")
    Call<StatusResponse> setVendorStatus(@Body JsonObject body);


}
