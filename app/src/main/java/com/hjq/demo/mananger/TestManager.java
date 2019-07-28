package com.hjq.demo.mananger;

import android.content.Context;
import android.content.SharedPreferences;

import com.hjq.demo.model.StateEnum;
import com.hjq.demo.model.TestEnum;
import com.hjq.demo.model.TestItemModel;
import com.hjq.demo.utils.SPUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class TestManager {
    private static TestManager ourInstance;
    private Context context;

    public static final String mTAG = "test";
    // 创建一个写入器
    private static SharedPreferences mPreferences;
    //    private List<TestItemModel> testItems;
    private Map<String, TestItemModel> testItemMap;

    public static TestManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("TestManager not instanced.");
        }
        return ourInstance;
    }

    private TestManager(Context context) {
        this.context = context;

//
//        for (StateEnum e : StateEnum.values()) {
//            Timber.d("%s", e.name());
//        }
        mPreferences = context.getSharedPreferences(mTAG, Context.MODE_PRIVATE);

        testItemMap = new LinkedHashMap<>();
//        testItems = new ArrayList<>();
        for (TestEnum e : TestEnum.values()) {
            Timber.d("%s", e.name());
            String title = e.name();
            String state = (String) SPUtils.getParam(mPreferences, title, StateEnum.NOT_TEST.name());
//            testItems.add(new TestItemModel(TestEnum.valueOf(title), StateEnum.valueOf(state)));
            testItemMap.put(title, new TestItemModel(TestEnum.valueOf(title), StateEnum.valueOf(state)));
        }
    }

    public static void init(Context context) {
        ourInstance = new TestManager(context);
    }

    public List<TestItemModel> getTestItems() {
        return new ArrayList<TestItemModel>(testItemMap.values());
    }

    public void updateTest(TestItemModel item) {
        testItemMap.put(item.getTitle().name(), item);
        SPUtils.setParam(mPreferences, item.getTitle().name(), item.getState().name());
    }

    public static SharedPreferences getmPreferences() {
        return mPreferences;
    }
}
