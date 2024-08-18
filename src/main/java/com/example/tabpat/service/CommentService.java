package com.example.tabpat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tabpat.domain.CommentDo;
import com.example.tabpat.dto.CommentDto;
import com.example.tabpat.form.CommentForm;
import com.example.tabpat.query.CommentQuery;
import com.example.tabpat.util.BeanCopierUtil;
import com.example.tabpat.util.PrimaryKeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService extends BaseService {
    /**
     * 查询所有评论
     *
     * @param commentQuery 搜索用
     */
    @Transactional
    public Result list(CommentQuery commentQuery) throws ServiceException {
        try {
            QueryWrapper<CommentDo> wrapper = new QueryWrapper<>();
            wrapper
                    .isNull("root_comment_id")
                    .or()
                    .eq("root_comment_id","");
            return getResult(commentQuery, wrapper);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 获取回复评论
     * @param commentQuery
     * @return
     * @throws ServiceException
     */

    @Transactional
    public Result getReply(CommentQuery commentQuery, String rootCommentId) throws ServiceException {
        try {
            QueryWrapper<CommentDo> wrapper = new QueryWrapper<>();
            wrapper.eq("root_comment_id", rootCommentId);
            return getResult(commentQuery, wrapper);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private Result getResult(CommentQuery commentQuery, QueryWrapper<CommentDo> wrapper) {
        PageHelper.startPage(commentQuery.getPageNum(), commentQuery.getPageSize());
        List<CommentDo> commentDoList = commentDao.selectList(wrapper);
        PageInfo<CommentDo> pageInfo = new PageInfo<>(commentDoList);
        List<CommentDto> commentDtoList = new ArrayList<>();
        if (commentDoList.isEmpty()) {
            return Result.success(200, "获取成功", commentDtoList);
        }

        for (CommentDo commentDo : commentDoList) {
            CommentDto commentDto = listShowDto(commentDo);
            commentDtoList.add(commentDto);
        }
        PageInfo<CommentDto> commentDtoPageInfo = new PageInfo<>(commentDtoList);
        commentDtoPageInfo.setTotal(pageInfo.getTotal());
        return Result.success(200, "获取成功", commentDtoPageInfo);
    }

    private CommentDto listShowDto(CommentDo commentDo) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(commentDo.getCommentId());
        commentDto.setContent(commentDo.getContent());
        commentDto.setCreateTime(commentDo.getCreateTime());
        commentDto.setIsDelete(commentDo.getIsDelete());
        commentDto.setUsername(commentDo.getUsername());
        commentDto.setArticleId(commentDo.getArticleId());
        commentDto.setCommentLikeCount(commentDo.getCommentLikeCount());
        commentDto.setRootCommentId(commentDo.getRootCommentId());
        commentDto.setToCommentId(commentDo.getToCommentId());
        return commentDto;
    }
    /**
     * 评论保存方法
     *
     * @param commentForm 博客评论body体
     */
    @Transactional
    public Result save(CommentForm commentForm) throws ServiceException {
        try {
            CommentDo commentDo = buildCommentSave(commentForm);
            commentDao.insert(commentDo);
            return Result.success(200, "评论保存成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private CommentDo buildCommentSave(CommentForm commentForm) {
        CommentDo commentDo = BeanCopierUtil.create(commentForm, CommentDo.class);
        String commentId = PrimaryKeyUtil.get();
        commentDo.setCommentId(commentId);
        commentDo.setContent(commentForm.getContent());
        commentDo.setCreateTime(System.currentTimeMillis());
        commentDo.setIsDelete(false);
        commentDo.setUsername(commentForm.getUsername());
        commentDo.setArticleId(commentForm.getArticleId());
        commentDo.setCommentLikeCount(0);
        commentDo.setRootCommentId(commentForm.getRootCommentId());
        commentDo.setToCommentId(commentForm.getToCommentId());
        return commentDo;
    }

    /**
     * 评论删除方法
     */
    @Transactional
    public Result delete(String commentId) throws ServiceException {
        try {
            CommentDo commentDo = commentDao.selectById(commentId);
            commentDo.setIsDelete(true);
            commentDao.updateById(commentDo);
            return Result.success(200, "删除成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
