package com.example.Constants;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://gocoding.azurewebsites.net/";

    public static ApiInterface getAPIService() {

        return ApiManager.getClient(BASE_URL).create(ApiInterface.class);
    }
}
