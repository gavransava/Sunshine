package com.example.savagavran.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savagavran.sunshine.data.WeatherRealmObject;

import java.util.ArrayList;


public class ForecastAdapter extends ArrayAdapter<WeatherRealmObject> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private boolean mUseTodayLayout = true;
    ArrayList<WeatherRealmObject> mData;

    public void setAdapterData(ArrayList<WeatherRealmObject> data) {
        mData = data;
    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }

    public ForecastAdapter(Context context, ArrayList<WeatherRealmObject> data) {
        super(context, 0, data);
        mData = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(position);
        int layoutId = -1;
        boolean isArt = false;

        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_today;
                isArt= true;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_forecast;
                isArt = false;
                break;
            }
        }

        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        WeatherRealmObject data = mData.get(position);
        view.setTag(viewHolder);
        int weatherImageResource = Integer.parseInt(data.getId());

        if(isArt) {
            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
                    weatherImageResource));
        } else {
            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
                    weatherImageResource));
        }

        viewHolder.descriptionView.setText(data.getDescription());
        viewHolder.dateView.setText(Utility.getFriendlyDayString(getContext(),Long.parseLong(data.getDateTime())));
        viewHolder.iconView.setContentDescription(data.getDescription());
        viewHolder.highTempView.setText(data.getMax());
        viewHolder.lowTempView.setText(data.getMin());

        return view;
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
