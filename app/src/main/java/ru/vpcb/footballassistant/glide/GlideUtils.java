package ru.vpcb.footballassistant.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.Map;

import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.utils.Config;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 17-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */

public class GlideUtils {


    public static void setTeamImage( String imageURL,ImageView imageView,
                                    RequestBuilder<PictureDrawable> requestBuilder,
                                    RequestBuilder<Drawable> requestBuilderCommon,
                                     int resourceId) {

        if (imageURL == null || imageURL.isEmpty()) {
            imageView.setImageResource(resourceId);
            return;
        }

        imageURL = Config.imageCheckReplaceURL(imageURL);  // address replacement for known addresses
        if (imageURL.toLowerCase().endsWith("svg")) {
            requestBuilder.load(imageURL).into(imageView);
        } else {
            requestBuilderCommon.load(imageURL).into(imageView);
        }
    }



    public static void setTeamImage(int id, ImageView imageView, Map<Integer, FDTeam> map,
                                    RequestBuilder<PictureDrawable> requestBuilder,
                                    RequestBuilder<Drawable> requestBuilderCommon) {
        if (map == null || id <= 0) return;
        if (imageView == null) return;

        FDTeam team = map.get(id);
        if(team == null) {
            imageView.setImageResource(R.drawable.fc_logo);
            return;
        }

        String imageURL = team.getCrestURL();
        if (imageURL == null || imageURL.isEmpty()) {
            imageView.setImageResource(R.drawable.fc_logo);
            return;
        }

        imageURL = Config.imageCheckReplaceURL(imageURL);  // address replacement for known addresses
        if (imageURL.toLowerCase().endsWith("svg")) {
            requestBuilder.load(imageURL).into(imageView);

        } else {
            requestBuilderCommon.load(imageURL).into(imageView);
        }
    }


    private void loadStandard(Context context, String imageURL, ImageView imageView) {
        if (imageURL == null || imageURL.isEmpty()) return;

        Glide.with(context)
                .load(imageURL)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.fc_logo_loading)
                        .error(R.drawable.fc_logo)
                )
                .into(imageView);
    }


    // glide
    public static RequestBuilder<PictureDrawable> getRequestBuilderSvg(Context context) {
        return Glide.with(context)
                .as(PictureDrawable.class)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.fc_logo_loading)
                        .error(R.drawable.fc_logo)
                )
                .listener(new SvgSoftwareLayerSetter());
    }

    public static RequestBuilder<Drawable> getRequestBuilderPng(Context context) {
        return Glide.with(context)
                .as(Drawable.class)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.fc_logo_loading)
                        .error(R.drawable.fc_logo)
                )
                .listener(new CommonRequestListener());
    }

    public static RequestBuilder<PictureDrawable> getRequestBuilderSvg(Context context, int id) {
        return Glide.with(context)
                .as(PictureDrawable.class)
                .apply(new RequestOptions()
                        .placeholder(id)
                        .error(id)
                )
                .listener(new SvgSoftwareLayerSetter());
    }

    public static RequestBuilder<Drawable> getRequestBuilderPng(Context context, int id) {
        return Glide.with(context)
                .as(Drawable.class)
                .apply(new RequestOptions()
                        .placeholder(id)
                        .error(id)
                )
                .listener(new CommonRequestListener());
    }


    private static class CommonRequestListener implements RequestListener<Drawable> {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    }
}
