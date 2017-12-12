package com.example.administrator.gallerydemo.customgallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gallerydemo.R;
import com.example.administrator.gallerydemo.xsimage.FileTraversal;
import com.example.administrator.gallerydemo.xsimage.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author: baiyang.
 * Created on 2017/12/11.
 */

public class JokesGalleryActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private ImageView iv_back;
    private LinearLayout ll_rotation;
    private ImageView iv_rotation;
    private GridView gv_gridView;
    private Boolean roateFlag = false;
    private Util util;
    private List<String> mPathDatas = new ArrayList<>();//所有图片路径的集合
    private JokesImagesAdapter mJokesImagesAdapter;
    private FrameLayout fl_seleted_content;
    private ListView lv_seleted_listView;
    private JokesSeletedAdapter mJokesSeletedAdapter;
    private List<FileTraversal> locallist;
    private TextView tv_title;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_jokes_gallery);
        mContext = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_rotation = (LinearLayout) findViewById(R.id.ll_rotation);
        iv_rotation = (ImageView) findViewById(R.id.iv_rotation);
        gv_gridView = (GridView) findViewById(R.id.gv_gridView);
        //隐藏显示的布局
        fl_seleted_content = (FrameLayout) findViewById(R.id.fl_seleted_content);
        lv_seleted_listView = (ListView) findViewById(R.id.lv_seleted_listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        initPermission();
    }

    @Override
    protected void initVariables() {


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
        util = new Util(mContext);
        mPathDatas = util.listAlldir();// 获取全部图片地址
        mJokesImagesAdapter = new JokesImagesAdapter(mContext, mPathDatas);
        gv_gridView.setAdapter(mJokesImagesAdapter);
        //这块就是读取数据
        locallist = util.LocalImgFileList(mPathDatas);
        List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
        //这块把每个条目的第一图片取出来设给listView
        if (locallist != null) {
            //第一行添加所有图片选项
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("filecount", mPathDatas.size() + "");
            map.put("imgpath", locallist.get(0).filecontent.get(0) == null ? null :
                    (locallist.get(0).filecontent.get(0)));
            map.put("filename", "所有图片");
            listdata.add(map);
            for (int i = 0; i < locallist.size(); i++) {
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("filecount", locallist.get(i).filecontent.size() + "");
                map1.put("imgpath", locallist.get(i).filecontent.get(0) == null ? null :
                        (locallist.get(i).filecontent.get(0)));
                map1.put("filename", locallist.get(i).filename);
                listdata.add(map1);
            }
        }
        mJokesSeletedAdapter = new JokesSeletedAdapter(mContext, listdata);
        lv_seleted_listView.setAdapter(mJokesSeletedAdapter);
    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisibilityFlcontent();
            }
        });
        lv_seleted_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isVisibilityFlcontent();
                if (position == 0) {
                    //所有图片选项
                    tv_title.setText("所有图片");
                    mJokesImagesAdapter.setData(mPathDatas);
                } else {
                    String filename = locallist.get(position - 1).filename;
                    tv_title.setText(filename);
                    List<String> pathUrl = locallist.get(position - 1).filecontent;
                    mJokesImagesAdapter.setData(pathUrl);
                }
            }
        });
        gv_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v_path = (View) view.findViewById(R.id.v_path);
                String path = (String) v_path.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("path",path);
                Intent intent = new Intent(mContext, JokesDetialGalleryActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //是否显示那个选中的布局
    private void isVisibilityFlcontent() {
        roateFlag = !roateFlag;
        AnimationUtil.rotateArrow(iv_rotation, roateFlag);
        if (roateFlag) {
            fl_seleted_content.setVisibility(View.VISIBLE);
        } else {
            fl_seleted_content.setVisibility(View.GONE);
        }

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 200) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                initSomeing();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 200) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), "没有读写权限无法使用图库功能", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
