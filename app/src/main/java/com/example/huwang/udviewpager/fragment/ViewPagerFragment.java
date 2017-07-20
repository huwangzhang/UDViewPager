package com.example.huwang.udviewpager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by huwang on 2017/5/12.
 */

public class ViewPagerFragment extends Fragment {
    private String mTitle;
    public static final String TITLE = "title";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(TITLE);
        }
        TextView tv = new TextView(getActivity());
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }

    public static ViewPagerFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
