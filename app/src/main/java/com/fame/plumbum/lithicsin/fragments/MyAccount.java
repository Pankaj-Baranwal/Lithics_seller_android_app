package com.fame.plumbum.lithicsin.fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.adapters.OrderAdapter;
import com.fame.plumbum.lithicsin.model.Orders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankaj on 12/1/17.
 */

public class MyAccount extends Fragment {
    private OrderAdapter adapter;
    private List<Orders> OrdersList;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_account, container, false);

        initCollapsingToolbar();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        OrdersList = new ArrayList<>();
        adapter = new OrderAdapter(getContext(), OrdersList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareOrder();

        try {
            Glide.with(getContext()).load(R.drawable.cover).into((ImageView) rootView.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    return rootView;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few Order for testing
     */
    private void prepareOrder() {
        int[] covers = new int[]{
                R.drawable.bag1,
                R.drawable.bag2,
                R.drawable.bag3,
                R.drawable.earring1,
                R.drawable.earring2,
                R.drawable.earring3,
                R.drawable.earring4,
                R.drawable.earring5,
                R.drawable.necklace,
                R.drawable.album10,
                R.drawable.album11};

        Orders a = new Orders("Order No: 1234567", 234, covers[0]);
        OrdersList.add(a);

        a = new Orders("Order No: 5456544", 348, covers[1]);
        OrdersList.add(a);

        a = new Orders("Order No: 7823423", 1145, covers[2]);
        OrdersList.add(a);

        a = new Orders("Order No: 2893473", 712, covers[3]);
        OrdersList.add(a);

        a = new Orders("Order No: 1289376", 1784, covers[4]);
        OrdersList.add(a);

        a = new Orders("Order No: 23487334", 178, covers[5]);
        OrdersList.add(a);

        a = new Orders("Order No: 2389473", 565, covers[6]);
        OrdersList.add(a);

        a = new Orders("Order No: 2389473", 914, covers[7]);
        OrdersList.add(a);

        a = new Orders("Order No: 2379423", 4511, covers[8]);
        OrdersList.add(a);

        a = new Orders("Order No: 9283743", 17, covers[9]);
        OrdersList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}