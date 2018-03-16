package ru.vpcb.footballassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;

import static ru.vpcb.footballassistant.utils.Config.BUNDLE_CALENDAR_DATA;
import static ru.vpcb.footballassistant.utils.Config.CALENDAR_DIALOG_ACTION_APPLY;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;


/**
 * FragmentError is Dialog Fragment class
 * to show dialog when there is no connection to network
 */
public class CalendarDialog extends DialogFragment implements View.OnClickListener {


    private TextView mTextViewYear;
    private TextView mTextViewDate;
    private Calendar mCalendar;
    private ICallback mCallback;

    public static Fragment newInstance(Calendar calendar) {
        CalendarDialog fragment = new CalendarDialog();
        fragment.setStyle(R.style.Dialog_Title, R.style.Calendar_Dialog);
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_CALENDAR_DATA, calendar.getTimeInMillis());
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Constructor default
     * Sets default layout for MainActivity
     */
    public CalendarDialog() {

    }

    /**
     * Creates FragmentError main View
     * Setup different layouts using mLayoutId field
     * Setup different title for MainActivity and DetailsActivity
     *
     * @param inflater           LayoutInflater inflates layout
     * @param container          ViewGroup parent view
     * @param savedInstanceState Bundle savedInstanceState
     * @return View of dialog
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        v.findViewById(R.id.btn_ok).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);
        getDialog().setTitle(getString(R.string.calendar_title));


        CalendarView mCalendarView = v.findViewById(R.id.calendar_view);
        mTextViewYear = v.findViewById(R.id.calendar_year);
        mTextViewDate = v.findViewById(R.id.calendar_date);
        mCalendar = Calendar.getInstance();
        long time = EMPTY_INT_VALUE;
        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                time = bundle.getLong(BUNDLE_CALENDAR_DATA, EMPTY_INT_VALUE);
            }
        } else {
            time = savedInstanceState.getLong(BUNDLE_CALENDAR_DATA, EMPTY_INT_VALUE);
        }
        if (time != EMPTY_INT_VALUE) {
            mCalendar.setTimeInMillis(time);
        }

        mCallback = (ICallback)getActivity();
        mCalendarView.setDate(mCalendar.getTimeInMillis());
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mCalendar.set(year, month, dayOfMonth);
                setCalendarHead();
            }
        });

        setCalendarHead();
        getDialog().setCanceledOnTouchOutside(true);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(BUNDLE_CALENDAR_DATA, mCalendar.getTimeInMillis());
    }

    /**
     * OnClick method, recreate MainActivity on RETRY or finish() any activity on EXIT.
     *
     * @param v View object of button
     */
    public void onClick(View v) {
        String s = ((Button) v).getText().toString();

        if(mCallback == null) return;

        if (s.equals(getString(android.R.string.ok))) {
           mCallback.onComplete(CALENDAR_DIALOG_ACTION_APPLY, mCalendar);
        }
        dismiss();
    }

       private void setCalendarHead() {
        mTextViewYear.setText(getString(R.string.calendar_text_year, mCalendar.get(Calendar.YEAR)));

        mTextViewDate.setText(getString(R.string.calendar_text_date,
                mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH),
                mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH),
                mCalendar.get(Calendar.DAY_OF_MONTH)));
    }
}
