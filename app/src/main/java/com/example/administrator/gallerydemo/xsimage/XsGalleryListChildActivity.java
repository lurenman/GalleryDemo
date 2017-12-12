package com.example.administrator.gallerydemo.xsimage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.gallerydemo.MainActivity;
import com.example.administrator.gallerydemo.R;
import com.example.administrator.gallerydemo.utils.CustomToast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/20.
 */

public class XsGalleryListChildActivity extends Activity {
    private static final String TAG = "XsGalleryListChildActiv";
    private ImageView iv_back;
    private TextView tv_title;//标题
    private GridView gridView;
    private RelativeLayout rl_toombar;
    private HorizontalScrollView scrollview;
    private LinearLayout selected_image_layout;//底部那个放选中imageView
    private TextView tv_upload;//上传

    // private String pageactivity = "";
    private int imgcount = 1;
    private FileTraversal fileTraversal;
    private ImgsAdapter imgsAdapter;
    private HashMap<Integer, ImageView> hashImage;//保存底部兰的imageView的位置及view
    ArrayList<String> filelist;//底部兰图片路径集合
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initEvents();
    }


    private void initViews() {
        setContentView(R.layout.activity_xs_gallerylist_child);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        gridView = (GridView) findViewById(R.id.gridView);
        rl_toombar = (RelativeLayout) findViewById(R.id.rl_toombar);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
        selected_image_layout = (LinearLayout) findViewById(R.id.selected_image_layout);
        tv_upload = (TextView) findViewById(R.id.tv_upload);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // pageactivity = bundle.getString("pageactivity");
            imgcount = bundle.getInt("imgcount", 1);
            fileTraversal = bundle.getParcelable("data");
            tv_title.setText(bundle.getString("name"));
        }
        imgsAdapter = new ImgsAdapter(this, fileTraversal.filecontent, onItemClickClass);
        gridView.setAdapter(imgsAdapter);
        hashImage = new HashMap<Integer, ImageView>();
        filelist = new ArrayList<String>();
    }

    private void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XsGalleryListChildActivity.this.finish();
            }
        });
        //上传的操作
        tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_image_layout.getChildCount() > 0) {
//                    switch (pageactivity) {
//                        case "UpLoadImageActivity":
//                            UpLoadImageActivity.Istabhost = true;
//                            UpLoadImageActivity.listfile = filelist;
//                            break;
//                    }
//                    XsGalleryActivity.instance.finish();
//                    XsGalleryListChildActivity.this.finish();
                    MainActivity.list=filelist;
                    Log.e(TAG,"-------"+filelist.get(0).toString());
                } else {
                    CustomToast.showToast(getApplicationContext(), "请选择图片", 1500);
                }
            }
        });
    }

    ImgsAdapter.OnItemClickClass onItemClickClass = new ImgsAdapter.OnItemClickClass() {
        @SuppressLint("NewApi")
        @Override
        public void OnItemClick(View v, int Position, CheckBox checkBox, ImageView imageView1) {

            String filapath = fileTraversal.filecontent.get(Position);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                selected_image_layout.removeView(hashImage.get(Position));
                filelist.remove(filapath);
                imageView1.setAlpha((float) 1);
                tv_upload.setText("上传(" + selected_image_layout.getChildCount() + ")张");
            } else {
                if (selected_image_layout.getChildCount() >= imgcount) {
                    //这块toast最好在多次点击的情况下只显示一次
                    //  Toast.makeText(getApplicationContext(),"最多选择" +imgcount+"张图片",Toast.LENGTH_SHORT).show();
                    CustomToast.showToast(getApplicationContext(), "最多选择" + imgcount + "张图片", 1500);
                    return;
                }
                try {
                    checkBox.setChecked(true);
                    //imageView 这个是底部兰或的imageview
                    ImageView imageView = iconImage(filapath, Position, checkBox, imageView1);
                    imageView1.setAlpha((float) 0.7);
                    LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    para.height = LinearLayout.LayoutParams.FILL_PARENT;
                    para.width = 100; //底部兰imageview宽度设置100像素
                    para.setMargins(5, 5, 5, 5);
                    imageView.setLayoutParams(para);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    if (imageView != null) {
                        hashImage.put(Position, imageView);
                        filelist.add(filapath);
                        selected_image_layout.addView(imageView);
                        tv_upload.setText("上传(" + selected_image_layout.getChildCount() + ")张");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public ImageView iconImage(String filepath, int index, CheckBox checkBox, ImageView
            imageView1) throws FileNotFoundException {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(rl_toombar.getMeasuredHeight() - 10,
                rl_toombar.getMeasuredHeight() - 10);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(params);
        //原图
        Glide.with(XsGalleryListChildActivity.this).load(filepath).centerCrop().placeholder(R.mipmap
                .icon_defaultimg)//占位图
                .error(R.mipmap.icon_errorimg)//设置错误图
                .crossFade()//动画效果
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓存整张图片
                .into(imageView);
        imageView.setOnClickListener(new ImgOnclick(filepath, checkBox, imageView1));
        return imageView;
    }

    //这个就是点击下面图片的时候取消用的
    class ImgOnclick implements View.OnClickListener {
        String filepath;
        CheckBox checkBox;
        ImageView imageView1;

        public ImgOnclick(String filepath, CheckBox checkBox, ImageView imageView1) {
            this.filepath = filepath;
            this.checkBox = checkBox;
            this.imageView1 = imageView1;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            checkBox.setChecked(false);
            selected_image_layout.removeView(v);
            imageView1.setImageAlpha(255);
            tv_upload.setText("上传(" + selected_image_layout.getChildCount() + ")张");
            filelist.remove(filepath);
        }
    }


}
