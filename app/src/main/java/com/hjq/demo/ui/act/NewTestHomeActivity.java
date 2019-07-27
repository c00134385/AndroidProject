package com.hjq.demo.ui.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.TestManager;
import com.hjq.demo.model.TestItemModel;
import com.hjq.demo.ui.act.t.BacklightTestActivity;
import com.hjq.demo.ui.act.t.BaseTestActivity;
import com.hjq.demo.ui.act.t.BtTestActivity;
import com.hjq.demo.ui.act.t.CameraTestActivity;
import com.hjq.demo.ui.act.t.DemoTestActivity;
import com.hjq.demo.ui.act.t.KeyTestActivity;
import com.hjq.demo.ui.act.t.ScreenTestActivity;
import com.hjq.demo.ui.act.t.SpeakerTestActivity;
import com.hjq.demo.ui.act.t.TouchTestActivity;
import com.hjq.demo.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class NewTestHomeActivity extends MyActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<TestItemModel> list = new ArrayList<>();
    RecyclerDemoAdapter recycleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_home_new;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
//设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
//        layoutManager.setOrientation(OrientationHelper.VERTICAL);
//设置Adapter
        recycleAdapter = new RecyclerDemoAdapter(this, list);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
//        recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
//设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
    }

    class RecyclerDemoAdapter extends RecyclerView.Adapter<RecyclerDemoAdapter.MyHolder> {

        Context context;
        List<TestItemModel> list;

        public RecyclerDemoAdapter(Context context, List<TestItemModel> list) {
            this.context = context;
            this.list = list;
        }

        public void update(List<TestItemModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_card_view, viewGroup, false);
            MyHolder holder = new MyHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            final TestItemModel s = list.get(i);
            myHolder.tvTitle.setText(s.getTitle().value());
            myHolder.tvState.setText(s.getState().value());
            switch (s.getState()) {
                case SUCCESS:
                    myHolder.itemView.setBackgroundResource(R.drawable.content_bg1);
                    break;
                case FAILED:
                    myHolder.itemView.setBackgroundResource(R.drawable.content_bg2);
                    break;
                case TESTING:
                case NOT_TEST:
                default:
                    myHolder.itemView.setBackgroundResource(R.drawable.content_bg);
                    break;
            }

//            myHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Timber.d("textView onClick");
//                }
//            });

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.d("itemView onClick");
                    switch (s.getTitle()) {
                        case SCREEN:
                            startActivity(ScreenTestActivity.class, s);
                            break;
                        case TOUCH:
                            startActivity(TouchTestActivity.class, s);
                            break;
                        case SPEAKER:
                            startActivity(SpeakerTestActivity.class, s);
                            break;
                        case KEY:
                            startActivity(KeyTestActivity.class, s);
                            break;
                        case BACK_LIGHT:
                            startActivity(BacklightTestActivity.class, s);
                            break;
                        case CAMERA:
                            startActivity(CameraTestActivity.class, s);
                            break;
                        case BLUETOOTH:
                            startActivity(BtTestActivity.class, s);
                            break;
                        case WIFI:
                        case LAN:
                        case FORG:
                        default:
                            startActivity(DemoTestActivity.class, s);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(null == list) {
                return 0;
            }
            return list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView tvTitle;
            TextView tvState;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvState = itemView.findViewById(R.id.tv_state);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.just(TestManager.getInstance().getTestItems())
                .doOnNext(new Consumer<List<TestItemModel>>() {
                    @Override
                    public void accept(List<TestItemModel> testItemModels) throws Exception {
                        list = TestManager.getInstance().getTestItems();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TestItemModel>>() {
                    @Override
                    public void accept(List<TestItemModel> testItemModels) throws Exception {
                        recycleAdapter.update(list);
                    }
                });
    }

    public void startActivity(Class<? extends Activity> cls, TestItemModel s) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(BaseTestActivity.KEY_TEST_ITEM, GsonUtil.getGson().toJson(s));
        startActivity(intent);
    }
}
