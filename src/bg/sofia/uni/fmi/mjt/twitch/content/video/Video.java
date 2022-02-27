package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;

import java.time.Duration;
import java.util.Objects;

public class Video extends AbstractContent {
    private final Duration duration;

    public Video(Metadata metadata, Duration duration) {
        super(metadata);
        this.duration = duration;
    }

    @Override
    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public void stopWatching(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user input value! User cannot be null!");
        }
        user.setStatus(UserStatus.OFFLINE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(duration, video.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration);
    }
}
