package com.quinn.githubknife.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.quinn.iconlibrary.icons.OctIcon;
import com.quinn.githubknife.R;
import com.quinn.githubknife.presenter.UserInfoPresenter;
import com.quinn.githubknife.presenter.UserInfoPresenterImpl;
import com.quinn.githubknife.ui.BaseActivity;
import com.quinn.githubknife.ui.fragments.FollowerFragment;
import com.quinn.githubknife.ui.fragments.FollowingFragment;
import com.quinn.githubknife.ui.fragments.StarredRepoFragment;
import com.quinn.githubknife.ui.widget.AnimateFirstDisplayListener;
import com.quinn.githubknife.ui.widget.UserLabel;
import com.quinn.githubknife.utils.BitmapUtils;
import com.quinn.githubknife.utils.L;
import com.quinn.githubknife.utils.ToastUtils;
import com.quinn.githubknife.view.UserInfoView;
import com.quinn.httpknife.github.User;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity implements UserInfoView {

    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private User user;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.backdrop)
    ImageView backDrop;


    @Bind(R.id.relation)
    FloatingActionButton relationBtn;


    @Bind(R.id.starLabel)
    UserLabel starLabel;
    @Bind(R.id.followersLabel)
    UserLabel followersLabel;
    @Bind(R.id.followingsLabel)
    UserLabel followingsLabel;


    @Bind(R.id.nickname)
    TextView nickname;

    @Bind(R.id.iconEmail)
    ImageView iconEmail;
    @Bind(R.id.iconBlog)
    ImageView iconBlog;
    @Bind(R.id.iconCompany)
    ImageView iconCompany;
    @Bind(R.id.iconLocation)
    ImageView iconLocation;
    @Bind(R.id.iconJoinTime)
    ImageView iconJoin;

    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.company)
    TextView company;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.blog)
    TextView blog;
    @Bind(R.id.joinTime)
    TextView joinTime;


    @Bind(R.id.scrollWrap)
    View scrollWrap;

    FollowState followState;


    enum FollowState {
        UNKNOWN,
        FOLLOWED,
        UNFOLLOWED
    }


    private UserInfoPresenter presenter;


    public static void launch(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        } else if (savedInstanceState != null) {
            user = (User) savedInstanceState.getSerializable("user");
        }
        presenter = new UserInfoPresenterImpl(this, this);
        presenter.user(user.getLogin());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(user.getLogin());
        paletteToolbar();

        BitmapUtils.setIconFont(this, iconEmail, OctIcon.EMAIL, R.color.theme_color);
        BitmapUtils.setIconFont(this, iconBlog, OctIcon.BLOG, R.color.theme_color);
        BitmapUtils.setIconFont(this, iconCompany, OctIcon.COMPANY, R.color.theme_color);
        BitmapUtils.setIconFont(this, iconLocation, OctIcon.LOCATE, R.color.theme_color);
        BitmapUtils.setIconFont(this, iconJoin, OctIcon.JOIN, R.color.theme_color);

        blog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        blog.getPaint().setAntiAlias(true);//抗锯齿
        email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        email.getPaint().setAntiAlias(true);//抗锯齿


        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        scrollWrap.setMinimumHeight(screenHeight - actionBarHeight);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

    }


    public void paletteToolbar(){
        //collapsingToolbar.setcon
        imageLoader.displayImage(user.getAvatar_url(), backDrop, option, new AnimateFirstDisplayListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);

//                Palette.generateAsync(loadedImage, 24, new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//
//                        Palette.Swatch vibrant = palette.getVibrantSwatch();
//                        Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
//                        Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
//                        Palette.Swatch muted = palette.getMutedSwatch();
//                        Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
//                        Palette.Swatch lightMuted = palette.getLightMutedSwatch();
//                        Palette.Swatch swatch = vibrant;
//                        swatch = (swatch == null) ? muted : swatch;
//                        swatch = (swatch == null) ? darkVibrant : swatch;
//                        swatch = (swatch == null) ? darkMuted : swatch;
//                        swatch = (swatch == null) ? lightVibrant : swatch;
//                        swatch = (swatch == null) ? lightMuted : swatch;
//                        collapsingToolbar.setContentScrim(new ColorDrawable(swatch.getRgb()));
//                        // 使用颜色
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setStatusBarColor(swatch.getRgb());
//                        }
//                    }
//                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                presenter.user(user.getLogin());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadUser(User user) {
        if (user == null)
            return;
        /**
         * 处理返回为空的
         */
        String nicknameStr= this.user.getLogin();
        if(TextUtils.isEmpty(user.getName())){
            nickname.setText(this.user.getLogin());
        }else{
            nickname.setText(user.getName());
        }
        followersLabel.setValue("" + user.getFollowers());
        followingsLabel.setValue("" + user.getFollowing());
        //repoLabel.setValue("" + user.getPublic_repos());



        email.setText(user.getEmail());
        company.setText(user.getCompany());
        blog.setText(user.getBlog());
        location.setText(user.getLocation());
        Date date = user.getCreated_at();

        joinTime.setText(date.toLocaleString());
        presenter.hasFollow(user.getLogin());
    }

    @Override
    public void setFollowState(boolean isFollow) {

        if (isFollow) {
            relationBtn.setImageDrawable(getResources().getDrawable(R.mipmap.unfollow));
            followState = FollowState.FOLLOWED;

        } else {
            relationBtn.setImageDrawable(getResources().getDrawable(R.mipmap.follow));
            followState = FollowState.UNFOLLOWED;
        }

        presenter.starredCount(user.getLogin());
    }

    @Override
    public void setStarredCount(int count) {
        starLabel.setValue("" + count);
    }

    @Override
    public void onError(String msg) {
        ToastUtils.showMsg(this, msg);
    }

    @OnClick(R.id.followerWrap)
    void viewFollower() {
        viewDetail(FollowerFragment.TAG);
    }

    @OnClick(R.id.followingWrap)
    void viewFollowing() {
        viewDetail(FollowingFragment.TAG);
    }

    @OnClick(R.id.starWrap)
    void viewStarred() {
        viewDetail(StarredRepoFragment.TAG);
    }


    @OnClick(R.id.relation)
    void changeRelation() {
        L.i(TAG, "try to changeRelation");
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        switch (followState) {
            case UNFOLLOWED:
                L.i(TAG, "try to follow " + user.getLogin());
                builder.setTitle("Follow Someone");
                builder.setMessage("Sure to follow " + user.getLogin() + "?");
                builder.setPositiveButton("follow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.follow(user.getLogin());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                break;
            case FOLLOWED:
                L.i(TAG, "try to unfollow " + user.getLogin());
                builder.setTitle("Unfollow Someone");
                builder.setMessage("Sure to unfollow " + user.getLogin() + "?");
                builder.setPositiveButton("Unfollow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.unFollow(user.getLogin());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                break;
            case UNKNOWN:
                break;
        }

    }

    @OnClick(R.id.blog)
    void directToBlog(){
        L.i(TAG,"click blog");
        if(blog.getText().toString().isEmpty() == false)
            redirectToBrowser(blog.getText().toString());
    }

    @OnClick(R.id.email)
    public void sendEmail(){
        if(email.getText().toString().isEmpty() == false)
            sendEmail(email.getText().toString());
    }


    public void viewDetail(String contentType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user.getLogin());
        bundle.putString("fragment", contentType);
        FoActivity.launch(this, bundle);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user", user);
    }
}
