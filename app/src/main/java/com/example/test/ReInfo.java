package com.example.test;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * 描述：广告信息</br>
 *
 * @author Eden Cheng</br>
 * @version 2015年4月23日 上午11:32:53
 */
public class ReInfo {
    private String id;
    private int imgPath = -1;//本地图片资源
    private ImageView mImageView;
    private Bitmap bitmap;
    private View view;
    private int _id;

    ReInfo(View view) {
        this.view = view;

    }


    public ImageView getmImageView() {
        return mImageView;
    }

    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getImgPath() {
        return imgPath;
    }

    public int get_id(){
        return _id;
    }
    public void setImgPath(int imgPath) {
        this._id=imgPath;
        this.imgPath = view.getResources().getIdentifier("recipe" + _id, "drawable", "com.example.test");

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
