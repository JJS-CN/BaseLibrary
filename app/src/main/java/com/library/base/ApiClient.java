package com.library.base;

import com.simplelibrary.http.BaseClient;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public class ApiClient extends BaseClient<ApiService> {
    private volatile static ApiClient sInstance;

    public static ApiService getApi() {
        if (sInstance == null) {
            synchronized (ApiClient.class) {
                if (sInstance == null) {
                    sInstance = new ApiClient();
                }
            }
        }
        return sInstance.create(ApiService.class);
    }

}
