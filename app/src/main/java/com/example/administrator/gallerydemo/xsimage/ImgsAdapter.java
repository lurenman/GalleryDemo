package com.example.administrator.gallerydemo.xsimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.gallerydemo.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImgsAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private Util util;
    private OnItemClickClass onItemClickClass;
    private int index = -1;
    List<View> holderlist;

    public ImgsAdapter(Context context, List<String> data, OnItemClickClass onItemClickClass) {
        this.context = context;
        this.data = data;
        this.onItemClickClass = onItemClickClass;
        util = new Util(context);
        holderlist = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Holder holder;
        if (position != index && position > index) {
            holder = new Holder();
            index = position;
            convertView = LayoutInflater.from(context).inflate(R.layout.imgsitem, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
            holderlist.add(convertView);
        } else {
            holder = (Holder) holderlist.get(position).getTag();
            convertView = holderlist.get(position);
        }
        Glide.with(context).load(data.get(position))
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0, RoundedCornersTransformation
                        .CornerType.ALL))
                .placeholder(R.mipmap.imgbg)//占位图
                .error(R.mipmap.icon_errorimg)//设置错误图
                .crossFade()//动画效果
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓存图片
                .into(holder.imageView);
        convertView.setOnClickListener(new OnPhotoClick(position, holder.checkBox, holder.imageView));
        return convertView;
    }

    class Holder {
        ImageView imageView;
        CheckBox checkBox;
    }
    public interface OnItemClickClass {
        public void OnItemClick(View v, int Position, CheckBox checkBox, ImageView imageView);
    }

    class OnPhotoClick implements OnClickListener {
        int position;
        CheckBox checkBox;
        ImageView imageView;

        public OnPhotoClick(int position, CheckBox checkBox, ImageView imageView) {
            this.position = position;
            this.checkBox = checkBox;
            this.imageView = imageView;
        }

        @Override
        public void onClick(View v) {
            if (data != null && onItemClickClass != null) {
                onItemClickClass.OnItemClick(v, position, checkBox, imageView);
            }
        }
    }

}
