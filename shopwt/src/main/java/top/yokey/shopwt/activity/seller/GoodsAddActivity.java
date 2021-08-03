package top.yokey.shopwt.activity.seller;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import top.yokey.base.base.BaseFileClient;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.SellerAlbumModel;
import top.yokey.base.model.SellerGoodsModel;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.choose.CateActivity;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.base.BaseImageLoader;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class GoodsAddActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatTextView cateTextView;
    private AppCompatEditText nameEditText;
    private AppCompatEditText moneyEditText;
    private AppCompatEditText priceEditText;
    private AppCompatTextView discountTextView;
    private AppCompatImageView oneImageView;
    private AppCompatImageView twoImageView;
    private AppCompatImageView thrImageView;
    private AppCompatImageView fouImageView;
    private AppCompatImageView fivImageView;
    private AppCompatImageView[] imageImageView;
    private AppCompatEditText stockEditText;
    private AppCompatEditText numberEditText;
    private AppCompatEditText logisticsEditText;
    private AppCompatEditText descEditText;
    private AppCompatRadioButton nowRadioButton;
    private AppCompatRadioButton wareRadioButton;
    private AppCompatTextView sendTextView;

    private int positionInt;
    private String gcIdString;
    private String[] imageString;

    @Override
    public void initView() {

        setContentView(R.layout.activity_seller_goods_add);
        mainToolbar = findViewById(R.id.mainToolbar);
        cateTextView = findViewById(R.id.cateTextView);
        nameEditText = findViewById(R.id.nameEditText);
        moneyEditText = findViewById(R.id.moneyEditText);
        priceEditText = findViewById(R.id.priceEditText);
        discountTextView = findViewById(R.id.discountTextView);
        oneImageView = findViewById(R.id.oneImageView);
        twoImageView = findViewById(R.id.twoImageView);
        thrImageView = findViewById(R.id.thrImageView);
        fouImageView = findViewById(R.id.fouImageView);
        fivImageView = findViewById(R.id.fivImageView);
        stockEditText = findViewById(R.id.stockEditText);
        numberEditText = findViewById(R.id.numberEditText);
        logisticsEditText = findViewById(R.id.logisticsEditText);
        descEditText = findViewById(R.id.descEditText);
        nowRadioButton = findViewById(R.id.nowRadioButton);
        wareRadioButton = findViewById(R.id.wareRadioButton);
        sendTextView = findViewById(R.id.sendTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "发布商品");

        imageImageView = new AppCompatImageView[5];
        imageImageView[0] = oneImageView;
        imageImageView[1] = twoImageView;
        imageImageView[2] = thrImageView;
        imageImageView[3] = fouImageView;
        imageImageView[4] = fivImageView;

        imageString = new String[5];
        imageString[0] = "";
        imageString[1] = "";
        imageString[2] = "";
        imageString[3] = "";
        imageString[4] = "";

        gcIdString = "";

    }

    @Override
    public void initEven() {

        cateTextView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), CateActivity.class, BaseConstant.CODE_CLASS));

        for (int i = 0; i < 5; i++) {
            final int pos = i;
            imageImageView[i].setOnClickListener(view -> {
                positionInt = pos;
                BaseApplication.get().startImagePicker(getActivity(), 1, BaseConstant.CODE_ALBUM, false);
            });
        }

        nowRadioButton.setOnClickListener(view -> {
            nowRadioButton.setChecked(true);
            wareRadioButton.setChecked(false);
        });

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String money = Objects.requireNonNull(moneyEditText.getText()).toString();
                String price = Objects.requireNonNull(priceEditText.getText()).toString();
                if (!TextUtils.isEmpty(money) && !TextUtils.isEmpty(price)) {
                    float m = Float.parseFloat(money);
                    float p = Float.parseFloat(price);
                    String dis = ((int) (m / p * 100)) + "";
                    discountTextView.setText(dis);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wareRadioButton.setOnClickListener(view -> {
            nowRadioButton.setChecked(false);
            wareRadioButton.setChecked(true);
        });

        sendTextView.setOnClickListener(view -> send());

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK) {
            switch (req) {
                case BaseConstant.CODE_CLASS:
                    gcIdString = intent.getStringExtra(BaseConstant.DATA_GCID);
                    cateTextView.setText(intent.getStringExtra(BaseConstant.DATA_CONTENT));
                    break;
                case BaseConstant.CODE_ALBUM:
                    @SuppressWarnings("unchecked")
                    ArrayList<ImageItem> arrayList = (ArrayList<ImageItem>) intent.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    updateImage(arrayList.get(0).path);
                    break;
                default:
                    break;
            }
        }
    }

    //自定义方法

    private void send() {

        String name = Objects.requireNonNull(nameEditText.getText()).toString();
        String money = Objects.requireNonNull(moneyEditText.getText()).toString();
        String price = Objects.requireNonNull(priceEditText.getText()).toString();
        String storage = Objects.requireNonNull(stockEditText.getText()).toString();
        String serial = Objects.requireNonNull(numberEditText.getText()).toString();
        String freight = Objects.requireNonNull(logisticsEditText.getText()).toString();
        String desc = Objects.requireNonNull(descEditText.getText()).toString();
        String state = nowRadioButton.isChecked() ? "1" : "0";
        String discount = discountTextView.getText().toString();
        StringBuilder imageAllBuilder = new StringBuilder();
        for (String string : imageString) {
            if (!TextUtils.isEmpty(string)) {
                imageAllBuilder.append(string).append(",");
            }
        }
        String imageAll = imageAllBuilder.toString();
        if (TextUtils.isEmpty(imageAll)) {
            BaseToast.get().show("最少上传一张图片！");
            return;
        }
        imageAll = imageAll.substring(0, imageAll.length() - 1);
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(money) || TextUtils.isEmpty(price) || TextUtils.isEmpty(storage) || TextUtils.isEmpty(serial) || TextUtils.isEmpty(freight) || TextUtils.isEmpty(desc)) {
            BaseToast.get().show("请输入完整信息！");
            return;
        }

        sendTextView.setEnabled(false);
        sendTextView.setText("发布中...");

        SellerGoodsModel.get().goodsAdd(gcIdString, cateTextView.getText().toString(), name, money, price, discount, imageAll, storage, serial, freight, desc, state, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                BaseToast.get().show("发布成功！");
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
                sendTextView.setText("发布商品");
                sendTextView.setEnabled(true);
            }
        });

    }

    private void updateImage(String path) {

        SellerAlbumModel.get().imageUpload(BaseFileClient.get().createImage("seller_add_goods_" + positionInt, BaseImageLoader.get().getLocal(path)), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    JSONObject jsonObject = new JSONObject(baseBean.getDatas());
                    imageString[positionInt] = jsonObject.getString("name");
                    BaseImageLoader.get().display(jsonObject.getString("thumb_name"), imageImageView[positionInt]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
