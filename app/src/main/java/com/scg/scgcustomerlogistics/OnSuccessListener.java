package com.scg.scgcustomerlogistics;

import okhttp3.ResponseBody;

public interface OnSuccessListener {
    void onSuccess(ResponseBody responseBody);
    void onComplete();

}

