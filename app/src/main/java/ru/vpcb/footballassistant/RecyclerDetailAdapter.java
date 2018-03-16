package ru.vpcb.footballassistant;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.RequestBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.utils.Config;

import static ru.vpcb.footballassistant.glide.GlideUtils.getRequestBuilderPng;
import static ru.vpcb.footballassistant.glide.GlideUtils.getRequestBuilderSvg;
import static ru.vpcb.footballassistant.glide.GlideUtils.setTeamImage;
import static ru.vpcb.footballassistant.utils.Config.RM_HEAD_VIEW_TYPE;
import static ru.vpcb.footballassistant.utils.Config.RM_ITEM_VIEW_TYPE_DARK;
import static ru.vpcb.footballassistant.utils.Config.RM_ITEM_VIEW_TYPE_LIGHT;
import static ru.vpcb.footballassistant.utils.FDUtils.formatMatchDate;
import static ru.vpcb.footballassistant.utils.FDUtils.formatMatchTime;

/**
 * RecyclerView Adapter class
 * Used to create and show Item objects of RecyclerView
 */
public class RecyclerDetailAdapter extends RecyclerView.Adapter<RecyclerDetailAdapter.ViewHolder> {
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

    private List<FDFixture> mList;
    private Map<Integer, FDTeam> mMap;
    private RequestBuilder<PictureDrawable> mRequestSvg;
    private RequestBuilder<Drawable> mRequestPng;

    /**
     * Constructor of RecyclerAdapter
     *
     * @param context Context of calling activity
     */
    public RecyclerDetailAdapter(Context context, List<FDFixture> list, Map<Integer, FDTeam> map) {
        mContext = context;

        /*
      Resources of activity
     */
        Resources mRes = context.getResources();
        mList = list;
        mMap = map;

        /*
      Boolean is true for tablet with sw800dp
     */
        boolean mIsWide = mRes.getBoolean(R.bool.is_wide);
        /*
      Boolean is true for landscape layout
     */
        boolean mIsLand = mRes.getBoolean(R.bool.is_land);
        DateFormat mDateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
//        if(mRequestSvg == null) mRequestSvg = getRequestBuilderSvg(context);
//        if(mRequestPng == null ) mRequestPng = getRequestBuilderPng(context);

        mRequestSvg = getRequestBuilderSvg(context);
        mRequestPng = getRequestBuilderPng(context);

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
        return (position % 2 == 0) ? RM_ITEM_VIEW_TYPE_LIGHT : RM_ITEM_VIEW_TYPE_DARK;
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
        int layoutId;
        if (viewType == RM_HEAD_VIEW_TYPE) {
            layoutId = R.layout.detail_recycler_head;
        } else {
            layoutId = R.layout.detail_recycler_item;
        }


        View view = ((AppCompatActivity) mContext).getLayoutInflater()
                .inflate(R.layout.detail_recycler_item, parent, false);

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
                if (mList == null || pos < 0 || pos >= mList.size()) {
                    return;
                }
                FDFixture fixture = mList.get(pos);
                if (fixture == null || fixture.getId() <= 0) {
                    return;
                }
                ((ICallback) mContext).onComplete(view, fixture.getId());
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
    public void swap(List<FDFixture> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class of RecyclerView Item
     * Used to hold text and image resources of Item of RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.text_tm_item_home)
        TextView mTextTeamHome;
        @Nullable
        @BindView(R.id.text_tm_item_away)
        TextView mTextTeamAway;
        @Nullable
        @BindView(R.id.text_tm_item_time)
        TextView mTextTime;
        @Nullable
        @BindView(R.id.text_rm_head_league)
        TextView mTextLeague;
        @Nullable
        @BindView(R.id.image_rm_item_home)
        ImageView mImageHome;
        @Nullable
        @BindView(R.id.image_rm_item_away)
        ImageView mImageAway;
        @Nullable
        @BindView(R.id.image_rm_head_league)
        ImageView mImageLeague;
        @Nullable
        @BindView(R.id.text_rm_item_date)
        TextView mTextDate;
        @Nullable
        @BindView(R.id.icon_rm_item_favorite)
        ImageView mImageFavorite;
        @Nullable
        @BindView(R.id.icon_rm_item_notify)
        ImageView mImageNotify;
        @Nullable
        @BindView(R.id.text_rm_item_status)
        TextView mTextStatus;
        @Nullable
        @BindView(R.id.constraint_recycler_match_item)
        View mLayout;

        private int mColorDark;

        /**
         * Constructor
         * Binds all views with the ButterKnife object.
         *
         * @param view View of parent
         */
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mColorDark = ContextCompat.getColor(mContext, R.color.rm_item_card_back_dark);
        }

        /**
         * Fills TextViews with the data from source data object
         * Loads image with the Glide loader to ImageViews.
         *
         * @param position int position of item in RecyclerView
         */
        private void fill(int position) {
            if (mList == null) return;

            FDFixture fixture = mList.get(position);

//            if (getItemViewType() == RM_HEAD_VIEW_TYPE) {
//                if (position == 0) {
//                setText( mTextLeague,mContext.getString(R.string.text_test_rm_item_favorites));
//                    mImageLeague.setImageResource(R.drawable.ic_star);
//                } else {
//                setText(mTextLeague,mContext.getString(R.string.text_test_rm_item_league2));
//                    mImageLeague.setImageResource(R.drawable.icon_ball);
//                }
//            }

//            if (getItemViewType() == RM_ITEM_VIEW_TYPE_DARK && mLayout != null) {
//                mLayout.setBackgroundColor(mColorDark);
//
//            }
            setText(mTextTeamHome, fixture.getHomeTeamName());
            setText(mTextTeamAway, fixture.getAwayTeamName());
            setText(mTextTime, formatMatchTime(fixture.getDate()));
            setText(mTextDate, formatMatchDate(fixture.getDate()));
            setText(mTextStatus, fixture.getStatus());
            setTeamImage(fixture.getHomeTeamId(),
                    mImageHome, mMap, mRequestSvg, mRequestPng);
            setTeamImage(fixture.getAwayTeamId(),
                    mImageAway, mMap, mRequestSvg, mRequestPng);

            if(fixture.isFavorite()) {
                mImageFavorite.setImageResource(R.drawable.ic_star);
            }else{
                mImageFavorite.setImageResource(R.drawable.ic_star_border);
            }
            if(fixture.isNotified()) {
                mImageNotify.setImageResource(R.drawable.ic_notifications);
            }else{
                mImageNotify.setImageResource(R.drawable.ic_notifications_none);
            }


        }

        private void setText(TextView textView, String s) {
            if (textView == null || s == null || s.isEmpty()) return;
            textView.setText(s);
        }

    }
//    mTextTeamHome = view.findViewById(R.id.text_tm_item_home);
//    mTextTeamAway= view.findViewById(R.id.text_tm_item_away);
//    mTextTime= view.findViewById(R.id.text_tm_item_time);
//    mTextLeague= view.findViewById(R.id.text_rm_head_league);
//    mImageHome= view.findViewById(R.id.image_rm_item_home);
//    mImageAway= view.findViewById(R.id.image_rm_item_away);
//    mImageLeague= view.findViewById(R.id.image_rm_head_league);
//    mTextDate= view.findViewById(R.id.text_rm_item_date);
//    mImageFavorite= view.findViewById(R.id.icon_rm_item_favorite);
//    mImageNotify= view.findViewById(R.id.icon_rm_item_notify);
//    mTextStatus= view.findViewById(R.id.text_rm_item_status);
//    mLayout = view.findViewById(R.id.constraint_recycler_match_item);

}