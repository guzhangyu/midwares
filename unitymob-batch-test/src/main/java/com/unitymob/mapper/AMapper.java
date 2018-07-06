package com.unitymob.mapper;

import com.unitymob.midware.mybatis.Batch;

import java.util.List;

public interface AMapper {
    @Batch
    void insert(List<String>list);

    void insert(String a);
}
