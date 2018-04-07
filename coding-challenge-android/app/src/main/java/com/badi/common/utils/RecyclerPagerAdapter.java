/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.badi.common.utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerPagerAdapter<T extends RecyclerPagerAdapter.ViewHolder> extends PagerAdapter {

    public abstract int getItemCount();

    public abstract void onBindViewHolder(T holder, int position);

    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    private static final String STATE = RecyclerPagerAdapter.class.getSimpleName();

    private SparseArray<RecycleCache> recycleTypeCaches = new SparseArray<>();

    private SparseArray<Parcelable> savedStates = new SparseArray<>();

    public RecyclerPagerAdapter() {
    }

    @Override
    public void destroyItem(ViewGroup parent, int position, Object object) {
        if (object instanceof ViewHolder) {
            ((ViewHolder) object).detach(parent);
        }
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        int viewType = getItemViewType(position);
        if (recycleTypeCaches.get(viewType) == null) {
            recycleTypeCaches.put(viewType, new RecycleCache(this));
        }
        ViewHolder viewHolder = recycleTypeCaches.get(viewType).getFreeViewHolder(parent, viewType);
        viewHolder.attach(parent, position);
        onBindViewHolder((T) viewHolder, position);
        viewHolder.onRestoreInstanceState(savedStates.get(getItemId(position)));
        return viewHolder;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof ViewHolder && ((ViewHolder) object).itemView == view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (ViewHolder viewHolder : getAttachedViewHolders()) {
            onNotifyItemChanged(viewHolder);
        }
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            SparseArray<Parcelable> ss = bundle.containsKey(STATE) ? bundle.getSparseParcelableArray(STATE) : null;
            savedStates = ss != null ? ss : new SparseArray<Parcelable>();
        }
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        for (ViewHolder viewHolder : getAttachedViewHolders()) {
            savedStates.put(getItemId(viewHolder.position), viewHolder.onSaveInstanceState());
        }
        bundle.putSparseParcelableArray(STATE, savedStates);
        return bundle;
    }

    public int getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    protected void onNotifyItemChanged(ViewHolder viewHolder) {
    }

    private List<ViewHolder> getAttachedViewHolders() {
        List<ViewHolder> attachedViewHolders = new ArrayList<>();
        int n = recycleTypeCaches.size();
        for (int i = 0; i < n; i++) {
            for (ViewHolder viewHolder : recycleTypeCaches.get(recycleTypeCaches.keyAt(i)).caches) {
                if (viewHolder.isAttached) {
                    attachedViewHolders.add(viewHolder);
                }
            }
        }
        return attachedViewHolders;
    }

    private static class RecycleCache {

        private final RecyclerPagerAdapter adapter;

        private final List<ViewHolder> caches;

        RecycleCache(RecyclerPagerAdapter adapter) {
            this.adapter = adapter;
            caches = new ArrayList<>();
        }

        ViewHolder getFreeViewHolder(ViewGroup parent, int viewType) {
            int i = 0;
            ViewHolder viewHolder;
            for (int n = caches.size(); i < n; i++) {
                viewHolder = caches.get(i);
                if (!viewHolder.isAttached) {
                    return viewHolder;
                }
            }
            viewHolder = adapter.onCreateViewHolder(parent, viewType);
            caches.add(viewHolder);
            return viewHolder;
        }
    }

    public abstract static class ViewHolder {

        private static final String STATE = ViewHolder.class.getSimpleName();

        public final View itemView;

        private boolean isAttached;

        private int position;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView should not be null");
            }
            this.itemView = itemView;
        }

        private void attach(ViewGroup parent, int position) {
            isAttached = true;
            this.position = position;
            parent.addView(itemView);
        }

        private void detach(ViewGroup parent) {
            parent.removeView(itemView);
            isAttached = false;
        }

        private void onRestoreInstanceState(Parcelable state) {
            if (state instanceof Bundle) {
                Bundle bundle = (Bundle) state;
                SparseArray<Parcelable> ss = bundle.containsKey(STATE) ? bundle.getSparseParcelableArray(STATE) : null;
                if (ss != null) {
                    itemView.restoreHierarchyState(ss);
                }
            }
        }

        private Parcelable onSaveInstanceState() {
            SparseArray<Parcelable> state = new SparseArray<>();
            itemView.saveHierarchyState(state);
            Bundle bundle = new Bundle();
            bundle.putSparseParcelableArray(STATE, state);
            return bundle;
        }
    }
}