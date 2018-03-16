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

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.vpcb.footballassistant.data.FDCompetition;
import ru.vpcb.footballassistant.data.FDFixture;
import ru.vpcb.footballassistant.data.FDTeam;
import ru.vpcb.footballassistant.utils.Config;

import static ru.vpcb.footballassistant.glide.GlideUtils.getRequestBuilderPng;
import static ru.vpcb.footballassistant.glide.GlideUtils.getRequestBuilderSvg;
import static ru.vpcb.footballassistant.glide.GlideUtils.setTeamImage;
import static ru.vpcb.footballassistant.utils.Config.RM_HEAD_VIEW_TYPE;
import static ru.vpcb.footballassistant.utils.Config.RM_ITEM_VIEW_TYPE_DARK;
import static ru.vpcb.footballassistant.utils.Config.RT_HEAD_VIEW_TYPE;
import static ru.vpcb.footballassistant.utils.Config.RT_ITEM_VIEW_TYPE_DARK;
import static ru.vpcb.footballassistant.utils.Config.RT_ITEM_VIEW_TYPE_LIGHT;
import static ru.vpcb.footballassistant.utils.FDUtils.formatMatchDate;
import static ru.vpcb.footballassistant.utils.FDUtils.formatMatchScore;

/**
 * RecyclerView Adapter class
 * Used to create and show Item objects of RecyclerView
 */
public class RecyclerMatchAdapter extends RecyclerView.Adapter<RecyclerMatchAdapter.ViewHolder> {
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
    // test!!!
    private static int counter = 0;

    private List<FDFixture> mList;
    private Map<Integer, FDCompetition> mMap;
    private Map<Integer, FDTeam> mMapTeams;
    private RequestBuilder<PictureDrawable> mRequestSvg;
    private RequestBuilder<Drawable> mRequestPng;


    /**
     * Constructor of RecyclerAdapter
     *
     * @param context Context of calling activity
     */
    public RecyclerMatchAdapter(Context context, List<FDFixture> list, Map<Integer, FDTeam> mapTeams) {
        mContext = context;
        /*
      Resources of activity
     */
        Resources mRes = context.getResources();
        mList = list;
        mMapTeams = mapTeams;
        /*
      Boolean is true for tablet with sw800dp
     */
        boolean mIsWide = mRes.getBoolean(R.bool.is_wide);
        /*
      Boolean is true for landscape layout
     */
        boolean mIsLand = mRes.getBoolean(R.bool.is_land);
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
        if (position == 0) return RT_HEAD_VIEW_TYPE;
        else return position % 2 == 0 ? RT_ITEM_VIEW_TYPE_DARK : RT_ITEM_VIEW_TYPE_LIGHT;
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
        int layoutId = R.layout.match_recycler_item;
        if (viewType == RM_HEAD_VIEW_TYPE) layoutId = R.layout.match_recycler_head;
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
                ((ICallback) mContext).onComplete(view, pos);
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
        return mList.size() + 1;
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
        @BindView(R.id.image_sm_team_home)
        ImageView mImageHome;
        @Nullable
        @BindView(R.id.image_sm_team_away)
        ImageView mImageAway;
        @Nullable
        @BindView(R.id.text_sm_item_home)
        TextView mTextTeamHome;
        @Nullable
        @BindView(R.id.text_sm_item_away)
        TextView mTextTeamAway;
        @Nullable
        @BindView(R.id.text_sm_item_score)
        TextView mTextScore;
        @Nullable
        @BindView(R.id.text_sm_item_league)
        TextView mTextLeague;
        @Nullable
        @BindView(R.id.text_sm_item_date)
        TextView mTextDate;
        @Nullable
        @BindView(R.id.text_sm_item_status)
        TextView mTextStatus;
        @Nullable
        @BindView(R.id.icon_sm_item_favorite)
        ImageView mImageFavorite;
        @Nullable
        @BindView(R.id.icon_sm_item_notify)
        ImageView mImageNotify;
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
            mColorDark = ContextCompat.getColor(mContext, R.color.match_recycler_card_back_dark);
        }

        /**
         * Fills TextViews with the data from source data object
         * Loads image with the Glide loader to ImageViews.
         *
         * @param position int position of item in RecyclerView
         */
        private void fill(int position) {
            if (mList == null || position < 0 || position > mList.size()) return;
            if (getItemViewType() == RM_HEAD_VIEW_TYPE) {
                return;
            }


            if (getItemViewType() == RM_ITEM_VIEW_TYPE_DARK && mLayout != null) {
                mLayout.setBackgroundColor(mColorDark);

            }
            FDFixture fixture = mList.get(position - 1);  // 1..n
            if (fixture == null || fixture.getId() <= 0) return;

            setText(mTextTeamHome, fixture.getHomeTeamName());
            setText(mTextTeamAway, fixture.getAwayTeamName());
            setText(mTextScore, formatMatchScore(fixture.getGoalsHome(), fixture.getGoalsAway()));
            setText(mTextDate, formatMatchDate(fixture.getDate()));
            setText(mTextStatus, fixture.getStatus());

            setTeamImage(fixture.getHomeTeamId(), mImageHome, mMapTeams, mRequestSvg, mRequestPng);
            setTeamImage(fixture.getAwayTeamId(), mImageAway, mMapTeams, mRequestSvg, mRequestPng);

            if (fixture.isFavorite() && mImageFavorite != null) {
                mImageFavorite.setImageResource(R.drawable.ic_star);
            }
            if (fixture.isNotified()) {
                mImageNotify.setImageResource(R.drawable.ic_notifications);
            }

        }

        private void setText(TextView textView, String s) {
            if (textView == null) return;
            textView.setText(s);
        }


    }


}