package com.example.vendor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EditInventoryActivity extends AppCompatActivity implements InventoryItemsAdapter.OnProductToggleChangedListener
{
    String fruitType;
    TextView titleType;
    TextView editButton;
    EditText searchBar;
    RecyclerView editProductsList;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<InventoryProduct> allProducts;
    private List<InventoryProduct> availableProducts;
    private List<InventoryProduct> unavailableProducts;
    InventoryItemsAdapter adapter;
    JsonObjectRequest allProductsRequest;
    JsonObjectRequest savedProductsRequest;
    JsonObjectRequest editProductsRequest;
    RequestQueue requestQueue;
    List<String> savedProductTitles;
    private int category_id;
    private String phone = "3";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_edit_inventory);
        Intent in = getIntent();
        fruitType = in.getStringExtra("fruit_type");
        if (fruitType.equals("Raw"))
        {
            category_id = 1;
        } else if (fruitType.equals("Chopped"))
        {
            category_id = 2;
        } else
        {
            category_id = 3;
        }

        availableProducts = in.getParcelableArrayListExtra("available");
        unavailableProducts = in.getParcelableArrayListExtra("unavailable");
        allProducts = new ArrayList<>();
        allProducts.addAll(availableProducts);
        allProducts.addAll(unavailableProducts);

        allProducts.sort(new Comparator<InventoryProduct>()
        {
            @Override
            public int compare(InventoryProduct inventoryProduct, InventoryProduct t1)
            {
                return inventoryProduct.getTitle().compareTo(t1.getTitle());
            }
        });

        savedProductTitles = new ArrayList<>();

        //Instantiating saved product titles.
        for (InventoryProduct p : availableProducts)
        {
            savedProductTitles.add(p.getTitle());
        }

        //Instantiating the request queue
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        //Instantiate the requests
        instantiateRequests();

        if (allProducts.isEmpty())
        {
            requestQueue.add(allProductsRequest);
            requestQueue.add(savedProductsRequest);
        }


        editButton = (TextView) findViewById(R.id.edit_done);
        searchBar = (EditText) findViewById(R.id.search_bar_edit);

        //Instantiating swipe refresh layout view.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.edit_inventory_list_refresh);

        //Setting up the recycler view
        editProductsList = (RecyclerView) findViewById(R.id.edit_inventory_list);
        editProductsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InventoryItemsAdapter(allProducts);

        //Allows the products to be edited (added or removed from availability)
        adapter.setEditMode(true);
        adapter.setListener(this);
        adapter.setAvailableProductsTitles(savedProductTitles);
        editProductsList.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                adapter.getFilter().filter(editable.toString());
            }
        });


        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                try
                {
                    swipeRefreshLayout.setRefreshing(true);
                    editProductsRequest = new JsonObjectRequest
                            (Request.Method.POST, "https://gocoding.azurewebsites.net/vendor/save_products/", getEditedrequestObject(), new Response.Listener<JSONObject>()
                            {

                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(EditInventoryActivity.this, "Your products have been updated", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(EditInventoryActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                requestQueue.add(editProductsRequest);
                Intent in = new Intent(EditInventoryActivity.this, MainActivity_.class);
                startActivity(in);
            }
        });

        //Setting up the swipe to refresh view.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                requestQueue.add(allProductsRequest);
                requestQueue.add(savedProductsRequest);
            }
        });


        getSupportActionBar().hide();//Hide the title bar

        //Get instances of the title and search bar
        titleType = (TextView) findViewById(R.id.fruits_title_type);
        searchBar = (EditText) findViewById(R.id.search_bar_edit);

        titleType.append("(" + fruitType + ")");
        searchBar.setHint("Search in " + fruitType);

    }


    private JSONObject getEditedrequestObject() throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("products", new JSONArray(savedProductTitles));
        obj.put("vendor_phone", phone);

        System.out.println(obj.toString());
        return obj;
    }


    private void instantiateRequests()
    {
        //Request for all products
        allProductsRequest = new JsonObjectRequest
                (Request.Method.GET, "https://gocoding.azurewebsites.net/vendor/get_products/", null, new Response.Listener<JSONObject>()
                {

                    @RequiresApi(api = Build.VERSION_CODES.N)
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
                        //Intermediate object to check for category id.
                        InventoryProduct p;
                        for (int i = 0; i < productsArray.length(); i++)
                        {
                            try
                            {
                                p = gson.fromJson(productsArray.get(i).toString(), InventoryProduct.class);
                                if (p.getCategoryId() == category_id)
                                {
                                    allProducts.add(p);
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        allProducts.sort(new Comparator<InventoryProduct>()
                        {
                            @Override
                            public int compare(InventoryProduct inventoryProduct, InventoryProduct t1)
                            {
                                return inventoryProduct.getTitle().compareTo(t1.getTitle());
                            }
                        });


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

                        Toast.makeText(EditInventoryActivity.this, message, Toast.LENGTH_SHORT).show();
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
                                availableProducts.clear();

                                //Intermediate object to check for category id.
                                InventoryProduct p;
                                for (int i = 0; i < productsArray.length(); i++)
                                {
                                    p = gson.fromJson(productsArray.get(i).toString(), InventoryProduct.class);

                                    if (p.getCategoryId() == category_id)
                                    {
                                        availableProducts.add(p);
                                    }
                                }

                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            savedProductTitles.clear();
                            for (InventoryProduct p : availableProducts)
                            {
                                savedProductTitles.add(p.getTitle());
                            }
                            adapter.setAvailableProductsTitles(savedProductTitles);
                            editProductsList.setAdapter(null);
                            editProductsList.setAdapter(adapter);

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

                            Toast.makeText(EditInventoryActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onChange(int position, boolean isChecked)
    {
        if (isChecked)
        {
            if (!savedProductTitles.contains(allProducts.get(position).getTitle()))
            {
                savedProductTitles.add(allProducts.get(position).getTitle());
            }
        } else
        {
            savedProductTitles.remove(allProducts.get(position).getTitle());
        }

    }
}
