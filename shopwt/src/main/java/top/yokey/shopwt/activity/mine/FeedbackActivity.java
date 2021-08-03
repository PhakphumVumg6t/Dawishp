package top.yokey.shopwt.activity.mine;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.MemberFeedbackModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class FeedbackActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText contentEditText;
    private AppCompatTextView submitTextView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_feedback);
        mainToolbar = findViewById(R.id.mainToolbar);
        contentEditText = findViewById(R.id.contentEditText);
        submitTextView = findViewById(R.id.submitTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "用户反馈");

    }

    @Override
    public void initEven() {

        submitTextView.setOnClickListener(view -> feedback());

    }

    //自定义方法

    private void feedback() {

        BaseApplication.get().hideKeyboard(getActivity());

        String feedback = Objects.requireNonNull(contentEditText.getText()).toString();
        if (TextUtils.isEmpty(feedback)) {
            BaseToast.get().show("请输入内容...");
            return;
        }

        submitTextView.setEnabled(false);
        submitTextView.setText("提交中...");

        MemberFeedbackModel.get().feedbackAdd(feedback, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    BaseToast.get().showSuccess();
                    BaseApplication.get().finish(getActivity());
                } else {
                    submitTextView.setEnabled(true);
                    submitTextView.setText("提 交");
                    BaseToast.get().showFailure();
                }
            }

            @Override
            public void onFailure(String reason) {
                submitTextView.setEnabled(true);
                submitTextView.setText("提 交");
                BaseToast.get().show(reason);
            }
        });

    }

}
