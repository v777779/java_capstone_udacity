package ru.vpcb.footballassistant.glide;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import ru.vpcb.footballassistant.R;

/**
 * Module for the SVG sample app.
 */
@GlideModule
public class SvgModule extends AppGlideModule {
    /**
     * Setup connection options for OkHttp Client
     *
     * @param context  Context of calling activity
     * @param glide    Glide object
     * @param registry Registry object
     */
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
        registry.replace(GlideUrl.class, InputStream.class, factory);

        registry.register(SVG.class, PictureDrawable.class, new SvgDrawableTranscoder())
                .append(InputStream.class, SVG.class, new SvgDecoder());
    }

    /**
     * Setup download options for Glide client
     *
     * @param context Context of calling activity
     * @param builder GlideBuilder builder for Glide client
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDefaultRequestOptions(new RequestOptions()
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .format(DecodeFormat.PREFER_ARGB_8888)
        );

        builder.setLogLevel(Log.ERROR);  // default Log.DEBUG
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
