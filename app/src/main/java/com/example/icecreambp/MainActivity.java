package com.example.icecreambp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText flavourname,price;
    Button choose,submit,viewProducts;
    private CharSequence[] options= {"Camera","Gallery","Cancel"};
    private String selectedImage;
    ImageView yourImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flavourname=findViewById(R.id.flavour);
        price=findViewById(R.id.price);
        choose=findViewById(R.id.uploadImage);
        submit=findViewById(R.id.submit);
        yourImage=findViewById(R.id.yourImage);
        viewProducts=findViewById(R.id.viewAll);

        requirePermission();

        choose.setOnClickListener(view -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select Image");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Camera")){
                            Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePic, 0);
                        }
                        else if(options[which].equals("Gallery")) {
                            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(gallery, 1);
                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });

                builder.show();

        });
        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileToServer();
            }
        });

        viewProducts.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,Products.class));
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        selectedImage = FilesUtils.getPath(MainActivity.this, getImageUri(MainActivity.this, image));
                        yourImage.setImageBitmap(image);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {

                        Uri image = data.getData();
                        selectedImage = FilesUtils.getPath(MainActivity.this, image);
                        Picasso.get().load(image).into(yourImage);
                    }
            }
        }
    }


    public Uri getImageUri(Context context, Bitmap bitmap){
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "myImage","");

        return Uri.parse(path);
    }


    public void requirePermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    private void uploadFileToServer() {
        File file = new File(Uri.parse(selectedImage).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage",file.getName(),requestBody);
        RequestBody fname=RequestBody.create(MediaType.parse("multipart/form-data"),flavourname.getText().toString());
        RequestBody fprice=RequestBody.create(MediaType.parse("multipart/form-data"),price.getText().toString());

        ApiService service = RetrofitBuilder.getClient().create(ApiService.class);

        Call<IceModel> call;
        call = service.callUploadApi(filePart,fname,fprice);
        call.enqueue(new Callback<IceModel>() {


            @Override
            public void onResponse(Call<IceModel> call, Response<IceModel> response) {
                IceModel fileModel = response.body();
                Toast.makeText(MainActivity.this, fileModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IceModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}