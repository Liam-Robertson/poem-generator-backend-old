package com.syllabary.poemgenerator.poem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "poemData")
public class PoemController {

    private PoemService poemService;

    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    @GetMapping("getAll")
    public List<Poem> getAll() throws IOException {
        return poemService.getAllPoemData("classpath*:poemDir/*.xml");
    }
}