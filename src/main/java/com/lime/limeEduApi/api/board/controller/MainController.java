package com.lime.limeEduApi.api.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lime.limeEduApi.framework.common.view.DownloadView;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final DownloadView downloadView;
	
    @RequestMapping(value={"", "/"})
    public String main(){
        return "redirect:/board/contentList/1";
    }
    
    @RequestMapping("/api/download/{seq}")
    public ModelAndView download(@PathVariable(name = "seq") int seq){
        return new ModelAndView(downloadView, "seq", seq);
    }
}