package com.example.test;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;


class PhotoUpWin extends PopupWindow {

    private View view;


    public PhotoUpWin(Context mContext, View.OnClickListener popListener) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_pic, null);

        this.setOutsideTouchable(true);

        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {

                        dismiss();
                    }
                }
                return true;
            }
        });


        this.setContentView(this.view);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        this.setAnimationStyle(R.style.photo_anim);
        getContentView().findViewById(R.id.btn_take_photo).setOnClickListener(popListener);
        getContentView().findViewById(R.id.btn_pick_photo).setOnClickListener(popListener);
        getContentView().findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}


public class tab_Me extends AppCompatActivity implements View.OnClickListener {
    public TextView nickname;
    public TableRow state,passwordreset,myreport,myfavourite,shoppingcart;

    public ImageView userpic;

    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;

    private PhotoUpWin photoWin;
    private String account_id;
    private SharedPreferences sp;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_me_layout);
        sp=getSharedPreferences("config", Context.MODE_PRIVATE);

        account_id=sp.getString("account_id","");

        nickname = (TextView) findViewById(R.id.tv_me_1);

        state=(TableRow)findViewById(R.id.tr_me_1);
        userpic = (ImageView) findViewById(R.id.ib_me_pic);
        passwordreset = (TableRow) findViewById(R.id.tr_me_2);
        myreport = (TableRow) findViewById(R.id.tr_me_3);
        myfavourite = (TableRow) findViewById(R.id.tr_me_4);
        shoppingcart = (TableRow) findViewById(R.id.tr_me_5);

    }

    public void initView() {
        if (!account_id.equals("")) {
            nickname.setText("Log Out");

            state.setOnClickListener(this);
            userpic.setOnClickListener(this);
            passwordreset.setOnClickListener(this);
            myreport.setOnClickListener(this);
            myfavourite.setOnClickListener(this);
            shoppingcart.setOnClickListener(this);


        } else {
            nickname.setText("Log In");
            nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(tab_Me.this, AccountInit.class);
                    startActivity(it);
                }
            });
        }



    }
    @Override
    protected void onResume(){
        super.onResume();
        initView();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_me_pic:
                userpic();
                break;

            case R.id.tr_me_1:
                logOut();
                break;
            case R.id.tr_me_2:
                setPasswordreset();
                break;
            case R.id.tr_me_3:
                myreport();
                break;
            case R.id.tr_me_4:
                myfavourite();
                break;
            case R.id.tr_me_5:
                shoppingcart();
                break;
            case R.id.btn_take_photo:
                getPicFromCamera();
                photoWin.dismiss();
                break;

            case R.id.btn_pick_photo:
                getPicFromPhoto();
                photoWin.dismiss();
                break;


        }
    }

    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    private void getPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "test.jpg")));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功
                        File file = new File(Environment.getExternalStorageDirectory()
                                + "/test.jpg");
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());

                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Log.w("test", "data");
                        Bitmap photo = extras.getParcelable("data");
                        userpic.setImageBitmap(photo);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void photoClip(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    public void userpic() {
        photoWin = new PhotoUpWin(tab_Me.this, this);
        photoWin.showAtLocation(tab_Me.this.findViewById(R.id.ll_me), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    private void myreport() {
        //进入 myreport
        Intent intent = new Intent();
        intent.setClass(this, myReport.class);
        startActivity(intent);


    }

    private void myfavourite() {
        //进入 myfavourite
        Intent intent = new Intent();
        intent.setClass(this, myFavourite.class);
        startActivity(intent);

    }

    private void shoppingcart() {
        //进入 shoppingcart
        Intent intent = new Intent();
        intent.setClass(this, shoppingCart.class);
        startActivity(intent);

    }

    private void setPasswordreset() {

        Intent intent = new Intent(this, ResetPassword.class);
        intent.putExtra("account_id", account_id);
        startActivity(intent);
    }

    private void logOut(){


        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Intent intent=new Intent(this,AccountInit.class);

        finish();
        startActivity(intent);
    }


}
