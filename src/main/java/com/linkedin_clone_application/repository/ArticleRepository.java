package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Article;
import com.linkedin_clone_application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> getArticlesByUserId(int id);


}