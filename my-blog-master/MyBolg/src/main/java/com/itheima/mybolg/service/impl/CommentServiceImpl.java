package com.itheima.mybolg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mybolg.dao.CommentMapper;
import com.itheima.mybolg.dao.StatisticMapper;
import com.itheima.mybolg.model.domain.Comment;
import com.itheima.mybolg.model.domain.Statistic;
import com.itheima.mybolg.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    // 根据文章id分页查询评论
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;
    }

    @Autowired
    private StatisticMapper statisticMapper;

    // 用户发表评论
    @Override
    public void pushComment(Comment comment){
        commentMapper.pushComment(comment);
        // 更新文章评论数据量
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }

    @Override
    public PageInfo<Comment> selectCommentWithPage(Integer page, Integer count) {
        //第一页page= 1
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage1();
        // 封装文章统计数据
//        for (int i = 0; i < commentList.size(); i++) {
//            Comment comment = commentList.get(i);
//            CommentStatistic commentStatistic = commentStatisticMapper.selectStatisticWithCommentId(comment.getId());
//            comment.setHits(commentStatistic.getCommentHits());
//        }
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        return pageInfo;
    }

//    @Autowired
//    private RedisTemplate redisTemplate;

    // 删除文章
    @Override
    public void deleteCommentWithId(int id) {

        // 删除文章的同时，删除对应的缓存
        commentMapper.deleteCommentWithId(id);
//        redisTemplate.delete("comment_" + id);

    }

}
