package com.mredrock.cyxbs.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.NavigationBarMarginView;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.event.OnNavigationMenuSelectedItemChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbs.ui.activity.me.AboutActivity;
import com.mredrock.cyxbs.ui.activity.me.AboutMeActivity;
import com.mredrock.cyxbs.ui.activity.me.EditInfoActivity;
import com.mredrock.cyxbs.ui.activity.me.EmptyRoomActivity;
import com.mredrock.cyxbs.ui.activity.me.ExamAndGradeActivity;
import com.mredrock.cyxbs.ui.activity.me.MyTrendActivity;
import com.mredrock.cyxbs.ui.activity.me.NewsRemindActivity;
import com.mredrock.cyxbs.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbs.ui.activity.me.SchoolCalendarActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbs.ui.fragment.UnLoginFragment;
import com.mredrock.cyxbs.ui.fragment.UserFragment;
import com.mredrock.cyxbs.ui.fragment.explore.ExploreFragment;
import com.mredrock.cyxbs.ui.fragment.social.SocialContainerFragment;
import com.mredrock.cyxbs.util.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    @Bind(R.id.main_toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
//    @Bind(R.id.bottom_bar)
//    BottomBar mBottomBar;
    @Bind(R.id.main_drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_navigation)
    NavigationView mNavigationView;
    @Bind(R.id.main_view_pager)
    ViewPager mViewPager;

    @BindString(R.string.community)
    String mStringCommunity;
    @BindString(R.string.course)
    String mStringCourse;
    @BindString(R.string.explore)
    String mStringExplore;
    @BindString(R.string.my_page)
    String mStringMyPage;


    BaseFragment socialContainerFragment;
    BaseFragment courseContainerFragment;
    BaseFragment exploreFragment;
    BaseFragment userFragment;
    BaseFragment unLoginFragment;

    private ArrayList<Fragment> mFragments;
    private TabPagerAdapter mAdapter;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
//        UpdateUtil.checkUpdate(this, false);
        InterFilter();
    }

    /**
     * 适配魅族 3D TOUCH
     */
    private void InterFilter() {
        Uri data = getIntent().getData();
        if (data != null && TextUtils.equals("forcetouch", data.getScheme())) {
            Log.d(TAG, "InterFilter: ");
            if (TextUtils.equals("/schedule", data.getPath())) {
                Log.d(TAG, "InterFilter: 进入主页");
            }
            if (TextUtils.equals("/new", data.getPath())) {
                Intent intent = new Intent(this, NewsRemindActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals("/foods", data.getPath())) {
                Intent intent = new Intent(this, SurroundingFoodActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals("/date", data.getPath())) {
                Intent intent = new Intent(this, NoCourseActivity.class);
                startActivity(intent);
            }
        }
    }

    private void initView() {
        initToolbar();
        socialContainerFragment = new SocialContainerFragment();
        courseContainerFragment = new CourseContainerFragment();
        exploreFragment = new ExploreFragment();
        userFragment = new UserFragment();
        unLoginFragment = new UnLoginFragment();

        mFragments = new ArrayList<>();
        //判断是否登陆
        if (!APP.isLogin()) {
            mFragments.add(unLoginFragment);
        } else {
            mFragments.add(courseContainerFragment);
        }
        mFragments.add(socialContainerFragment);
        mFragments.add(exploreFragment);
        mFragments.add(userFragment);

        ArrayList<String> titles = new ArrayList<>();
        titles.add(mStringCourse);
        titles.add(mStringCommunity);
        titles.add(mStringExplore);
        titles.add(mStringMyPage);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        refreshNavigationView(APP.isLogin());
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (getCurrentPosition()) {
            case 0:
                EventBus.getDefault().post(new OnNavigationMenuSelectedItemChangeEvent(R.id.item_course));
                break;
            case 1:
                EventBus.getDefault().post(new OnNavigationMenuSelectedItemChangeEvent(R.id.item_community));
                break;
            case 2:
                EventBus.getDefault().post(new OnNavigationMenuSelectedItemChangeEvent(R.id.item_explorer));
                break;
        }
    }

    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        boolean isLogin = event.getNewState();
        Log.d(TAG, "onLoginStateChangeEvent: " + APP.isFresh());
        if (!isLogin) {
            mFragments.remove(0);
            mFragments.add(0, new UnLoginFragment());
            mAdapter.notifyDataSetChanged();
        } else {
            mFragments.remove(0);
            mFragments.add(0, new CourseContainerFragment());
//            mBottomBar.setCurrentView(0);
            mAdapter.notifyDataSetChanged();
        }
        refreshNavigationHeader(event.getNewState());
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setTitle(mStringCommunity);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbar != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    public void refreshNavigationView(boolean isLogin) {
        refreshNavigationHeader(isLogin);
        // add a item as margin for immersive
        if (NavigationBarMarginView.isNeedMargin(this)) {
            mNavigationView.getMenu().add("").setEnabled(false);
        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                EventBus.getDefault().post(new OnNavigationMenuSelectedItemChangeEvent(item.getItemId()));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNaviagtionMenuSelectedItemChangeEvent(OnNavigationMenuSelectedItemChangeEvent event) {
        mNavigationView.setCheckedItem(event.getItemId());
        switch (event.getItemId()) {
            case R.id.item_course:
                mViewPager.setCurrentItem(0);
                setTitle(((CourseContainerFragment) courseContainerFragment).getTitle());
                break;
            case R.id.item_community:
                mViewPager.setCurrentItem(1);
                setTitle(R.string.community);
                break;
            case R.id.item_explorer:
                mViewPager.setCurrentItem(2);
                setTitle(R.string.explore);
                break;
            case R.id.item_relate_me:
                checkLoginAndRun(() -> startActivity(new Intent(this, AboutMeActivity.class)), "登录后才能查看与我相关哦");
                break;
            case R.id.item_my_trend:
                checkLoginAndRun(() -> startActivity(new Intent(this, MyTrendActivity.class)), "登录后才能查看我的动态哦");
                break;
            case R.id.item_no_course:
                checkLoginAndRun(() -> startActivity(new Intent(this, NoCourseActivity.class)), "登录后才能使用没课约哦");
                break;
            case R.id.item_empty_room:
                startActivity(new Intent(this, EmptyRoomActivity.class));
                break;
            case R.id.item_exam_and_grade:
                checkLoginAndRun(() -> startActivity(new Intent(this, ExamAndGradeActivity.class)), "登录后才能查看考试成绩哦");
                break;
            case R.id.item_school_calendar:
                startActivity(new Intent(this, SchoolCalendarActivity.class));
                break;
            case R.id.item_feedback:
                break;
            case R.id.item_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.item_account:
                startActivity(new Intent(this, EditInfoActivity.class));
        }
    }

    public void refreshNavigationHeader(boolean isLogin) {
        View headView = mNavigationView.getHeaderView(0);
        CircularImageView avatarView = (CircularImageView) headView.findViewById(R.id.nh_avatar);
        CircularImageView avatarAlpha = (CircularImageView) headView.findViewById(R.id.nh_avatar_alpha);
        TextView nicknameView = (TextView) headView.findViewById(R.id.nh_nickname);
        TextView usernameView = (TextView) headView.findViewById(R.id.nh_username);
        TextView stuNumView = (TextView) headView.findViewById(R.id.nh_stu_num);
        if (isLogin) {
            User user = APP.getUser(this);
            ImageLoader.getInstance().loadAvatar(user.photo_thumbnail_src, avatarView);
            nicknameView.setText(user.nickname);
            usernameView.setText(user.name);
            usernameView.setVisibility(View.VISIBLE);
            stuNumView.setText(user.stuNum);
            stuNumView.setVisibility(View.VISIBLE);
            headView.setOnClickListener(null);
            headView.setBackgroundResource(R.color.mdc_light_blue_800);
            avatarAlpha.setVisibility(View.VISIBLE);
            avatarAlpha.setOnClickListener(v -> EventBus.getDefault().post(new OnNavigationMenuSelectedItemChangeEvent(R.id.item_account)));
        } else {
            avatarView.setImageResource(R.drawable.ic_default_avatar_with_bg_white);
            nicknameView.setText(R.string.check_me_to_login);
            usernameView.setVisibility(View.GONE);
            stuNumView.setVisibility(View.GONE);
            headView.setOnClickListener(v -> EventBus.getDefault().post(new LoginEvent()));
            headView.setBackgroundResource(R.drawable.bg_head_navigation_unlogin);
            avatarAlpha.setOnClickListener(null);
            avatarAlpha.setVisibility(View.GONE);
        }
    }

    public void checkLoginAndRun(Runnable runnable, String messageWhenUnLogin) {
        if (APP.isLogin()) {
            runnable.run();
        } else {
            EventBus.getDefault().post(new AskLoginEvent(messageWhenUnLogin));
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}