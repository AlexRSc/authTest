package de.neuefische.rem_213_github.backend.model;


import lombok.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "rem_user")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private final Set<RepoEntity> repos = new HashSet<>();

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String name;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name ="user_role")
    private String role;

    public Set<RepoEntity> getRepos() {
        // protect inner repo set for modification from outside
        return Collections.unmodifiableSet(repos);
    }

    public void setRepos(Set<RepoEntity> userRepos) {
        // all current repos into map for easy select
        Map<Integer, RepoEntity> allRepos = new HashMap<>();
        for (RepoEntity repoEntity : getRepos()) {
            allRepos.put(repoEntity.hashCode(), repoEntity);
        }

        // iterate over all user repos to be set
        Set<RepoEntity> newRepos = new HashSet<>();
        for (RepoEntity repoEntity : userRepos) {
            RepoEntity existing = allRepos.get(repoEntity.hashCode());
            // in case a user repo with same hash exist, we have to take this one to re-set again
            newRepos.add(existing != null ? existing : repoEntity);
        }

        // clear all repos now and add all to be set again
        this.repos.clear();
        addRepos(newRepos);
    }

    public void addRepos(Set<RepoEntity> userRepos) {
        this.repos.addAll(userRepos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserEntity that = (UserEntity) o;
        return this.getName() != null && this.getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
