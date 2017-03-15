package com.ziv.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class DialogUtil extends ProgressDialog {  
  
    public DialogUtil(Context context, int theme) {  
        super(context, theme);  
    }  
  
    public DialogUtil(Context context) {  
        super(context);  
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.dialog_style);  
        setScreenBrightness();  
        this.setOnShowListener(new OnShowListener(){  
                @Override  
                public void onShow(DialogInterface dialog) {  
//                    ImageView image = (ImageView) findViewById(R.id.loading_img);  
//                    Animation anim =AnimationUtils.loadAnimation(getContext(), R.drawable.frame_loading);
//                    Animation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);  
//                    anim.setRepeatCount(Animation.INFINITE); 
//                    anim.setDuration(10000);                  
//                    anim.setInterpolator(new LinearInterpolator()); 
//                    image.startAnimation(anim);  
                }  
            });  
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }  
  
    private void setScreenBrightness() {  
        Window window = getWindow();  
        WindowManager.LayoutParams layoutParams = window.getAttributes();   
        layoutParams.dimAmount = 0;  
        window.setAttributes(layoutParams);  
    }  
      
}  