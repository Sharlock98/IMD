package com.example.limingyan.imd.util;




import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.example.limingyan.imd.MyApplication;

public class HttpRequest {
    public static void getBitmap(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        ImageRequest request=new ImageRequest(url,listener,150,170, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,errorListener);
        MyApplication.queue.add(request);
    }
}
