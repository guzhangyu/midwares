package com.unitymob;


import com.unitymob.mapper.AMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class TransactionTest {

    @Autowired
    AMapper aMapper;

    @Transactional
    public void test(){
        aMapper.insert("a");
        aMapper.insert(Arrays.asList("b"));
        System.out.println("s");
        //throw new IllegalArgumentException("aa");
    }
}
