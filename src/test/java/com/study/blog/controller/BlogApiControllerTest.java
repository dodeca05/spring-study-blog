package com.study.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.blog.domain.Article;
import com.study.blog.dto.AddArticleRequest;
import com.study.blog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.transform.Result;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    BlogRepository blogRepository;
    @BeforeEach
    public void mockMvcSetUp(){
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다")
    @Test
    public void addArticle() throws Exception{
        final String url="/api/articles";
        final String title = "title";
        final String content="content";
        final AddArticleRequest userRequest=new AddArticleRequest(title,content);

        final String requestBody=objectMapper.writeValueAsString(userRequest);

        ResultActions result=mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articles=blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception{
        final String url="/api/articles";
        final String title="title";
        final String content="content";

        blogRepository.save(Article.builder().title(title).content(content).build());

        final ResultActions resultActions=mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE));

        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$[0].content").value(content)).andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다")
    @Test
    public void findArticle() throws Exception{
        final String url="/api/articles/{id}";
        final String title="title";
        final String content="content";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        final ResultActions resultActions=mockMvc.perform(get(url,savedArticle.getId()));

        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.content").value(content)).andExpect(jsonPath("$.title").value(title));
    }
    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

}