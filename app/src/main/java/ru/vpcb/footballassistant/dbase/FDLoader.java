package ru.vpcb.footballassistant.dbase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;

import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_ID2;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_ID;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_DATA_URI;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_REQUEST;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_REQUEST_DATES;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_REQUEST_FIXTURES;
import static ru.vpcb.footballassistant.utils.Config.BUNDLE_LOADER_REQUEST_TEAMS;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 30-Jan-18
 * Email: vadim.v.voronov@gmail.com
 */
public class FDLoader extends CursorLoader {
    public FDLoader(Context context, Uri uri, String sortOrder) {
        super(context, uri, null, null, null, sortOrder);
    }

    public static FDLoader getInstance(Context context, int id, Bundle args) {
        Uri uri = FDProvider.buildLoaderIdUri(context, id);
        String sortOrder = FDProvider.buildLoaderIdSortOrder(context, id);

        if (args == null) {
            return new FDLoader(context, uri, sortOrder);
        }

        uri = args.getParcelable(BUNDLE_LOADER_DATA_URI);
        if (uri == null) {
            uri = FDProvider.buildLoaderIdUri(context, id);
        }
        return new FDLoader(context, uri, sortOrder);


    }


}
