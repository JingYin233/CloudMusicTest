package net.fkm.cloudmusictest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.fkm.cloudmusictest.R;
import net.fkm.cloudmusictest.activity.MainActivity;
import net.fkm.cloudmusictest.activity.MyActivity;
import net.fkm.cloudmusictest.database.DatabaseHelper;
import net.fkm.cloudmusictest.model.MusicModel;

import java.util.List;

/**
 * 显示音乐列表
 * 通过MusicModel对象展示音乐的封面图片，名称，作者和备注
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context mContext;
    private List<MusicModel> mList;
    private OnItemClikListener mOnItemClikListener;
    private DatabaseHelper dbHelper;
    private Activity activity;

    private boolean isSelect;

    public MusicAdapter(Context context, List<MusicModel> mList) {
        this.mContext = context;
        this.mList = mList;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void setActivityType(Activity activity) {
        this.activity = activity;
    }

    /**
     * 创建新的ViewHolder对象
     * 从item_music_view.xml加载视图，再用view对象创建ViewHolder对象
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_music_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * 将MusicModel对象的数据绑定到ViewHolder的视图上
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MusicModel data = mList.get(position);
        data = mList.get(position);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(mContext).load(data.getPoster()).apply(options).into(holder.iv_cover);
        holder.tv_name.setText(data.getName());
        holder.tv_author.setText(String.format(" - %s", data.getAuthor()));
        holder.tv_remark.setText(data.getRemark());

        holder.cb_like.setImageResource(dbHelper.isMusicLiked(data.getMusicId()) ? R.drawable.heart : R.drawable.heart_null);
        MusicModel finalData = data;
        holder.cb_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cb_like.getDrawable().getConstantState() == mContext.getResources().getDrawable(R.drawable.heart_null).getConstantState()){
                    // 图像是heart_null
                    dbHelper.insertMusic(finalData);
                    holder.cb_like.setImageResource(R.drawable.heart);
                    holder.cb_like.invalidate();
                } else if (holder.cb_like.getDrawable().getConstantState() == mContext.getResources().getDrawable(R.drawable.heart).getConstantState()){
                    // 图像是heart
                    dbHelper.deleteMusic(finalData.getMusicId());
                    holder.cb_like.setImageResource(R.drawable.heart_null);
                    holder.cb_like.invalidate();
                    if (mContext instanceof MainActivity) {
                        // 视图在MainActivity中
                    } else {
                        // 视图不在MainActivity中
                        Activity activity = (Activity) mContext;
                        activity.recreate();
                    }
                }
            }
        });

        if (mOnItemClikListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemClik(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemLongClik(holder.itemView, pos);
                    return true;
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 用于缓存音乐项视图中的各个组件
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_cover;
        private TextView tv_name;
        private TextView tv_author;
        private TextView tv_remark;
        private ImageView cb_like;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_cover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);
            cb_like = (ImageView) itemView.findViewById(R.id.cb_like);
        }
    }

    /**
     * 接口，定义了两个方法
     * onItemClick 当音乐项被单击时被调用
     * OnItemLongClick 当音乐项长按时被调用
     */
    public interface OnItemClikListener {
        void onItemClik(View view, int position);

        void onItemLongClik(View view, int position);
    }

    /**
     * 设置音乐点击事件监听器
     * @param mOnItemClikListener
     */
    public void setItemClikListener(OnItemClikListener mOnItemClikListener) {
        this.mOnItemClikListener = mOnItemClikListener;
    }

}
