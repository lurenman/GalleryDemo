package com.example.administrator.gallerydemo.xsimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.gallerydemo.R;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImgFileListAdapter extends BaseAdapter {
    Context context;
    String filecount = "filecount";
    String filename = "filename";
    String imgpath = "imgpath";
    List<HashMap<String, String>> listdata;
    Util util;
    Bitmap[] bitmaps;
    private int index = -1;
   // List<View> holderlist;

    public ImgFileListAdapter(Context context, List<HashMap<String, String>> listdata) {
        this.context = context;
        this.listdata = listdata;
        bitmaps = new Bitmap[listdata.size()];
        util = new Util(context);
        //holderlist = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        Holder holder;
        if (position != index && position > index) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.imgfileadapter, null);
            holder.photo_imgview = (ImageView) convertView.findViewById(R.id.filephoto_imgview);
            holder.filecount_textview = (TextView) convertView.findViewById(R.id.filecount_textview);
            holder.filename_textView = (TextView) convertView.findViewById(R.id.filename_textview);
            convertView.setTag(holder);
           // holderlist.add(convertView);
        } else {
            //holder = (Holder) holderlist.get(position).getTag();
            holder = (Holder) convertView.getTag();
            //convertView = holderlist.get(position);
        }

        holder.filename_textView.setText(listdata.get(position).get(filename));
        holder.filecount_textview.setText(listdata.get(position).get(filecount));

        Glide.with(context).load(listdata.get(position).get(imgpath)).bitmapTransform(new
                RoundedCornersTransformation(context, 20, 0, RoundedCornersTransformation
                .CornerType.ALL))
                .placeholder(R.mipmap.imgbg)//占位图
                .error(R.mipmap.icon_errorimg)//设置错误图
                .crossFade()//动画效果
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓存图片
                .into(holder.photo_imgview);
        return convertView;
    }

    class Holder {
        ImageView photo_imgview;
        TextView filecount_textview;
        TextView filename_textView;
    }

}
