package ru.vpcb.footballassistant;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import ru.vpcb.footballassistant.glide.GlideUtils;
import ru.vpcb.footballassistant.news.NDArticle;
import ru.vpcb.footballassistant.utils.Config;
import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.glide.GlideUtils.getRequestBuilderPng;
import static ru.vpcb.footballassistant.glide.GlideUtils.getRequestBuilderSvg;
import static ru.vpcb.footballassistant.utils.Config.DATE_FULL_PATTERN;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_STRING;
import static ru.vpcb.footballassistant.utils.Config.RM_ITEM_VIEW_TYPE_LIGHT;


/**
 * RecyclerView Adapter class
 * Used to create and show Item objects of RecyclerView
 */
public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerNewsAdapter.ViewHolder> {
    /**
     * Cursor object source of data
     */
    private Cursor mCursor;
    /**
     * Context  context of calling activity
     */
    private Context mContext;
    /**
     * Span object used for RecyclerView as storage of display item parameters
     */
    private Config.Span mSpan;

    private List<NDArticle> mList;
    private RequestBuilder<PictureDrawable> mRequestSvg;
    private RequestBuilder<Drawable> mRequestPng;
    private SimpleDateFormat mDateFormat;

    /**
     * Constructor of RecyclerAdapter
     *
     * @param context Context of calling activity
     */

    public RecyclerNewsAdapter(Context context, List<NDArticle> list) {
        mContext = context;
        /*
      Resources of activity
     */
        Resources mRes = context.getResources();
        mList = list;

        /*
      Boolean is true for tablet with sw800dp
     */
        boolean mIsWide = mRes.getBoolean(R.bool.is_wide);
        /*
      Boolean is true for landscape layout
     */
        boolean mIsLand = mRes.getBoolean(R.bool.is_land);
        mDateFormat = new SimpleDateFormat(DATE_FULL_PATTERN, Locale.ENGLISH);

        mRequestSvg = getRequestBuilderSvg(context, R.drawable.fc_logo_news);
        mRequestPng = getRequestBuilderPng(context, R.drawable.fc_logo_news);


    }

    /**
     * Returns itemID by position
     *
     * @param position in position of item
     * @return int itemID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns item viewType by position
     *
     * @param position in position of item
     * @return int item viewType
     */

    @Override
    public int getItemViewType(int position) {
// test  every 3rd is header
//        return (position % 3 == 0) ? RM_HEAD_VIEW_TYPE : RM_ITEM_VIEW_TYPE;
        return RM_ITEM_VIEW_TYPE_LIGHT;
    }

    /**
     * Creates ViewHolder of Item of RecyclerView
     * Sets width or height of item according to span and size of RecyclerView Container
     *
     * @param parent   ViewGroup parent of item
     * @param viewType int type of View of Item, unused in this application
     * @return ViewHolder of Item of RecyclerView
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.news_recycler_item;

        View view = ((AppCompatActivity) mContext).getLayoutInflater()
                .inflate(layoutId, parent, false);

//        view.getLayoutParams().height = mSpan.getHeight();
        return new ViewHolder(view);
    }


    /**
     * Fills ViewHolder Item with image and text from data source.
     * Sets onClickListener which calls  ICallback.onComplete(view, int) method in calling activity.
     *
     * @param holder   ViewHolder object which is filled
     * @param position int position of item in Cursor data source
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fill(position);
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = getLink(pos);
                String title = getTitle(pos);
                if (link == null) {
                    FDUtils.showMessage(mContext,
                            mContext.getString(R.string.news_no_data_message));
                    return;
                }

                ((ICallback) mContext).onComplete(view, link, title);
            }
        });

    }

    /**
     * Returns number of Items of Cursor mCursor data source
     *
     * @return int number of Items of Cursor mCursor data source
     */
    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    /**
     * Replaces mList with new List<FDFixture> object and
     * calls notifyDataSetChanged() method.
     *
     * @param list List<FDFixture> parameter.
     */
    public void swap(List<NDArticle> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class of RecyclerView Item
     * Used to hold text and image resources of Item of RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_news_item_poster)
        ImageView mImagePoster;

        @BindView(R.id.text_news_header)
        TextView mTextHead;

        @BindView(R.id.text_news_body)
        TextView mTextBody;

        @BindView(R.id.text_news_item_source)
        TextView mTextSource;

        @BindView(R.id.text_news_item_time)
        TextView mTextTime;

        /**
         * Constructor
         * Binds all views with the ButterKnife object.
         *
         * @param view View of parent
         */
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * Fills TextViews with the data from source data object
         * Loads image with the Glide loader to ImageViews.
         *
         * @param position int position of item in RecyclerView
         */
        private void fill(int position) {
            if (mList == null) return;

            NDArticle article = mList.get(position);
            if (article == null) return;

            String imageURL = article.getUrlToImage();
            GlideUtils.setTeamImage(imageURL, mImagePoster, mRequestSvg, mRequestPng, R.drawable.fc_logo_news);

            mTextHead.setText(article.getTitle());
            mTextBody.setText(article.getDescription());
            mTextSource.setText(article.getSource().getName());
            mTextTime.setText(getTimeAgo(article.getPublishedAt()));

        }


    }

    private String getTitle(int pos) {
        if (mList == null || pos < 0 || pos >= mList.size()) return EMPTY_STRING;
        NDArticle article = mList.get(pos);
        if (article == null) return EMPTY_STRING;
        String title = article.getTitle();
        if (title == null || title.isEmpty()) return EMPTY_STRING;
        return title;
    }

    private String getLink(int pos) {
        if (mList == null || pos < 0 || pos >= mList.size()) return null;
        NDArticle article = mList.get(pos);
        if (article == null) return null;
        String url = article.getUrl();
        if (url == null || url.isEmpty()) return null;
        return url;
    }


    private String getTimeAgo(String s) {
        String time = null;
        try {
            long currentTime = System.currentTimeMillis();
            long newsTime = mDateFormat.parse(s).getTime();
            long delta = TimeUnit.MILLISECONDS.toMinutes(currentTime - newsTime);
            if (delta < 60) {
                time = mContext.getString(R.string.text_news_item_time_min, delta);

            } else {
                delta = delta / 60;
                time = mContext.getString(R.string.text_news_item_time_hour, delta);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (time == null || time.isEmpty())
            time = mContext.getString(R.string.text_news_item_time_empty);
        return time;
    }


}