package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Stream extends AbstractContent {

    private final LocalDateTime localDateTime;

    public Stream(Metadata metadata) {
        super(metadata);
        this.localDateTime = LocalDateTime.now();
    }

    @Override
    public Duration getDuration() {
        return Duration.between(this.localDateTime, LocalDateTime.now());
    }

    @Override
    public void stopWatching(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user input value! User cannot be null!");
        }
        user.setStatus(UserStatus.OFFLINE);
        super.numberOfViews -= 1;
    }
}
