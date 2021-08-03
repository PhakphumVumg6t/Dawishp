package top.yokey.shopwt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.base.BaseViewHolder;
import top.yokey.base.bean.AreaBean;
import top.yokey.shopwt.R;

/**
 * 适配器
 *
 * @author MapleStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/Shopwt-Android
 */

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.ViewHolder> {

    private final ArrayList<AreaBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public AreaListAdapter(ArrayList<AreaBean> arrayList) {
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
        final AreaBean bean = arrayList.get(position);

        holder.titleTextView.setText(bean.getAreaName());

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_area, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, AreaBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.titleTextView)
        private AppCompatTextView titleTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
