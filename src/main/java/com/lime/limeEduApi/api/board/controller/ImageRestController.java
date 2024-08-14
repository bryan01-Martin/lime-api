package com.lime.limeEduApi.api.board.controller;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lime.limeEduApi.api.board.domain.BoardContentListReq;
import com.lime.limeEduApi.api.board.dto.ContentDto;
import com.lime.limeEduApi.api.board.service.ContentService;
import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.annotaion.User;
import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.controller.BaseController;
import com.lime.limeEduApi.framework.common.domain.ResponseDomain;
import com.lime.limeEduApi.framework.common.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(Const.API.BOARD_IMAGE)
@RequiredArgsConstructor
@Api(tags = {"게시판 API"}) //swagger 최상단 Controller 명칭 
public class ImageRestController extends BaseController{

	private final ContentService contentService;
	private final MessageSource messageSource;
	
	@PostMapping("/selectContentList")
	@ApiOperation(value = "게시판 목록 조회", notes = "게시판 목록 조회")
	public ResponseDomain<ContentDto>selectContentList(@RequestBody BoardContentListReq boardContentListReq) {
		ContentDto contentDto = new ContentDto();
		contentDto.setBoardSeq(boardContentListReq.getBoardSeq());
		return ResponseDomain.<ContentDto>builder()
				.list(contentService.selectContentList(boardContentListReq))
				.data(contentDto)
				.totalCnt(contentService.getContentListTotalCnt(boardContentListReq))
				.code(ResponseCode.SEARCH_SUCCESS.getCode())
				.message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
				.build();
	}
	
	 @PostMapping("/createContent")
	 @ApiOperation(value = "게시글 작성", notes = "게시글 작성")
	    public ResponseDomain<Integer> createContent(ContentDto contentDto, MultipartFile file, @User@ApiIgnore UserDto userDto) throws Exception {
		 	contentDto.setRegSeq(userDto.getSeq());
		 	contentDto.setModSeq(userDto.getSeq());
		 	int result = contentService.createContent(contentDto, file);
	        return ResponseDomain.<Integer>builder()
	                    .data(result)
	                    .code(ResponseCode.INSERT_SUCCESS.getCode())
	                    .message(messageSource.getMessage(ResponseCode.INSERT_SUCCESS.getKey(), null, CommonUtil.getLocale()))
	                .build();
	    }
	 
	@PostMapping("/modContent")
    @ApiOperation(value = "게시글 수정", notes = "게시글 수정")
    public ResponseDomain<Integer> modContent(ContentDto contentDto, MultipartFile file, @User@ApiIgnore UserDto userDto) throws Exception{
		contentDto.setModSeq(userDto.getSeq());
		contentDto.setModNm(userDto.getName());
        int result = contentService.modContent(contentDto, file);
        if(result == 0){
            return ResponseDomain.<Integer>builder()
                            .data(contentDto.getSeq())
                            .code(ResponseCode.UPDATE_SUCCESS.getCode())
                            .message(messageSource.getMessage(ResponseCode.UPDATE_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                            .build();
        } else {
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
    }
	
	@PostMapping("/getContentInfo")
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글 상세 조회")
    public ResponseDomain<ContentDto> getContentInfo(@RequestBody ContentDto contentDto){
		int seq = contentDto.getSeq();
        return ResponseDomain.<ContentDto>builder()
                    .data(contentService.getContentInfo(seq))
                    .code(ResponseCode.SEARCH_SUCCESS.getCode())
                    .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }
}
