package com.innovative.shortsvideo;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.vaibhavpandey.katora.contracts.MutableContainer;
import com.vaibhavpandey.katora.contracts.Provider;

public class ExoPlayerProvider implements Provider {
    final Context mContext;

    public ExoPlayerProvider(Context context) {
        mContext = context;
    }

    @Override
    public void provide(MutableContainer container) {
        container.singleton(
                HttpProxyCacheServer.class,
                c -> new HttpProxyCacheServer.Builder(mContext)
                        .build()
        );
    }
}

