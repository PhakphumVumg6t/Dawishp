package top.yokey.shopwt.activity.store;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.squareup.otto.Subscribe;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import top.yokey.base.base.BaseCountTime;
import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.StoreActivityBean;
import top.yokey.base.bean.StoreInfoBean;
import top.yokey.base.event.StoreBeanEvent;
import top.yokey.base.model.StoreModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseApplication;
import top.yokey.shopwt.base.BaseConstant;
import top.yokey.shopwt.base.BaseFragment;

/**
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

@ContentView(R.layout.fragment_store_activity)
public class ActivityFragment extends BaseFragment {

    @ViewInject(R.id.mansongCardView)
    private CardView mansongCardView;
    @ViewInject(R.id.manSongNameTextView)
    private AppCompatTextView manSongNameTextView;
    @ViewInject(R.id.manSongTimeTextView)
    private AppCompatTextView manSongTimeTextView;
    @ViewInject(R.id.manSongDescTextView)
    private AppCompatTextView manSongDescTextView;
    @ViewInject(R.id.manSongRemarkTextView)
    private AppCompatTextView manSongRemarkTextView;

    @ViewInject(R.id.xianshiCardView)
    private CardView xianshiCardView;
    @ViewInject(R.id.xianshiNameTextView)
    private AppCompatTextView xianshiNameTextView;
    @ViewInject(R.id.xianshiTimeTextView)
    private AppCompatTextView xianshiTimeTextView;
    @ViewInject(R.id.xianshiDescTextView)
    private AppCompatTextView xianshiDescTextView;
    @ViewInject(R.id.xianshiRemarkTextView)
    private AppCompatTextView xianshiRemarkTextView;

    @ViewInject(R.id.activityTextView)
    private AppCompatTextView activityTextView;

    private StoreInfoBean storeInfoBean;
    private StoreActivityBean storeActivityBean;

    @Override
    public void initData() {

        storeInfoBean = new StoreInfoBean();
        storeActivityBean = new StoreActivityBean();

    }

    @Override
    public void initEven() {

        mansongCardView.setOnClickListener(view -> BaseApplication.get().startStoreGoodsList(getActivity(), storeInfoBean.getStoreId(), "", ""));

        xianshiCardView.setOnClickListener(view -> BaseApplication.get().startStoreGoodsList(getActivity(), storeInfoBean.getStoreId(), "", ""));

    }

    //???????????????

    private void getPromotion() {

        StoreModel.get().storeSale(storeInfoBean.getStoreId(), new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "sale");
                data = data.replace("[]", "null");
                if (data.equals("null")) {
                    mansongCardView.setVisibility(View.GONE);
                    xianshiCardView.setVisibility(View.GONE);
                    activityTextView.setVisibility(View.VISIBLE);
                } else {
                    mansongCardView.setVisibility(View.VISIBLE);
                    xianshiCardView.setVisibility(View.VISIBLE);
                    activityTextView.setVisibility(View.GONE);
                    storeActivityBean = JsonUtil.json2Bean(data, StoreActivityBean.class);
                    if (storeActivityBean.getMansong() == null) {
                        mansongCardView.setVisibility(View.GONE);
                    } else {
                        mansongCardView.setVisibility(View.VISIBLE);
                        manSongNameTextView.setText(storeActivityBean.getMansong().getMansongName());
                        manSongTimeTextView.setText("???????????????");
                        manSongTimeTextView.append(storeActivityBean.getMansong().getStartTimeText());
                        manSongTimeTextView.append(" ??? ");
                        manSongTimeTextView.append(storeActivityBean.getMansong().getEndTimeText());
                        manSongDescTextView.setText("????????????????????????");
                        manSongDescTextView.append(storeActivityBean.getMansong().getRules().get(0).getPrice());
                        manSongDescTextView.append("????????????");
                        manSongDescTextView.append(storeActivityBean.getMansong().getRules().get(0).getDiscount());
                        manSongDescTextView.append("????????????????????????");
                        manSongDescTextView.append(storeActivityBean.getMansong().getRules().get(0).getMansongGoodsName());
                        manSongRemarkTextView.setText("???????????????");
                        manSongRemarkTextView.append(storeActivityBean.getMansong().getRemark());
                    }
                    if (storeActivityBean.getXianshi() == null) {
                        xianshiCardView.setVisibility(View.GONE);
                    } else {
                        xianshiCardView.setVisibility(View.VISIBLE);
                        xianshiNameTextView.setText(storeActivityBean.getXianshi().getXianshiName());
                        xianshiTimeTextView.setText("???????????????");
                        xianshiTimeTextView.append(storeActivityBean.getXianshi().getStartTimeText());
                        xianshiTimeTextView.append(" ??? ");
                        xianshiTimeTextView.append(storeActivityBean.getXianshi().getEndTimeText());
                        xianshiDescTextView.setText("????????????????????? ");
                        xianshiDescTextView.append(storeActivityBean.getXianshi().getLowerLimit());
                        xianshiDescTextView.append(" ???????????????????????????");
                        xianshiRemarkTextView.setText("???????????????");
                        xianshiRemarkTextView.append(storeActivityBean.getXianshi().getXianshiExplain());
                    }
                }
            }

            @Override
            public void onFailure(String reason) {
                new BaseCountTime(BaseConstant.TIME_COUNT, BaseConstant.TIME_TICK) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getPromotion();
                    }
                }.start();
            }
        });

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onStoreBeanEvent(StoreBeanEvent event) {

        String data = JsonUtil.getDatasString(event.getBaseBean().getDatas(), "store_info");
        storeInfoBean = JsonUtil.json2Bean(data, StoreInfoBean.class);
        getPromotion();

    }

}
