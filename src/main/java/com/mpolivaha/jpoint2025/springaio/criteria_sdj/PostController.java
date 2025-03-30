package com.mpolivaha.jpoint2025.springaio.criteria_sdj;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/post/v1")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping
	public Page<Post> find(PostSearchForm queryForm) {
		return postService.findDynamic(queryForm);
	}
}
