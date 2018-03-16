package ru.vpcb.footballassistant;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 02-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */

import android.view.View;

import java.util.Calendar;
import java.util.Date;

import ru.vpcb.footballassistant.data.FDFixture;

/**
 *  ICallback callback common user interface.
 */
public interface ICallback {
    /**
     *  Performs callback processing for RecyclerView
     *  in MainActivity
     */
    void onComplete(View view, int value);
    /**
     *  Performs callback processing for FragmentError
     *  in DetailActivity
     */
    @SuppressWarnings("SameParameterValue")
    void onComplete(int mode, Calendar calendar);

    void onComplete(View view, String link, String title);

}
