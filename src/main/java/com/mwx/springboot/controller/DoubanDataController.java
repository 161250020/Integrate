package com.mwx.springboot.controller;


import com.mwx.springboot.entity.PageBean;
import com.mwx.springboot.service.DoubanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/douban")
public class DoubanDataController {

    @Autowired
    private DoubanService doubanService;

    @RequestMapping("/findDouBanDataByConPage")
    public PageBean findByConPage(
            @RequestParam(value = "pageCode", required = false) int pageCode,
            @RequestParam(value = "pageSize", required = false) int pageSize){
        System.out.println("按页查询: "+ pageCode+" "+pageSize);

        return doubanService.findDouBanDataByConPage(pageCode,pageSize);
    }



}