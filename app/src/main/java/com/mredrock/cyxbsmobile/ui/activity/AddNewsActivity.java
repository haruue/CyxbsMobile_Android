package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.NineGridlayout;
import com.mredrock.cyxbsmobile.model.community.BBDD;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.Student;
import com.mredrock.cyxbsmobile.model.community.UploadImgResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.util.ScreenTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AddNewsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_cancel)
    TextView mCancelText;
    @Bind(R.id.toolbar_title)
    TextView mTitleText;
    @Bind(R.id.toolbar_save)
    TextView mSaveText;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.add_news_edit)
    EditText mAddNewsEdit;
    @Bind(R.id.iv_ngrid_layout)
    NineGridlayout mNineGridlayout;

    private final static String ADD_IMG = "file:///android_asset/add_news.jpg";
    private final static int REQUEST_IMAGE = 0001;
    private List<Image> mImgs;
    private int singlePicX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mImgs = new ArrayList<>();
        singlePicX = (int) new ScreenTools(this).getSinglePicX();
        mImgs.add(new Image(ADD_IMG, Image.ADDIMAG));
        mNineGridlayout.setImagesData(mImgs);
        setSupportActionBar(mToolBar);
        mCancelText.setOnClickListener(this);
        mSaveText.setOnClickListener(this);

        mNineGridlayout.setOnAddImagItemClickListener((v, position) -> {
            Intent intent = new Intent(AddNewsActivity.this, MultiImageSelectorActivity.class);
            // 是否显示调用相机拍照
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
            // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
            // 默认选择图片,回填选项(支持String ArrayList)
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        mNineGridlayout.setmOnClickDeletecteListener(new NineGridlayout.OnClickDeletecteListener() {
            @Override
            public void onClickDelete(View v, int position) {
                mImgs.remove(position);
                mNineGridlayout.setImagesData(mImgs);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_cancel:
                AddNewsActivity.this.finish();
                break;
            case R.id.toolbar_save:
                sendDynamic("我是你", "12345 12345 12345 12345", BBDD.BBDD);
                break;
        }
    }

    private void sendDynamic(String title, String content, int type) {
        Observable<OkResponse> observable;
        List<Image> currentImgs = new ArrayList<>();
        currentImgs.addAll(mImgs);
        currentImgs.remove(0);
        final String[] photoSrc = {""};
        final String[] thumbnailSrc = {""};
        if (currentImgs.size() > 0) {
            observable = uploadWithImg(currentImgs, title, content, type);
        } else {
            observable = uploadWithoutImg(title, content, type);
        }
        observable.subscribe(okResponse -> {
            if (okResponse.getState() == OkResponse.RESPONSE_OK) {
                closeLoadingProgress();
                showUploadSucess();
            }
        }, throwable -> {
            closeLoadingProgress();
            showUploadFail(throwable.toString());
        });

   /*     Observable.from(currentImgs)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(Schedulers.newThread())
                .map(image -> image.getUrl())
                .flatMap(s -> RequestManager.getInstance().uploadNewsImg(Student.STU_NUM, s))
                .buffer(mImgs.size() - 1)
                .flatMap(uploadImgResponses -> {
                    for (UploadImgResponse uploadImgResponse : uploadImgResponses) {
                        photoSrc[0] += photoSrc[0] + uploadImgResponse.getData().getPhotosrc() + ",";
                        thumbnailSrc[0] += thumbnailSrc[0] + uploadImgResponse.getData().getPhotosrc() + ",";
                    }
                    return RequestManager.getInstance().sendDynamic(BBDD.BBDD, "天地大同", Student.UER_ID, "122334", thumbnailSrc[0], photoSrc[0], Student.STU_NUM, Student.ID_NUM);
                })
                .subscribe(okResponse -> {
                    if (okResponse.getState() == OkResponse.RESPONSE_OK) {
                        closeLoadingProgress();
                        showUploadSucess();
                    }
                }, throwable -> {
                    closeLoadingProgress();
                    showUploadFail(throwable.toString());
                });*/
    }

    private Observable<OkResponse> uploadWithImg(List<Image> currentImgs, String title, String content, int type) {
        final String[] photoSrc = {""};
        final String[] thumbnailSrc = {""};
        return Observable.from(currentImgs)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(Schedulers.newThread())
                .map(image -> image.getUrl())
                .flatMap(url -> RequestManager.getInstance().uploadNewsImg(Student.STU_NUM, url))
                .buffer(currentImgs.size())
                .flatMap(uploadImgResponses -> {
                    for (UploadImgResponse uploadImgResponse : uploadImgResponses) {
                        photoSrc[0] += photoSrc[0] + uploadImgResponse.getData().getPhotosrc().split("/")[6] + ",";
                        thumbnailSrc[0] += thumbnailSrc[0] + uploadImgResponse.getData().getPhotosrc().split("/")[6] + ",";
                    }
                    return RequestManager.getInstance().sendDynamic(type, title, Student.UER_ID, content, thumbnailSrc[0], photoSrc[0], Student.STU_NUM, Student.ID_NUM);
                });
    }

    private Observable<OkResponse> uploadWithoutImg(String title, String content, int type) {
        return RequestManager.getInstance()
                .sendDynamic(type, title, Student.UER_ID, content, " ", " ", Student.STU_NUM, Student.ID_NUM)
                .doOnSubscribe(() -> showLoadingProgress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                Observable.from(path)
                        .map(s -> new Image(s, singlePicX, singlePicX, Image.NORMALIMAGE))
                        .map(image -> {
                            mImgs.add(image);
                            return mImgs;
                        })
                        .subscribe(images -> {
                            mNineGridlayout.setImagesData(images);
                        }, throwable -> {
                            Toast.makeText(AddNewsActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    private void showUploadFail(String reason) {
        Log.e("===========>>>", "showUploadFail:" + reason);
    }

    private void showUploadSucess() {
        Log.e("===========>>>", "showUploadSucess");
    }

    private void showLoadingProgress() {
        Log.e("===========>>>", "showLoadingProgress");
    }

    private void closeLoadingProgress() {
        Log.e("===========>>>", "closeLoadingProgress");
    }
}
