package com.huang.stuabc.view;
 
import com.huang.stuabc.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
public class CustomProgressDialog extends Dialog {
    private static CustomProgressDialog customProgressDialog = null;
    public CustomProgressDialog(Context context){
        super(context);
    }
     
    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
     
    public static CustomProgressDialog createDialog(Context context){
        customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.customprogressdialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
         
        return customProgressDialog;
    }
  
    public void onWindowFocusChanged(boolean hasFocus){
         
        if (customProgressDialog == null){
            return;
        }
         
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
                animationDrawable.start();

    }
    public CustomProgressDialog setTitile(String strTitle){
        return customProgressDialog;
    }
     
}
