/*
 * File: GalleryActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.HackyViewPager;
import com.badi.presentation.base.BaseActivity;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GalleryActivity extends BaseActivity {

    public static final String EXTRA_IMAGES = "GalleryActivity.EXTRA_IMAGES";
    public static final String EXTRA_INDEX_IMAGE_PAGER = "GalleryActivity.EXTRA_INDEX_IMAGE_PAGER";

    private ArrayList<String> images;

    @BindView(R.id.pager) HackyViewPager imagesPager;
    @BindView(R.id.thumbnails) LinearLayout thumbnailsLayout;

    /**
     * Return an Intent to start this Activity.
     * images is an list of url of the room pictures to load.
     * currentImageIndex is the index of the current image seen in the viewpager pictures adapter in the room detail.
     */
    public static Intent getCallingIntent(Activity activity, ArrayList<String> images, int currentImageIndex) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGES, images);
        intent.putExtra(EXTRA_INDEX_IMAGE_PAGER, currentImageIndex);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        images = getIntent().getStringArrayListExtra(EXTRA_IMAGES);

        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(this);
        imagesPager.setAdapter(galleryPagerAdapter);
        imagesPager.setOffscreenPageLimit(6); // how many images to load into memory
        imagesPager.setCurrentItem(getIntent().getIntExtra(EXTRA_INDEX_IMAGE_PAGER, 0));
    }

    @OnClick(R.id.button_close)
    public void onCloseButtonClick() {
        supportFinishAfterTransition();
    }

    class GalleryPagerAdapter extends PagerAdapter {

        Context context;
        LayoutInflater inflater;

        public GalleryPagerAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = inflater.inflate(R.layout.item_pager_gallery, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = thumbnailsLayout.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams) imagesPager.getLayoutParams()).bottomMargin - (borderSize * 2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);

            final ImageView thumbView = new ImageView(context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.d("Thumbnail clicked");

                    // Set the pager position when thumbnail clicked
                    imagesPager.setCurrentItem(position);
                }
            });
            thumbnailsLayout.addView(thumbView);

            final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) itemView.findViewById(R.id.image_gallery);

            // Asynchronously load the image and set the thumbnail and pager view
            GlideApp.with(context)
                    .asBitmap()
                    .load(images.get(position))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImage(ImageSource.bitmap(resource));
                            thumbView.setImageBitmap(resource);
                        }
                    });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
