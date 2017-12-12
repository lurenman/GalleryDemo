package com.example.administrator.gallerydemo.xsimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.gallerydemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/10/19.
 * requires android.permission.READ_EXTERNAL_STORAGE 需要动态获取读的权限
 */

public class XsGalleryActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "XsGalleryActivity";
    public static XsGalleryActivity instance=null;
    private static final int STORAGE_PERMISSIONS = 200;
   // private String pageactivity = "";
    private int imgcount = 1;
    private ListView lv_listView;
    private ImageView iv_back;
    private Util util;
    private ImgFileListAdapter listAdapter;
    private List<FileTraversal> locallist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xs_gallerylist);
        XsGalleryActivity.instance=this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           // pageactivity = bundle.getString("pageactivity");
            imgcount = bundle.getInt("imgcount",1);
        }
        initViews();
        initPermission();
        initEvents();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_listView = (ListView) findViewById(R.id.lv_listView);


    }

    private void initPermission() {
        //这块就申请写权限把报读权限错误 requires android.permission.READ_EXTERNAL_STORAGE
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "需要读写权限", 200, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            initSomeing();
        }
    }

    private void initSomeing() {
        util = new Util(this);
        //这块就是读取数据
        locallist = util.LocalImgFileList();
        List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
        //这块把每个条目的第一图片取出来设给listView
        if (locallist != null) {
            for (int i = 0; i < locallist.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("filecount", "共" + locallist.get(i).filecontent.size() + "张");
                map.put("imgpath", locallist.get(i).filecontent.get(0) == null ? null :
                        (locallist.get(i).filecontent.get(0)));
                map.put("filename", locallist.get(i).filename);
                listdata.add(map);
            }
        }
        listAdapter = new ImgFileListAdapter(this, listdata);
        lv_listView.setAdapter(listAdapter);
    }

    private void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XsGalleryActivity.this.finish();
            }
        });
        //listView 点击事件
        lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView filename_textview = (TextView) view.findViewById(R.id.filename_textview);
                Intent intent = new Intent(XsGalleryActivity.this, XsGalleryListChildActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", filename_textview.getText().toString());
                bundle.putParcelable("data", locallist.get(position));
               // bundle.putString("pageactivity", pageactivity);
                bundle.putInt("imgcount", imgcount);
                intent.putExtras(bundle);
                //这块跳转动画根据项目需求做
                startActivity(intent);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 200)
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                initSomeing();
            }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 200)
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Toast.makeText(getApplicationContext(),"没有读写权限无法使用图库功能",Toast.LENGTH_SHORT).show();
    }
}
