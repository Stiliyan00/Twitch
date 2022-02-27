package bg.sofia.uni.fmi.mjt.twitch.content;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.util.Objects;

public abstract class AbstractContent implements Content {
    protected final Metadata metadata;
    protected int numberOfViews = 0;

    public AbstractContent(Metadata metadata) {
        this.metadata = metadata;
        this.numberOfViews = 0;
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

    @Override
    public void startWatching(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user input value! User cannot be null!");
        }

        this.numberOfViews += 1;
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractContent that = (AbstractContent) o;
        return numberOfViews == that.numberOfViews && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, numberOfViews);
    }
}
