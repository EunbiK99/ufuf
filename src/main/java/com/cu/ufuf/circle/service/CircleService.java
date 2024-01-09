package com.cu.ufuf.circle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.circle.mapper.CircleSqlMapper;

@Service
public class CircleService {

    @Autowired
    private CircleSqlMapper circleSqlMapper;


}
