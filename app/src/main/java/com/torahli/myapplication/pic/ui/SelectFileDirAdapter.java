package com.torahli.myapplication.pic.ui;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.torahli.myapplication.R;

import java.io.File;

public class SelectFileDirAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    public SelectFileDirAdapter() {
        super(R.layout.item_game_text);

    }

    @Override
    protected void convert(BaseViewHolder helper, File item) {
        TextView fileType = helper.getView(R.id.tv_file_type);
        TextView fileName = helper.getView(R.id.tv_file_name);

        if (helper.getLayoutPosition() == 0) {
            fileType.setText("返回");
            fileName.setText("上级文件夹");
            return;
        }

        String name = item.getName();
        fileName.setText(name);
        if (item.isDirectory()) {
            fileType.setText("文件夹");
        } else {
            if (name.startsWith("\\.")) {
                fileType.setText("隐藏");
            } else {
                String[] split = name.split("\\.");

                if (split != null && split.length > 1) {
                    fileType.setText(split[split.length - 1]);
                } else {
                    fileType.setText("未知");
                }
            }
        }
    }
}
