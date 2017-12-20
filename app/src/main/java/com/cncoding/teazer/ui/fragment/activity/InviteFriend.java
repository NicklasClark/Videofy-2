package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

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
    @BindView(R.id.teazersite)
    ProximaNovaRegularCheckedTextView teazersite;
    String url;
    String teazerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        ButterKnife.bind(this);

        url = getString(R.string.teazer_support_url);
        teazerLink = getString(R.string.teazer_universal_link);

        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>Invite Friends</font>"));
        facebookShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(getString(R.string.teazer_universal_link)))
                        .setContentTitle("Teazer")
                        .setContentDescription(
                                "Express better").build();
                ShareDialog shareDialog = new ShareDialog(InviteFriend.this);
                shareDialog.show(content);
                ShareApi.share(content, null);

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
                    watsppIntent.putExtra(Intent.EXTRA_TEXT, teazerLink);
                    startActivity(watsppIntent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(InviteFriend.this, "Please install Whatsapp app", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        gmailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Teazer-Express better");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this cool app-Teazer. Let's do something crazy, an all new way to interact socially. Join the fun and let's keep it going " + teazerLink);
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

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra("sms_body", "Hey, checkout this cool app-Teazer. Let's do something crazy, an all new way to interact socially. Join the fun and let's keep it going " + teazerLink);
                        intent.setData(Uri.parse("sms:"));
                        startActivity(intent);

                } catch (android.content.ActivityNotFoundException anfe) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                        Log.d("Error" , "Error");
                    }
            }
        });

        teazersite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InviteFriend.this, WebViewActivity.class).putExtra("Links",url));
            }
        });
    }



}
