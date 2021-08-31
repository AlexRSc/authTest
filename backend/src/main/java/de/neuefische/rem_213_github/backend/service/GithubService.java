package de.neuefische.rem_213_github.backend.service;

import de.neuefische.rem_213_github.backend.api.GithubPullRequest;
import de.neuefische.rem_213_github.backend.model.RepoEntity;
import de.neuefische.rem_213_github.backend.model.UserEntity;
import de.neuefische.rem_213_github.backend.rest.github.GithubClient;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubPullRequestDto;
import de.neuefische.rem_213_github.backend.rest.github.GithubRepoDto;
import de.neuefische.rem_213_github.backend.rest.github.GithubRepoDtos;
import de.neuefische.rem_213_github.backend.rest.github.GithubUserDto;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GithubService {

    private final GithubClient githubClient;
    private final UserService userService;

    @Autowired
    public GithubService(GithubClient githubClient, UserService userService) {
        this.githubClient = githubClient;
        this.userService = userService;
    }

    public GithubUserDto getWhoAmI() {
        return githubClient.getWhoAmI();
    }

    public GithubRepoDtos getUserRepos(GithubUserDto githubUserDto) {
        String login = githubUserDto.getLogin();
        Assert.hasText(login, "Login must not be null to get user repos");

        GithubRepoDtos githubRepoDtos = githubClient.getUserRepos(login);

        List<GithubRepoDto> githubRepoDtoList = githubRepoDtos.getGithubRepoDtos();
        if (githubRepoDtoList.isEmpty()) {
            return githubRepoDtos;
        }

        Optional<UserEntity> existingUserEntityOpt = userService.find(login);
        if (existingUserEntityOpt.isPresent()) {
            // update existing user
            UserEntity existingUserEntity = existingUserEntityOpt.get();

            UserEntity userEntity = new UserEntity();
            Set<RepoEntity> userRepos = map(githubRepoDtoList);
            userEntity.addRepos(userRepos);

            userService.update(existingUserEntity.getId(), userEntity);

        } else {
            // create new user with all repos
            UserEntity userEntity = new UserEntity();
            userEntity.setAvatarUrl(githubUserDto.getAvatar_url());
            userEntity.setName(login);

            Set<RepoEntity> userRepos = map(githubRepoDtoList);
            userEntity.addRepos(userRepos);

            userService.create(userEntity);
        }

        return githubRepoDtos;
    }

    private Set<RepoEntity> map(List<GithubRepoDto> githubRepoDtoList) {
        Set<RepoEntity> userRepos = new HashSet<>();
        for (GithubRepoDto githubRepoDto : githubRepoDtoList) {
            RepoEntity repoEntity = new RepoEntity();
            repoEntity.setName(githubRepoDto.getName());
            userRepos.add(repoEntity);
        }
        return userRepos;
    }

    public Optional<GithubPullDtos> getPulls(GithubUserDto githubUserDto, String repoName) {
        return githubClient.getRepoPulls(githubUserDto.getLogin(), repoName);
    }

    public Optional<GithubPullDtos> create(GithubUserDto githubUserDto, String repoName,
                                           GithubPullRequest githubPullRequest) {
        GithubPullRequestDto githubPullRequestDto = GithubPullRequestDto.builder()
                .repo(repoName)
                .owner(githubUserDto.getLogin())
                .head(githubPullRequest.getHead())
                .base(githubPullRequest.getBase())
                .title(githubPullRequest.getTitle())
                .build();

        return githubClient.createPullRequest(githubUserDto.getLogin(), repoName, githubPullRequestDto);
    }
}
