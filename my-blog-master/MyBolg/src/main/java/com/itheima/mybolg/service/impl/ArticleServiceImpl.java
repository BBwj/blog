package com.itheima.mybolg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itheima.mybolg.dao.ArticleMapper;
import com.itheima.mybolg.dao.CommentMapper;
import com.itheima.mybolg.dao.StatisticMapper;
import com.itheima.mybolg.model.domain.Article;
import com.itheima.mybolg.model.domain.Statistic;
import com.itheima.mybolg.service.IArticleService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    // 分页查询文章列表
    @Override
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count) {
        //第一页page=1
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticleWithPage();
        // 封装文章统计数据
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo=new PageInfo<>(articleList);
        return pageInfo;
    }

    // 统计前10的热度文章信息
    @Override
    public List<Article> getHeatArticles( ) {
        List<Statistic> list = statisticMapper.getStatistic();
        List<Article> articlelist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Article article = articleMapper.selectArticleWithId(list.get(i).getArticleId());
            article.setHits(list.get(i).getHits());
            article.setCommentsNum(list.get(i).getCommentsNum());
            articlelist.add(article);
            if(i>=9){
                break;
            }
        }
        return articlelist;
    }


//    @Autowired
//    private RedisTemplate redisTemplate;

    // 根据id查询单个文章详情，并使用Redis进行缓存管理
    public Article selectArticleWithId(Integer id){
        Article article = null;
//        Object o = redisTemplate.opsForValue().get("article_" + id);
//        if(o!=null){
//            article=(Article)o;
//        }else{
            article = articleMapper.selectArticleWithId(id);
//            if(article!=null){
//                redisTemplate.opsForValue().set("article_" + id,article);
//            }
//        }
        return article;
    }


    // 发布文章
    @Override
    public void publish(Article article) {
        // 去除表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setCreated(new Date());
        article.setHits(0);
        article.setCommentsNum(0);
        // 插入文章，同时插入文章统计数据
        articleMapper.publishArticle(article);
        statisticMapper.addStatistic(article);
    }


    // 更新文章
    @Override
    public void updateArticleWithId(Article article) {
        article.setModified(new Date());
        articleMapper.updateArticleWithId(article);
//        redisTemplate.delete("article_" + article.getId());
    }

    @Autowired
    private CommentMapper commentMapper;

    // 删除文章
    @Override
    public void deleteArticleWithId(int id) {

        // 删除文章的同时，删除对应的缓存
        articleMapper.deleteArticleWithId(id);
//        redisTemplate.delete("article_" + id);

        // 同时删除对应文章的统计数据
        statisticMapper.deleteStatisticWithId(id);

        // 同时删除对应文章的评论数据
        commentMapper.deleteCommentWithId(id);

    }

    @Override
    public List<Article> selectArticleByTitle(String title) {
        List<Article> list = articleMapper.selectArticleByTitle(title);
        for (Article article : list) {
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
        }
        return list;
    }


}