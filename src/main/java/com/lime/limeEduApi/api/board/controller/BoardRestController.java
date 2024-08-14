package com.lime.limeEduApi.api.board.controller;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lime.limeEduApi.api.board.dto.BoardDto;
import com.lime.limeEduApi.api.board.service.BoardService;
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
@RequestMapping(Const.API.BOARD)
@RequiredArgsConstructor
@Api(tags = {"게시판 API"}) //swagger 최상단 Controller 명칭 
public class BoardRestController extends BaseController{

	private final BoardService boardService;
	private final MessageSource messageSource;
	
	@PostMapping("/getBoardList")
	@ApiOperation(value = "게시판 리스트 조회", notes = "게시판 리스트 조회")
	public ResponseDomain<BoardDto>getBoardList(@RequestBody BoardDto boardDto) throws Exception {
		return ResponseDomain.<BoardDto>builder()
					.list(boardService.getBoardList(boardDto))
					.code(ResponseCode.SEARCH_SUCCESS.getCode())
					.message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
				.build();
	}
	
	@PostMapping("/getBoardInfo")
	@ApiOperation(value = "게시판 상세 조회", notes = "게시판 상세 조회")
	public ResponseDomain<BoardDto> getBoardInfo(@RequestBody BoardDto boardDto) {
		int seq = boardDto.getSeq();
		return ResponseDomain.<BoardDto>builder()
					.data(boardService.getBoardInfo(seq))
					.code(ResponseCode.SEARCH_SUCCESS.getCode())
					.message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
				.build();
	}
	
	 @PostMapping("/createBoard")
	 @ApiOperation(value = "게시판 작성", notes = "게시판 작성")
	    public ResponseDomain<Integer> createBoard(@RequestBody BoardDto boardDto, @User@ApiIgnore UserDto userDto) throws Exception {
		 	boardDto.setRegSeq(userDto.getSeq());
		 	boardDto.setModSeq(userDto.getSeq());
		 	int result = boardService.createBoard(boardDto);
	        return ResponseDomain.<Integer>builder()
	                    .data(result)
	                    .code(ResponseCode.INSERT_SUCCESS.getCode())
	                    .message(messageSource.getMessage(ResponseCode.INSERT_SUCCESS.getKey(), null, CommonUtil.getLocale()))
	                .build();
	    }
	 
	@PostMapping("/modBoard")
    @ApiOperation(value = "게시판 수정", notes = "게시판 수정")
    public ResponseDomain<Integer> modBoard(BoardDto boardDto, @User@ApiIgnore UserDto userDto) throws Exception{
		boardDto.setModSeq(userDto.getSeq());
		boardDto.setModNm(userDto.getName());
        int result = boardService.modBoard(boardDto);
        if(result == 0){
            return ResponseDomain.<Integer>builder()
                            .data(boardDto.getSeq())
                            .code(ResponseCode.UPDATE_SUCCESS.getCode())
                            .message(messageSource.getMessage(ResponseCode.UPDATE_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                            .build();
        } else {
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
    }
}
