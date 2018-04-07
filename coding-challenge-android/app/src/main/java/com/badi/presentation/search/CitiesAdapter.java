package com.badi.presentation.search;

import android.content.Context;
import android.support.annotation.StyleableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.data.entity.search.City;
import com.badi.data.entity.search.Country;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link City} in the search.
 */
public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityHolder> {

    private Context context;
    private List<City> cityList;
    private OnCityListener onCityListener;

    interface OnCityListener {
        void onUserItemClicked(View cityImage, City city);
    }

    CitiesAdapter(Context context, List<City> cityList, OnCityListener onCityListener) {
        this.context = context;
        this.cityList = cityList;
        this.onCityListener = onCityListener;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        // Show country details inside viewHolder
        holder.show(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList == null ? 0 : cityList.size();
    }

    public void setCities(List<City> cities) {
        cityList = cities;
        notifyDataSetChanged();
    }

    class CityHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_city) ImageView imageCity;

        @BindView(R.id.text_city_name) TextView textCityName;

        CityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(final City city) {
            textCityName.setText(city.name());

            if (city.imgSmall() > 0)
                GlideApp.with(context)
                        .load(city.imgSmall())
                        .transition(withCrossFade())
                        .into(imageCity);
        }

        @OnClick(R.id.image_city)
        void onCityClick() {
            if (getAdapterPosition() != -1)
                onCityListener.onUserItemClicked(imageCity, cityList.get(getAdapterPosition()));
        }
    }
}
