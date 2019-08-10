package com.leyou.search.web;

import com.leyou.common.vo.PageResult;
import com.leyou.common.vo.SearchRequest;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author javie
 * @date 2019/5/23 14:41
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<PageResult> search(@RequestBody SearchRequest searchRequest){
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(searchRequest.getKey());
        PageResult<Goods> pageResult = searchService.search(searchRequest);
        if (pageResult==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(pageResult);
    }
    @GetMapping("page")
    public ResponseEntity<SearchResult> page(@RequestParam("key") String key,@RequestParam("page") Integer page){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKey(key);
        searchRequest.setPage(page);
        SearchResult searchResult = searchService.search(searchRequest);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(searchRequest);
        return ResponseEntity.ok(searchResult);
    }
}
