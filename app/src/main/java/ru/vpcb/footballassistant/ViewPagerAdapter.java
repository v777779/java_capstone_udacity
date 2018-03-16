package ru.vpcb.footballassistant;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.utils.Config;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_FIXTURE_DATE;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 29-Sep-17
 * Email: vadim.v.voronov@gmail.com
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> recyclers;
    private List<String> titles;

    /**
     * Constructor  created object from List<View>> data source
     */
    public ViewPagerAdapter() {
        this.recyclers = null;
        this.titles = null;
    }

    @Override
    public int getItemPosition(Object object) {  // notificationDataSetChange() update
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = recyclers.get(position);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (recyclers == null) return 0;
        return recyclers.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null || position < 0 || position >= titles.size())
            return EMPTY_FIXTURE_DATE;
        String title = titles.get(position);
        if (title == null) title = EMPTY_FIXTURE_DATE;
        return title;
    }

    public void swap(List<View> recyclers, List<String> titles) {
        if (recyclers == null || recyclers.isEmpty() || titles == null || titles.isEmpty()) return;

        this.recyclers = recyclers;
        this.titles = titles;
        notifyDataSetChanged();
    }

}
