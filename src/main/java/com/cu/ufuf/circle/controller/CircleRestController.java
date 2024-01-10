package com.cu.ufuf.circle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.circle.service.CircleService;

@RestController
@RequestMapping("/circle")
public class CircleRestController {
    
    @Autowired
    private CircleService circleService;
}
