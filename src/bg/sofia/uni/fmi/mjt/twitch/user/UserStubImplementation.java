package bg.sofia.uni.fmi.mjt.twitch.user;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserStubImplementation implements User {
    private final String name;
    private UserStatus currentUserStatus;
    private final Map<Content, Integer> contentViewsMap;

    public UserStubImplementation(String name) {
        this.name = name;
        this.currentUserStatus = UserStatus.OFFLINE;
        this.contentViewsMap = new HashMap<>(1);
    }

    private Integer findViewsOfContent(Content content) {
        return this.contentViewsMap.getOrDefault(content, null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UserStatus getStatus() {
        return this.currentUserStatus;
    }

    @Override
    public void setStatus(UserStatus status) {
        this.currentUserStatus = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStubImplementation that = (UserStubImplementation) o;
        return Objects.equals(name, that.name) && currentUserStatus == that.currentUserStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentUserStatus);
    }
}
