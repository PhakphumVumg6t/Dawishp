package top.yokey.shopwt.activity.main;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.CartBean;
import top.yokey.base.model.MemberCartModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.activity.base.LoginActivity;
import top.yokey.shopwt.adapter.CartListAdapter;
import top.yokey.shopwt.base.BaseActivity;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.view.PullRefreshView;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class CartActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private RelativeLayout tipsRelativeLayout;
    private AppCompatTextView tipsTextView;
    private PullRefreshView mainPullRefreshView;
    private View lineView;
    private LinearLayoutCompat operaLinearLayout;
    private AppCompatTextView moneyTextView;
    private AppCompatCheckBox mainCheckBox;
    private AppCompatTextView balanceTextView;

    private int countInt;
    private float moneyFloat;
    private String cartIdString;
    private CartListAdapter mainAdapter;
    private ArrayList<CartBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_main_cart);
        mainToolbar = findViewById(R.id.mainToolbar);
        tipsRelativeLayout = findViewById(R.id.tipsRelativeLayout);
        tipsTextView = findViewById(R.id.tipsTextView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);
        lineView = findViewById(R.id.lineView);
        operaLinearLayout = findViewById(R.id.operaLinearLayout);
        moneyTextView = findViewById(R.id.moneyTextView);
        mainCheckBox = findViewById(R.id.mainCheckBox);
        balanceTextView = findViewById(R.id.balanceTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "?????????");

        if (BaseApplication.get().isLogin()) {
            tipsTextView.setText("?????????...");
        }

        countInt = 0;
        moneyFloat = 0f;
        cartIdString = "";
        mainArrayList = new ArrayList<>();
        mainAdapter = new CartListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.setCanLoadMore(false);

        getCart();

    }

    @Override
    public void initEven() {

        tipsRelativeLayout.setOnClickListener(view -> {
            if (!BaseApplication.get().isLogin()) {
                BaseApplication.get().start(getActivity(), LoginActivity.class);
            } else {
                getCart();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCart();
            }

            @Override
            public void onLoadMore() {

            }
        });

        mainAdapter.setOnItemClickListener(new CartListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, CartBean cartBean) {

            }

            @Override
            public void onStore(int position, CartBean cartBean) {
                BaseApplication.get().startStore(getActivity(), cartBean.getStoreId());
            }

            @Override
            public void onCheck(int position, boolean isCheck, CartBean cartBean) {
                mainArrayList.get(position).setCheck(isCheck);
                for (int i = 0; i < mainArrayList.get(position).getGoods().size(); i++) {
                    mainArrayList.get(position).getGoods().get(i).setCheck(isCheck);
                }
                mainAdapter.notifyItemChanged(position);
                checkAll();
                calc();
            }

            @Override
            public void onGoods(int position, int positionGoods, CartBean.GoodsBean goodsBean) {
                BaseApplication.get().startGoods(getActivity(), goodsBean.getGoodsId());
            }

            @Override
            public void onGoodsDelete(int position, int positionGoods, CartBean.GoodsBean goodsBean) {
                cartDel(position, positionGoods, goodsBean);
            }

            @Override
            public void onGoodsAdd(int position, int positionGoods, CartBean.GoodsBean goodsBean) {
                int number = Integer.parseInt(goodsBean.getGoodsNum()) + 1;
                cartEditQuantity(goodsBean.getCartId(), number);
            }

            @Override
            public void onGoodsSub(int position, int positionGoods, CartBean.GoodsBean goodsBean) {
                int number = Integer.parseInt(goodsBean.getGoodsNum());
                if (number == 1) {
                    BaseToast.get().show("?????????????????????...");
                    return;
                }
                number--;
                cartEditQuantity(goodsBean.getCartId(), number);
            }

            @Override
            public void onGoodsCheck(int position, int positionGoods, boolean isCheck, CartBean.GoodsBean goodsBean) {
                boolean check = true;
                mainArrayList.get(position).getGoods().get(positionGoods).setCheck(isCheck);
                for (int i = 0; i < mainArrayList.get(position).getGoods().size(); i++) {
                    if (!mainArrayList.get(position).getGoods().get(i).isCheck()) {
                        check = false;
                    }
                }
                mainArrayList.get(position).setCheck(check);
                mainAdapter.notifyItemChanged(position);
                checkAll();
                calc();
            }
        });

        mainCheckBox.setOnClickListener(view -> {
            for (int i = 0; i < mainArrayList.size(); i++) {
                mainArrayList.get(i).setCheck(mainCheckBox.isChecked());
                for (int j = 0; j < mainArrayList.get(i).getGoods().size(); j++) {
                    mainArrayList.get(i).getGoods().get(j).setCheck(mainCheckBox.isChecked());
                }
            }
            mainAdapter.notifyDataSetChanged();
            calc();
        });

        balanceTextView.setOnClickListener(view -> BaseApplication.get().startGoodsBuy(getActivity(), cartIdString, "1"));

    }

    //???????????????

    @SuppressWarnings("StringConcatenationInLoop")
    private void calc() {

        countInt = 0;
        moneyFloat = 0.0f;
        cartIdString = "";

        for (int i = 0; i < mainArrayList.size(); i++) {
            for (int j = 0; j < mainArrayList.get(i).getGoods().size(); j++) {
                if (mainArrayList.get(i).getGoods().get(j).isCheck()) {
                    String cartId = mainArrayList.get(i).getGoods().get(j).getCartId();
                    int count = Integer.parseInt(mainArrayList.get(i).getGoods().get(j).getGoodsNum());
                    float money = Float.parseFloat(mainArrayList.get(i).getGoods().get(j).getGoodsPrice()) * count;
                    countInt += count;
                    moneyFloat += money;
                    cartIdString += cartId + "|" + count + ",";
                }
            }
        }

        if (!TextUtils.isEmpty(cartIdString)) {
            balanceTextView.setEnabled(true);
            cartIdString = cartIdString.substring(0, cartIdString.length() - 1);
        } else {
            balanceTextView.setEnabled(false);
        }

        String temp = "??? <font color='#FF0000'>" + countInt + "</font> ??????" + "?????????" + "<font color='#FF0000'>???" + moneyFloat + " ???</font>";
        moneyTextView.setText(Html.fromHtml(temp));

    }

    private void getCart() {

        MemberCartModel.get().cartList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "cart_list");
                moneyFloat = Float.parseFloat(JsonUtil.getDatasString(baseBean.getDatas(), "sum"));
                countInt = Integer.parseInt(JsonUtil.getDatasString(baseBean.getDatas(), "cart_count"));
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, CartBean.class));
                for (int i = 0; i < mainArrayList.size(); i++) {
                    mainArrayList.get(i).setCheck(true);
                    for (int j = 0; j < mainArrayList.get(i).getGoods().size(); j++) {
                        mainArrayList.get(i).getGoods().get(j).setCheck(true);
                    }
                }
                mainPullRefreshView.setComplete();
                mainCheckBox.setChecked(true);
                if (mainArrayList.size() == 0) {
                    tipsEmpty();
                } else {
                    tipsRelativeLayout.setVisibility(View.GONE);
                    operaLinearLayout.setVisibility(View.VISIBLE);
                    mainPullRefreshView.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);
                    calc();
                }
            }

            @Override
            public void onFailure(String reason) {
                tipsRelativeLayout.setVisibility(View.VISIBLE);
                operaLinearLayout.setVisibility(View.GONE);
                mainPullRefreshView.setVisibility(View.GONE);
                lineView.setVisibility(View.GONE);
                tipsTextView.setText(reason);
            }
        });

    }

    private void checkAll() {

        boolean check = true;

        for (int i = 0; i < mainArrayList.size(); i++) {
            for (int j = 0; j < mainArrayList.get(i).getGoods().size(); j++) {
                if (!mainArrayList.get(i).getGoods().get(j).isCheck()) {
                    check = false;
                }
            }
        }

        mainCheckBox.setChecked(check);

    }

    private void tipsEmpty() {

        tipsRelativeLayout.setVisibility(View.VISIBLE);
        operaLinearLayout.setVisibility(View.GONE);
        mainPullRefreshView.setVisibility(View.GONE);
        lineView.setVisibility(View.GONE);
        tipsTextView.setText("????????????????????????????????????");

    }

    private void cartEditQuantity(String cartId, int quantity) {

        MemberCartModel.get().cartEditQuantity(cartId, quantity + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                getCart();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

    private void cartDel(final int position, final int positionGoods, CartBean.GoodsBean goodsBean) {

        MemberCartModel.get().cartDel(goodsBean.getCartId(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.get(position).getGoods().remove(positionGoods);
                if (mainArrayList.get(position).getGoods().size() == 0) {
                    mainArrayList.remove(position);
                }
                if (mainArrayList.size() == 0) {
                    tipsEmpty();
                }
                if (position == 0) {
                    mainAdapter.notifyDataSetChanged();
                } else {
                    mainAdapter.notifyItemChanged(position);
                }
                calc();
            }

            @Override
            public void onFailure(String reason) {
                BaseToast.get().show(reason);
            }
        });

    }

}
