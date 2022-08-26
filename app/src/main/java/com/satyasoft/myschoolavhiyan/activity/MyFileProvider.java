package com.satyasoft.myschoolavhiyan.activity;

import androidx.core.content.FileProvider;
import com.satyasoft.myschoolavhiyan.R;
public class MyFileProvider extends FileProvider {
    public MyFileProvider() {
        super(R.xml.file_paths);
    }
}