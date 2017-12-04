package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.home.camera.UploadFragment;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteFriend extends AppCompatActivity {

    Context context;
    @BindView(R.id.facebookShareLayout)
    LinearLayout facebookShareLayout;
    @BindView(R.id.watsppLayout)
    LinearLayout watsppLayout;
    @BindView(R.id.gmailShare)
    LinearLayout gmailShare;

    @BindView(R.id.smsLayout)
    LinearLayout smsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        ButterKnife.bind(this);
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Invite Friends</font>"));

        facebookShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://www.youtube.com/watch?v=jBfo87raroE"))
                        .setContentTitle("Teazer")
                        .setContentDescription(
                                "Hello").build();
                ShareDialog shareDialog = new ShareDialog(InviteFriend.this);
                shareDialog.show(content);
                ShareApi.share(content, null);

//                final String s = "https://s3.ap-south-1.amazonaws.com/teazer-medias/Teazer/post/2/4/1511202104939_thumb.png";
//                new AsyncTask<Void, Void, Bitmap>() {
//                    @Override
//                    protected Bitmap doInBackground(final Void... params) {
//                        Bitmap bitmap = null;
//                        try {
//                            final URL url = new URL(s);
//                            try {
//                                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        return bitmap;
//                    }
//
//                    @Override
//                    protected void onPostExecute(final Bitmap result) {
//
////
////                        SharePhoto photo = new SharePhoto.Builder()
////                                .setBitmap(result)
////                                .build();
////                        SharePhotoContent content = new SharePhotoContent.Builder()
////                                .addPhoto(photo)
////                                .build();
////
////                        ShareDialog shareDialog = new ShareDialog(InviteFriend.this);
////                        shareDialog.show(content);
////                        ShareApi.share(content, null);
//                    }
//                }.execute();

            }

        });
        watsppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getPackageManager();
                try {
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    Intent watsppIntent = new Intent(Intent.ACTION_SEND);
                    watsppIntent.setType("text/plain");
                    watsppIntent.setPackage("com.whatsapp");
                    watsppIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=jBfo87raroE");
                    startActivity(watsppIntent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(InviteFriend.this, "Please install whatsapp app", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        gmailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
//                startActivity(intent);
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"fhabib4@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "https://www.youtube.com/watch?v=jBfo87raroE");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(context,"Gmail App is not installed",Toast.LENGTH_SHORT).show();
            }
        });




        smsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
//                    startActivity(intent);


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cnapplications.com"));
                    startActivity(browserIntent);

                    try {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra("sms_body", "https://www.youtube.com/watch?v=jBfo87raroE");
                        intent.setData(Uri.parse("sms:"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException anfe) {
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later!",
                                Toast.LENGTH_LONG).show();
                        Log.d("Error" , "Error");
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }


}
