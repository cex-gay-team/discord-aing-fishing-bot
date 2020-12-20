package com.cex.api.controller;

import com.cex.api.mapper.BaitsMapper;
import com.cex.api.model.BaitsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FishingInformationController {
    @Autowired
    private BaitsMapper baitsMapper;

    @GetMapping("/test")
    @ResponseBody
    public List<BaitsModel> test() {
        return baitsMapper.selectBaits();
    }

    @GetMapping("/page")
    public String page() {
        return "hello";
    }
}
