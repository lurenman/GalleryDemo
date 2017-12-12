package com.example.administrator.gallerydemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.gallerydemo.customgallery.JokesGalleryActivity;
import com.example.administrator.gallerydemo.xsimage.XsGalleryActivity;
import com.example.administrator.gallerydemo.xsimage.XsGalleryListChildActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   {
    private static final String TAG = "MainActivity";
    public static ArrayList<String> list=null;
    private TextView tv_xs_gallery;
    private TextView tv_jokes_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvents();
    }

    private void initViews() {
        tv_xs_gallery=(TextView) findViewById(R.id.tv_xs_gallery);
        tv_jokes_gallery = (TextView) findViewById(R.id.tv_jokes_gallery);
    }

    private void initEvents() {

        tv_xs_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("imgcount", 4);
                Intent intent = new Intent(MainActivity.this, XsGalleryActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
//                for (int i=0;i<filelist.size();i++)
//                {
//                    String path = filelist.get(i);
//                    Log.e(TAG,""+path);
//                }
            }
        });
        tv_jokes_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JokesGalleryActivity.class);
                startActivity(intent);

            }
        });

    }
}
