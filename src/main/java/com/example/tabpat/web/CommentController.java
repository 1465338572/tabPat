package com.example.tabpat.web;

import com.example.tabpat.annotation.UnderlineToCamel;
import com.example.tabpat.code.HttpStatusCode;
import com.example.tabpat.form.CommentForm;
import com.example.tabpat.query.CommentQuery;
import com.example.tabpat.service.CommentService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 查询评论
     *
     * @param commentQuery
     * @param response
     * @return
     */
    @GetMapping(value = "/public/commentList")
    @ResponseBody
    public Result list(@UnderlineToCamel CommentQuery commentQuery, HttpServletResponse response) {
        Result result;
        try {
            result = commentService.list(commentQuery);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());

        }
        return result;
    }

    @GetMapping(value = "/public/commentList/{root_comment_id}")
    @ResponseBody
    public Result getReply(@UnderlineToCamel CommentQuery commentQuery, @PathVariable("root_comment_id") String rootCommentId, HttpServletResponse response) {
        Result result;
        try {
            result = commentService.getReply(commentQuery,rootCommentId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());

        }
        return result;
    }

    /**
     * 添加评论
     */
    @PostMapping(value = "/public/addComment")
    @ResponseBody
    public Result save(@RequestBody CommentForm commentForm, HttpServletResponse response) {
        Result result;
        try {
            result = commentService.save(commentForm);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 删除评论
     */
    @PutMapping(value = "/public/deleteComment/{comment_id}")
    @ResponseBody
    public Result delete(@PathVariable("comment_id") String commentId, HttpServletResponse response) {
        Result result;
        try {
            result = commentService.delete(commentId);
            if (result.getCode() != 200) {
                response.setStatus(HttpStatusCode.SERVICEERROR);
                return result;
            }
        } catch (ServiceException e) {
            response.setStatus(HttpStatusCode.SERVICEERROR);
            result = Result.failure(HttpStatusCode.SERVICEERROR, e.getMessage());
        }
        return result;
    }

}
