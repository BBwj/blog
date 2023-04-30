package com.itheima.mybolg.service;

import com.github.pagehelper.PageInfo;
import com.itheima.mybolg.model.domain.Comment;

//文章评论业务处理接口
//文章评论业务处理接口
public interface ICommentService {

    // 获取文章下的评论
    public PageInfo<Comment> getComments(Integer aid, int page, int count);

    //用户发表评论
    public void pushComment(Comment comment);

    /**
     * 分页查询评论
     * @param page
     * @param count
     * @return
     */
    public PageInfo<Comment> selectCommentWithPage(Integer page, Integer count);


    //删除文章
    // 根据主键删除文章
    public void deleteCommentWithId(int id);
}