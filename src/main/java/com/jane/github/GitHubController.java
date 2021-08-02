package com.jane.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GitHubController {
    private final String username;
    private final String repositoryName;
    private final String token;
    private final RestTemplate restTemplate;

    public GitHubController(RestTemplateBuilder restTemplateBuilder,
                            @Value("${github.username}") String username,
                            @Value("${github.repository.name}") String repositoryName,
                            @Value("${github.personal.access.token}") String token) {
        restTemplate = restTemplateBuilder.build();
        this.username = username;
        this.repositoryName = repositoryName;
        this.token = token;
    }

    @GetMapping("/create")
    public String createIssues(@RequestParam("week_number") int weekNumber,
                               @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               @RequestParam("issue_count") int issueCount) {

        String url = "https://api.github.com/repos/" + username + "/" + repositoryName + "/issues";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);

        for (int i = 0; i < issueCount; i++) {
            String title = startDate.toString();
            GitHubRequest gitHubRequest = new GitHubRequest(title, List.of("WEEK" + weekNumber), username);
            HttpEntity<GitHubRequest> request = new HttpEntity<>(gitHubRequest, httpHeaders);
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            startDate = startDate.plusDays(1);
            if (i % 7 == 6) {
                weekNumber++;
            }
        }

        return "이슈 생성 완료";
    }
}
