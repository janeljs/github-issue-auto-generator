package com.jane.github;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRequest {

    private String title;
    private List<String> labels;
    private String assignee;

}
