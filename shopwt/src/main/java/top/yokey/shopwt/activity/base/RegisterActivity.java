package top.yokey.shopwt.activity.base;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseShared;
import top.yokey.base.base.BaseToast;
import top.yokey.base.base.MemberHttpClient;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.ConnectModel;
import top.yokey.base.model.LoginModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.base.util.TextUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.main.MainActivity;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.view.CenterTextView;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class RegisterActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private LinearLayoutCompat typeLinearLayout;
    private CenterTextView normalTextView;
    private CenterTextView mobileTextView;
    private LinearLayoutCompat normalLinearLayout;
    private AppCompatEditText usernameEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatEditText confirmEditText;
    private AppCompatEditText emailEditText;
    private AppCompatTextView registerTextView;
    private LinearLayoutCompat mobileLinearLayout;
    private AppCompatEditText mobileEditText;
    private AppCompatTextView getTextView;
    private AppCompatEditText codeEditText;
    private AppCompatEditText passwordSmsEditText;
    private AppCompatTextView completeTextView;

    private long exitTimeLong;
    private Drawable normalDrawable, normalPressDrawable;
    private Drawable mobileDrawable, mobilePressDrawable;

    @Override
    public void initView() {

        setContentView(R.layout.activity_base_register);
        mainToolbar = findViewById(R.id.mainToolbar);
        typeLinearLayout = findViewById(R.id.typeLinearLayout);
        normalTextView = findViewById(R.id.normalTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        normalLinearLayout = findViewById(R.id.normalLinearLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmEditText = findViewById(R.id.confirmEditText);
        emailEditText = findViewById(R.id.emailEditText);
        registerTextView = findViewById(R.id.registerTextView);
        mobileLinearLayout = findViewById(R.id.mobileLinearLayout);
        mobileEditText = findViewById(R.id.mobileEditText);
        getTextView = findViewById(R.id.getTextView);
        codeEditText = findViewById(R.id.codeEditText);
        passwordSmsEditText = findViewById(R.id.passwordSmsEditText);
        completeTextView = findViewById(R.id.completeTextView);

    }

    @Override
    public void initData() {

        exitTimeLong = 0L;
        normalDrawable = BaseApplication.get().getMipmap(R.mipmap.ic_register_normal, R.color.greyAdd);
        mobileDrawable = BaseApplication.get().getMipmap(R.mipmap.ic_register_mobile, R.color.greyAdd);
        normalPressDrawable = BaseApplication.get().getMipmap(R.mipmap.ic_register_normal_press);
        mobilePressDrawable = BaseApplication.get().getMipmap(R.mipmap.ic_register_mobile_press);

        setToolbar(mainToolbar, "????????????");
        getState();

    }

    @Override
    public void initEven() {

        normalTextView.setOnClickListener(view -> {
            normalTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
            normalTextView.setCompoundDrawablesWithIntrinsicBounds(normalPressDrawable, null, null, null);
            mobileTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            mobileTextView.setCompoundDrawablesWithIntrinsicBounds(mobileDrawable, null, null, null);
            normalLinearLayout.setVisibility(View.VISIBLE);
            mobileLinearLayout.setVisibility(View.GONE);
        });

        mobileTextView.setOnClickListener(view -> {
            normalTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
            normalTextView.setCompoundDrawablesWithIntrinsicBounds(normalDrawable, null, null, null);
            mobileTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
            mobileTextView.setCompoundDrawablesWithIntrinsicBounds(mobilePressDrawable, null, null, null);
            normalLinearLayout.setVisibility(View.GONE);
            mobileLinearLayout.setVisibility(View.VISIBLE);
        });

        registerTextView.setOnClickListener(view -> register());

        getTextView.setOnClickListener(view -> getCode());

        completeTextView.setOnClickListener(view -> checkCode());

    }

    @Override
    public void onReturn() {

        if (System.currentTimeMillis() - exitTimeLong > BaseConstant.TIME_EXIT) {
            BaseToast.get().showReturnOneMoreTime();
            exitTimeLong = System.currentTimeMillis();
        } else {
            super.onReturn();
        }

    }

    //???????????????

    private void getState() {

        ConnectModel.get().getState("connect_sms_reg", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.getDatasString(baseBean.getDatas(), "connect_sms_reg").equals("1")) {
                    normalTextView.setTextColor(BaseApplication.get().getColors(R.color.primary));
                    normalTextView.setCompoundDrawablesWithIntrinsicBounds(normalPressDrawable, null, null, null);
                    mobileTextView.setTextColor(BaseApplication.get().getColors(R.color.greyAdd));
                    mobileTextView.setCompoundDrawablesWithIntrinsicBounds(mobileDrawable, null, null, null);
                    typeLinearLayout.setVisibility(View.VISIBLE);
                    normalLinearLayout.setVisibility(View.VISIBLE);
                    mobileLinearLayout.setVisibility(View.GONE);
                } else {
                    typeLinearLayout.setVisibility(View.GONE);
                    normalLinearLayout.setVisibility(View.VISIBLE);
                    mobileLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void register() {

        BaseApplication.get().hideKeyboard(getActivity());

        String username = Objects.requireNonNull(usernameEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();
        String confirm = Objects.requireNonNull(confirmEditText.getText()).toString();
        String email = Objects.requireNonNull(emailEditText.getText()).toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm) || TextUtils.isEmpty(email)) {
            BaseToast.get().show("???????????????????????????...");
            return;
        }

        if (!password.equals(confirm)) {
            BaseToast.get().show("??????????????????????????????...");
            return;
        }

        if (!TextUtil.isEmail(email)) {
            BaseToast.get().show("???????????????????????????...");
            return;
        }

        registerTextView.setEnabled(false);
        registerTextView.setText("?????????...");

        LoginModel.get().register(username, password, email, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                registerTextView.setEnabled(true);
                registerTextView.setText("????????????");
                BaseToast.get().show("???????????????");
                MemberHttpClient.get().updateKey(JsonUtil.getDatasString(baseBean.getDatas(), "key"));
                BaseShared.get().putString(BaseConstant.SHARED_KEY, JsonUtil.getDatasString(baseBean.getDatas(), "key"));
                BaseApplication.get().start(getActivity(), MainActivity.class);
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                registerTextView.setText("????????????");
                registerTextView.setEnabled(true);
            }
        });

    }

    private void getCode() {

        BaseApplication.get().hideKeyboard(getActivity());

        String mobile = Objects.requireNonNull(mobileEditText.getText()).toString();

        if (!TextUtil.isMobile(mobile)) {
            BaseToast.get().show("??????????????????????????????");
            return;
        }

        getTextView.setEnabled(false);
        getTextView.setText("?????????...");

        ConnectModel.get().getSmsCaptcha("1", mobile, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                final String smsTime = JsonUtil.getDatasString(baseBean.getDatas(), "sms_time");
                final int time = Integer.parseInt(smsTime);
                //?????????
                new BaseCountTime(time * 1000, BaseConstant.TIME_TICK) {

                    int totalTime = time;

                    @Override
                    public void onTick(long millis) {
                        super.onTick(millis);
                        String temp = "???????????????" + totalTime-- + " S ???";
                        getTextView.setText(temp);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getTextView.setEnabled(true);
                        getTextView.setText("???????????????");
                    }

                }.start();

            }

            @Override
            public void onFailure(String reason) {
                getTextView.setEnabled(true);
                getTextView.setText("???????????????");
                BaseToast.get().show(reason);
            }
        });

    }

    private void checkCode() {

        BaseApplication.get().hideKeyboard(getActivity());

        String captcha = Objects.requireNonNull(codeEditText.getText()).toString();
        String mobile = Objects.requireNonNull(mobileEditText.getText()).toString();

        if (TextUtils.isEmpty(captcha)) {
            BaseToast.get().show("?????????????????????");
            return;
        }

        completeTextView.setEnabled(false);
        completeTextView.setText("?????????...");

        ConnectModel.get().checkSmsCaptcha("1", mobile, captcha, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    smsRegister();
                } else {
                    completeTextView.setEnabled(true);
                    completeTextView.setText("????????????");
                    BaseToast.get().showFailure();
                }
            }

            @Override
            public void onFailure(String reason) {
                completeTextView.setEnabled(true);
                completeTextView.setText("????????????");
                BaseToast.get().show(reason);
            }
        });

    }

    private void smsRegister() {

        String captcha = Objects.requireNonNull(codeEditText.getText()).toString();
        String mobile = Objects.requireNonNull(mobileEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordSmsEditText.getText()).toString();

        if (TextUtils.isEmpty(password)) {
            BaseToast.get().show("????????????????????????");
            return;
        }

        ConnectModel.get().smsRegister(mobile, captcha, password, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                completeTextView.setEnabled(true);
                completeTextView.setText("????????????");
                BaseToast.get().show("???????????????");
                MemberHttpClient.get().updateKey(JsonUtil.getDatasString(baseBean.getDatas(), "key"));
                BaseShared.get().putString(BaseConstant.SHARED_KEY, JsonUtil.getDatasString(baseBean.getDatas(), "key"));
                BaseApplication.get().start(getActivity(), MainActivity.class);
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                completeTextView.setEnabled(true);
                completeTextView.setText("????????????");
                BaseToast.get().show(reason);
            }
        });

    }

}
