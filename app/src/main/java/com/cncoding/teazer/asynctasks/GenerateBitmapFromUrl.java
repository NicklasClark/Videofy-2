package com.cncoding.teazer.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.ui.fragment.activity.EditProfile.bitmaptoByte;

/**
 * Created by amit on 18/12/17.
 */

public class GenerateBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {

    private final Context context;
    private String filePath;

    public GenerateBitmapFromUrl(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... imageUrl) {
        Bitmap bitmap = null;
        if (imageUrl != null) {
            try {
                //            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(url));
                try {
                    URL url = new URL(imageUrl[0]);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch(IOException e) {
                    e.printStackTrace();
                }
                //            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        byte[] bte = bitmaptoByte(result);
        //            SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        //            SharedPreferences.Editor editor = preferences.edit();
        //            editor.putString("MYIMAGES", url);
        //            editor.apply();

        // File profileImage = new File(r.getPath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bte);
        MultipartBody.Part body = MultipartBody.Part.createFormData("media", "profile_image.jpg", reqFile);
        saveDataToDatabase(body);
    }

    private void saveDataToDatabase(MultipartBody.Part body) {

        ApiCallingService.User.updateUserProfileMedia(body, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {

                    if (response.code() == 400) {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
