package com.udabe.controller;


import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.cmmn.base.PageParam;
import com.udabe.entity.News;
import com.udabe.service.impl.NewsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/news")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NewsController extends BaseCrudController<News, Long> {

    private final NewsServiceImpl newsService;

    public NewsController(NewsServiceImpl newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/listAllNews")
    public ResponseEntity<?> listAllNews(News entity, PageParam pageParam){

            return newsService.listAllNews(entity, pageParam.of());
    }


    @GetMapping("/getSingleNews/{newsId}")
    public ResponseEntity<?> getSingleNews(@PathVariable("newsId") Long newsId){

        return newsService.getSingleNews(newsId);
    }


    @PostMapping("/addNews")
    public ResponseEntity<?> addNews(@RequestBody News news){

        return newsService.addNews(news);
    }


    @PutMapping("/updatedNews/{newsId}")
    public ResponseEntity<?> updatedNews(@PathVariable("newsId") Long newsId, @RequestBody News news){

        return newsService.updatedNews(newsId, news);
    }


    @GetMapping("/checkUnDisplayNews/{userId}")
    public ResponseEntity<?> checkUnDisplayNews(@PathVariable("userId") Long userId){

        return newsService.checkUnDisplayNews(userId);
    }


    @DeleteMapping("/deleteNews/{newsId}")
    public ResponseEntity<?> deleteNews(@PathVariable("newsId") Long newsId){

        return newsService.deleteNews(newsId);
    }
}
