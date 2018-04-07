package com.badi.presentation.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.ViewUtil;
import com.badi.data.entity.search.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link City} in the search.
 */
public class SuggestedCitiesAdapter extends RecyclerView.Adapter<SuggestedCitiesAdapter.CityHolder> {

    private Context context;
    private List<City> cityList;
    private CitiesAdapter.OnCityListener onCityListener;
    private int defaultMargin;
    private int doubleMargin;

    SuggestedCitiesAdapter(Context context, CitiesAdapter.OnCityListener cityClickListener) {
        this.context = context;
        this.onCityListener = cityClickListener ;
        setupCitiesList();

        defaultMargin = (int) context.getResources().getDimension(R.dimen.recycler_view_margin_default);
        doubleMargin = (int) context.getResources().getDimension(R.dimen.recycler_view_margin_double);
    }

    private void setupCitiesList() {
        cityList = new ArrayList<>();

        TypedArray suggestedCitiesTypedArray = context.getResources().obtainTypedArray(R.array.suggested_cities);

        for (int cityCount = 0; cityCount < suggestedCitiesTypedArray.length(); cityCount++) {
            TypedArray city = context.getResources().obtainTypedArray(suggestedCitiesTypedArray.getResourceId(cityCount, 0));

            cityList.add(getCityBuild(city));

            city.recycle();
        }

        suggestedCitiesTypedArray.recycle();
    }

    private City getCityBuild(TypedArray city) {
        return City.builder()
                .setId(city.getString(City.ID_INDEX))
                .setName(city.getString(City.NAME_INDEX))
                .setAddress(city.getString(City.ADDRESS_INDEX))
                .setImgSmall(city.getResourceId(City.CITY_IMG_SMALL_INDEX, 0))
                .setImgLarge(city.getResourceId(City.CITY_IMG_LARGE_INDEX, 0))
                .build();
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_large, parent, false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        // Show country details inside viewHolder
        holder.show(cityList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return cityList == null ? 0 : cityList.size();
    }

    class CityHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_city) ImageView imageCity;
        @BindView(R.id.item_city_layout) CardView itemCityLayout;
        @BindView(R.id.text_city_name) TextView textCityName;

        CityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(final City city, int position) {
            textCityName.setText(city.name());

            if (city.imgLarge() > 0)
                GlideApp.with(context)
                        .load(city.imgLarge())
                        .transition(withCrossFade())
                        .into(imageCity);

            setMargin(position);
        }

        private void setMargin(int position) {
            if (position == 0) {
                ViewUtil.changeViewMargin(doubleMargin, defaultMargin, defaultMargin, defaultMargin, itemCityLayout);
            } else if (position == getItemCount() - 1) {
                ViewUtil.changeViewMargin(defaultMargin, defaultMargin, doubleMargin, defaultMargin, itemCityLayout);
            } else {
                ViewUtil.changeViewMargin(defaultMargin, itemCityLayout);
            }
        }

        @OnClick(R.id.image_city)
        void onCityClick() {
            if (getAdapterPosition() != -1)
                onCityListener.onUserItemClicked(imageCity, cityList.get(getAdapterPosition()));
        }
    }
}
