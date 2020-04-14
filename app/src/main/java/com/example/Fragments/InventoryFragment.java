package com.example.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Adapters.InventoryItemsAdapter;
import com.example.Models.InventoryProduct;
import com.example.vendor.EditInventoryActivity;
import com.example.vendor.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class InventoryFragment extends Fragment
{
    TextView choppedTabTitle, juiceTabTitle, rawTabTitle, editFruits;
    Button availableButton, unavaialableButton;
    boolean isAvailableSelected = true;
    View juiceTabLine, rawTabLine, choppedTabLine;
    EditText searchBar;
    RecyclerView productsList;
    private List<InventoryProduct> allProducts = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> previouslySavedProducts = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> unavailableProducts;
    private List<InventoryProduct> rawAvailable = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> choppedAvailable = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> juiceAvailable = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> rawUnavailable = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> choppedUnavailable = new ArrayList<InventoryProduct>();
    private List<InventoryProduct> juiceUnavailable = new ArrayList<InventoryProduct>();
    private String phone = "3";
    InventoryItemsAdapter adapter;
    RequestQueue requestQueue;
    JsonObjectRequest allProductsRequest;
    JsonObjectRequest savedProductsRequest;
    SwipeRefreshLayout swipeRefreshLayout;

    //Debug elements
    Button submitVendorNumber;
    EditText editVendorNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.inventory_fragment_layout, container, false);

        //Debug elements
//        submitVendorNumber = (Button) rootView.findViewById(R.id.vendor_number_submit);
//        editVendorNumber = (EditText) rootView.findViewById(R.id.edit_vendor_number);

//        submitVendorNumber.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                phone = editVendorNumber.getText().toString();
//                editVendorNumber.setText("");
                instantiateRequests();
//                Toast.makeText(getContext(), "Updated the vendor number to " + phone, Toast.LENGTH_SHORT).show();
//            }
//        });


        //Instantiate volley request queue.
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Accessing the recycler View
        productsList = (RecyclerView) rootView.findViewById(R.id.products_inventory_list);

        //Instantiate the swipe refresh layout
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.inventory_swipe_refresh);
        swipeRefreshLayout.setRefreshing(true);

        //Instantiate adapter
        adapter = new InventoryItemsAdapter(previouslySavedProducts);
        productsList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        productsList.setAdapter(adapter);

        //Accessing the tabs from the root view.
        View choppedTab = rootView.findViewById(R.id.tab_chopped);
        View juiceTab = rootView.findViewById(R.id.tab_juice);
        View rawTab = rootView.findViewById(R.id.tab_fresh);

        //Accessing the search bar
        searchBar = (EditText) rootView.findViewById(R.id.search_bar_home);


        //Accessing the edit button.
        editFruits = (TextView) rootView.findViewById(R.id.edit_fruits);

        //Accessing the title of each tab.
        choppedTabTitle = (TextView) choppedTab.findViewById(R.id.tab_title);
        juiceTabTitle = (TextView) juiceTab.findViewById(R.id.tab_title);
        rawTabTitle = (TextView) rawTab.findViewById(R.id.tab_title);

        //Accessing the line markers for each tab
        choppedTabLine = choppedTab.findViewById(R.id.tab_line);
        juiceTabLine = juiceTab.findViewById(R.id.tab_line);
        rawTabLine = rawTab.findViewById(R.id.tab_line);

        //Having chopped to be selected by default
        choppedTabLine.setVisibility(View.VISIBLE);

        //Setting the title of each tab.
        choppedTabTitle.setText("Chopped");
        juiceTabTitle.setText("Juice");
        rawTabTitle.setText("Raw");

        availableButton = rootView.findViewById(R.id.available_btn);
        unavaialableButton = rootView.findViewById(R.id.unavailable_btn);

        //Performing get request to store all products and to get previously saved products by user

        //Request for all products
        allProductsRequest = new JsonObjectRequest
                (Request.Method.GET, "https://gocoding.azurewebsites.net/vendor/get_products/", null, new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        JSONArray productsArray = null;
                        try
                        {
                            productsArray = response.getJSONArray("products");
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        allProducts.clear();
                        for (int i = 0; i < productsArray.length(); i++)
                        {
                            try
                            {
                                allProducts.add(gson.fromJson(productsArray.get(i).toString(), InventoryProduct.class));
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        String message = null;
                        swipeRefreshLayout.setRefreshing(false);
                        if (volleyError instanceof NetworkError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError)
                        {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError)
                        {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
        try
        {
            //Request for previously saved products
            savedProductsRequest = new JsonObjectRequest
                    (Request.Method.POST,
                            "https://gocoding.azurewebsites.net/vendor/get_prev_products/",
                            new JSONObject("{'vendor_phone':'" + phone + "'}"), new Response.Listener<JSONObject>()
                    {

                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                JSONArray productsArray = response.getJSONArray("products");
                                Gson gson = new Gson();
                                previouslySavedProducts.clear();
                                for (int i = 0; i < productsArray.length(); i++)
                                {
                                    previouslySavedProducts.add(gson.fromJson(productsArray.get(i).toString(), InventoryProduct.class));
                                }

                                //Get unavailable products by subtracting available products from all products
                                unavailableProducts = getUnavailableProducts();

                                //Segregate the products
                                segregateProducts();
                                if (isAvailableSelected)
                                {
                                    if (juiceTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(juiceAvailable);
                                    } else if (rawTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(rawAvailable);
                                    } else
                                    {
                                        adapter.setProducts(choppedAvailable);
                                    }

                                } else
                                {
                                    if (juiceTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(juiceUnavailable);
                                    } else if (rawTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(rawUnavailable);
                                    } else
                                    {
                                        adapter.setProducts(choppedUnavailable);
                                    }
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            productsList.setAdapter(adapter);

                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener()
                    {

                        @Override
                        public void onErrorResponse(VolleyError volleyError)
                        {
                            String message = null;
                            swipeRefreshLayout.setRefreshing(false);
                            if (volleyError instanceof NetworkError)
                            {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError)
                            {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (volleyError instanceof AuthFailureError)
                            {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ParseError)
                            {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (volleyError instanceof NoConnectionError)
                            {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof TimeoutError)
                            {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }

                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        requestQueue.add(allProductsRequest);
        requestQueue.add(savedProductsRequest);


        availableButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                availableButton.setBackgroundResource(R.drawable.click_shape);
                unavaialableButton.setBackgroundResource(R.drawable.shape);
                availableButton.setTextColor(Color.parseColor("#F05A48"));
                unavaialableButton.setTextColor(Color.BLACK);
                isAvailableSelected = true;

                //Populate recycler view with available items
                if (!previouslySavedProducts.isEmpty())
                {
                    if (juiceTabLine.getVisibility() == View.VISIBLE)
                    {
                        adapter.setProducts(juiceAvailable);
                    } else if (rawTabLine.getVisibility() == View.VISIBLE)
                    {
                        adapter.setProducts(rawAvailable);
                    } else
                    {
                        adapter.setProducts(choppedAvailable);
                    }
                } else
                {
                    Toast.makeText(getContext(), "Loading !", Toast.LENGTH_SHORT).show();
                }


            }
        });

        unavaialableButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                unavaialableButton.setBackgroundResource(R.drawable.click_shape);
                availableButton.setBackgroundResource(R.drawable.shape);
                unavaialableButton.setTextColor(Color.parseColor("#f05a48"));
                availableButton.setTextColor(Color.BLACK);
                isAvailableSelected = false;

                //Populate recycler view with unavailable items
                if (unavailableProducts != null)
                {
                    if (juiceTabLine.getVisibility() == View.VISIBLE)
                    {
                        adapter.setProducts(juiceUnavailable);
                    } else if (rawTabLine.getVisibility() == View.VISIBLE)
                    {
                        adapter.setProducts(rawUnavailable);
                    } else
                    {
                        adapter.setProducts(choppedUnavailable);
                    }
                    adapter.notifyDataSetChanged();
                } else
                {
                    Toast.makeText(getContext(), "Loading..", Toast.LENGTH_SHORT).show();
                }


            }
        });

        choppedTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                choppedTabLine.setVisibility(View.VISIBLE);
                juiceTabLine.setVisibility(View.INVISIBLE);
                rawTabLine.setVisibility(View.INVISIBLE);
                searchBar.setHint("Search in Chopped");

                //Populate recycler view
                if (isAvailableSelected)
                {

                    adapter.setProducts(choppedAvailable);
                } else
                {
                    adapter.setProducts(choppedUnavailable);
                }
            }
        });

        juiceTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                choppedTabLine.setVisibility(View.INVISIBLE);
                juiceTabLine.setVisibility(View.VISIBLE);
                rawTabLine.setVisibility(View.INVISIBLE);
                searchBar.setHint("Search in Juice");
                if (isAvailableSelected)
                {

                    adapter.setProducts(juiceAvailable);
                } else
                {
                    adapter.setProducts(juiceUnavailable);
                }
            }
        });

        rawTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                choppedTabLine.setVisibility(View.INVISIBLE);
                juiceTabLine.setVisibility(View.INVISIBLE);
                rawTabLine.setVisibility(View.VISIBLE);
                searchBar.setHint("Search in Raw");

                //populate recyclerview
                if (isAvailableSelected)
                {

                    adapter.setProducts(rawAvailable);
                } else
                {
                    adapter.setProducts(rawUnavailable);
                }
            }
        });

        editFruits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent in = new Intent(getContext(), EditInventoryActivity.class);

                if (choppedTabLine.getVisibility() == View.VISIBLE)
                {
                    in.putExtra("fruit_type", "Chopped");
                    in.putParcelableArrayListExtra("available", (ArrayList<? extends Parcelable>) choppedAvailable);
                    in.putParcelableArrayListExtra("unavailable", (ArrayList<? extends Parcelable>) choppedUnavailable);
                } else if (juiceTabLine.getVisibility() == View.VISIBLE)
                {
                    in.putExtra("fruit_type", "Juice");
                    in.putParcelableArrayListExtra("available", (ArrayList<? extends Parcelable>) juiceAvailable);
                    in.putParcelableArrayListExtra("unavailable", (ArrayList<? extends Parcelable>) juiceUnavailable);

                } else
                {
                    in.putExtra("fruit_type", "Raw");
                    in.putParcelableArrayListExtra("available", (ArrayList<? extends Parcelable>) rawAvailable);
                    in.putParcelableArrayListExtra("unavailable", (ArrayList<? extends Parcelable>) rawUnavailable);
                }
                startActivity(in);
            }
        });

        //Set up the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                requestQueue.add(allProductsRequest);
                requestQueue.add(savedProductsRequest);
            }
        });

        return rootView;
    }

    private void segregateProducts()
    {
        rawUnavailable.clear();
        choppedUnavailable.clear();
        juiceUnavailable.clear();
        rawAvailable.clear();
        juiceAvailable.clear();
        choppedAvailable.clear();
        for (InventoryProduct product : unavailableProducts)
        {
            if (product.getCategoryId() == 1)
            {
                rawUnavailable.add(product);
            } else if (product.getCategoryId() == 2)
            {
                choppedUnavailable.add(product);
            } else if (product.getCategoryId() == 3)
            {
                juiceUnavailable.add(product);
            }
        }

        for (InventoryProduct product : previouslySavedProducts)
        {
            if (product.getCategoryId() == 1)
            {
                rawAvailable.add(product);
            } else if (product.getCategoryId() == 2)
            {
                choppedAvailable.add(product);
            } else if (product.getCategoryId() == 3)
            {
                juiceAvailable.add(product);
            }
        }
    }


    private List<InventoryProduct> getUnavailableProducts()
    {
        List<InventoryProduct> unavailableProducts = new ArrayList<InventoryProduct>(allProducts);

        unavailableProducts.removeAll(previouslySavedProducts);
        return unavailableProducts;
    }

    //Debug elements
    private void instantiateRequests()
    {
        //Request for all products
        allProductsRequest = new JsonObjectRequest
                (Request.Method.GET, "https://gocoding.azurewebsites.net/vendor/get_products/", null, new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        JSONArray productsArray = null;
                        try
                        {
                            productsArray = response.getJSONArray("products");
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        allProducts.clear();
                        for (int i = 0; i < productsArray.length(); i++)
                        {
                            try
                            {
                                allProducts.add(gson.fromJson(productsArray.get(i).toString(), InventoryProduct.class));
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        String message = null;
                        swipeRefreshLayout.setRefreshing(false);
                        if (volleyError instanceof NetworkError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError)
                        {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError)
                        {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
        try
        {
            //Request for previously saved products
            savedProductsRequest = new JsonObjectRequest
                    (Request.Method.POST,
                            "https://gocoding.azurewebsites.net/vendor/get_prev_products/",
                            new JSONObject("{'vendor_phone':'" + phone + "'}"), new Response.Listener<JSONObject>()
                    {

                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                JSONArray productsArray = response.getJSONArray("products");
                                Gson gson = new Gson();
                                previouslySavedProducts.clear();
                                for (int i = 0; i < productsArray.length(); i++)
                                {
                                    previouslySavedProducts.add(gson.fromJson(productsArray.get(i).toString(), InventoryProduct.class));
                                }

                                //Get unavailable products by subtracting available products from all products
                                unavailableProducts = getUnavailableProducts();

                                //Segregate the products
                                segregateProducts();
                                if (isAvailableSelected)
                                {
                                    if (juiceTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(juiceAvailable);
                                    } else if (rawTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(rawAvailable);
                                    } else
                                    {
                                        adapter.setProducts(choppedAvailable);
                                    }

                                } else
                                {
                                    if (juiceTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(juiceUnavailable);
                                    } else if (rawTabLine.getVisibility() == View.VISIBLE)
                                    {
                                        adapter.setProducts(rawUnavailable);
                                    } else
                                    {
                                        adapter.setProducts(choppedUnavailable);
                                    }
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            productsList.setAdapter(adapter);

                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener()
                    {

                        @Override
                        public void onErrorResponse(VolleyError volleyError)
                        {
                            String message = null;
                            swipeRefreshLayout.setRefreshing(false);
                            if (volleyError instanceof NetworkError)
                            {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError)
                            {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (volleyError instanceof AuthFailureError)
                            {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ParseError)
                            {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (volleyError instanceof NoConnectionError)
                            {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof TimeoutError)
                            {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }

                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
