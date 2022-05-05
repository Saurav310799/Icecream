package com.example.icecreambp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("UploadImage.php")
    Call<IceModel> callUploadApi(@Part MultipartBody.Part sendimage,
                                 @Part("fname") RequestBody flavourname,
                                 @Part("price")RequestBody price);

    @GET("DisplayProduct.php")
    Call<String> STRING_CALL();
}
