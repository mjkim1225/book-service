package mj.kakao.bookservice.controller;

import mj.kakao.bookservice.model.entity.BookResultEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mj.kakao.bookservice.model.dto.ResultDTO;
import mj.kakao.bookservice.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/book")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ResultDTO>> retrieveBookList(@RequestParam(name = "query", required = true) String query) {
        return ResponseEntity.ok().body(service.retrieveBookList(query));
    }
    
}
