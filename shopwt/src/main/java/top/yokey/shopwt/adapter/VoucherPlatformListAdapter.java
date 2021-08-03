package top.yokey.shopwt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.VoucherPlatformBean;
import top.yokey.shopwt.R;
import top.yokey.shopwt.base.BaseImageLoader;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class VoucherPlatformListAdapter extends RecyclerView.Adapter<VoucherPlatformListAdapter.ViewHolder> {

    private final ArrayList<VoucherPlatformBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public VoucherPlatformListAdapter(ArrayList<VoucherPlatformBean> arrayList) {
        this.arrayList = arrayList;
        this.onItemClickListener = null;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int positionInt = position;
        final VoucherPlatformBean bean = arrayList.get(position);

        BaseImageLoader.get().display(bean.getCouponTCustomimgUrl(), holder.mainImageView);
        holder.titleTextView.setText(bean.getCouponTTitle());
        holder.timeTextView.setText(bean.getEndDate());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getCouponTPrice());
        holder.progressTextView.setText("已领取：");
        holder.progressTextView.append(bean.getCouponTProgress() + "%");

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_voucher_platform, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, VoucherPlatformBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.titleTextView)
        private AppCompatTextView titleTextView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.progressTextView)
        private AppCompatTextView progressTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
