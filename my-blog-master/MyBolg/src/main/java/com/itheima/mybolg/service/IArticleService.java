package com.itheima.mybolg.service;

import java.util.List;

import com.github.pagehelper.PageInfo;


import com.itheima.mybolg.model.domain.Article;

public interface IArticleService {
    // 分页查询文章列表,第一页page=1
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count);
    // 统计前10的热度文章信息
    public List<Article> getHeatArticles();

    // 根据文章id查询单个文章详情
    public Article selectArticleWithId(Integer id);

    //修改文章
    public void updateArticleWithId(Article article);


    //删除文章
    // 根据主键删除文章
    public void deleteArticleWithId(int id);

    // 根据文章标题搜索文章
    public List<Article> selectArticleByTitle(String title);

    // 发布文章
    public void publish(Article article);


}
