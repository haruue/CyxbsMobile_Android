package com.mredrock.cyxbs.ui.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.PersonInfo;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.social.PostNewsActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 社区
 * //TODO: Modify it
 */
public class SocialContainerFragment extends BaseFragment {

    @Bind(R.id.community_TabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.community_ViewPager)
    ViewPager mViewPager;
    @Bind(R.id.fab_add)
    FloatingActionButton mFabAdd;
    private boolean firstLogin = false;
    private int resumenCount = 0;

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_container, container, false);
        ButterKnife.bind(this, view);
        getUserData();
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstLogin && resumenCount == 1) {
            firstLogin = false;
            getUserData();
        }
        ++resumenCount;
    }

    private void getUserData() {
        if (APP.isLogin()) {
            mUser = APP.getUser(getContext());
            if (mUser.id == null) getPersonInfoData();
            else init();
        } else {
            firstLogin = true;
//            EventBus.getDefault().post(new LoginEvent());
        }
    }

    @OnClick(R.id.fab_add)
    public void onClickFabAdd() {
        if (APP.isLogin()) {
            PostNewsActivity.startActivity(getActivity());
        } else {
            EventBus.getDefault().post(new AskLoginEvent("还没有完善信息，不能发动态哟！"));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getPersonInfoData() {
        if (!APP.isLogin()) {
//            EventBus.getDefault().post(new LoginEvent());
            return;
        }
        if (mUser != null) {
            RequestManager.getInstance().getPersonInfo(new SimpleSubscriber<>(getActivity(), new SubscriberListener<PersonInfo>() {
                @Override
                public void onNext(PersonInfo personInfo) {
                    super.onNext(personInfo);
                    super.onNext(personInfo);
                    mUser = User.cloneFromUserInfo(mUser, personInfo);
                    APP.setUser(getActivity(), mUser);
//                    init();
                }
            }), mUser.stuNum, mUser.stuNum, mUser.idNum);
        }
    }


    private void init() {
        List<Fragment> fragmentLIst = new ArrayList<>();

        HotNewsFragment mPopularNewFragment = new HotNewsFragment();
        BBDDNewsFragment mBBLLNewFragment = new BBDDNewsFragment();

        OfficialFragment mOfficialFragment = new OfficialFragment();
        fragmentLIst.add(mPopularNewFragment);
        fragmentLIst.add(mBBLLNewFragment);
        fragmentLIst.add(mOfficialFragment);

        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), fragmentLIst, Arrays
                .asList(getActivity().getResources().getStringArray(R.array.community_tab_tiles)));

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragmentLIst.size());

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void changeViewPagerIndex(int index) {
        mViewPager.setCurrentItem(index);
    }
}
