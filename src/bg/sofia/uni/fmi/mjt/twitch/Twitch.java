package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Twitch implements StreamingPlatform {
    private final UserService userService;
    private final LinkedList<Content> contents;
    private final ContentByCategoryService contentByCategoryService;
    private final Map<User, Integer> userViews;

    public Twitch(UserService userService) {
        this.userService = userService;
        this.contents = new LinkedList<>();
        this.contentByCategoryService = new ContentByCategoryService(userService);
        this.userViews = new HashMap<>();
        for (Map.Entry<String, User> entry : this.userService.getUsers().entrySet()) {
            this.userViews.put(entry.getValue(), 0);
        }
    }

    private User findUserByNameInUserService(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Invalid value for user! User cannot be null!");
        }
        Map<String, User> userMap = userService.getUsers();

        for (Map.Entry<String, User> userEntry : userMap.entrySet()) {
            if (userEntry.getValue() != null && userEntry.getKey().equals(username)) {
                return userEntry.getValue();
            }
        }
        return null;
    }

    private LinkedList<Content> findAllContentByUser(User user) {
        LinkedList<Content> contentsByUser = new LinkedList<>();

        for (Content content : this.contents) {
            if (content.getMetadata().user().equals(user)) {
                contentsByUser.add(content);
            }
        }
        return contentsByUser;
    }

    private Content findMostViewedContentInContentList(LinkedList<Content> contentByUser) {
        int maxViews = -1;
        Content mostViewedContent = null;

        for (Content content : contentByUser) {
            if (content != null && content.getNumberOfViews() > maxViews) {
                maxViews = content.getNumberOfViews();
                mostViewedContent = content;
            }
        }
        return mostViewedContent;
    }

    @Override
    public Stream startStream(String username, String title, Category category) throws UserNotFoundException, UserStreamingException {
        if (username == null || title == null || title.isEmpty() || category == null) {
            throw new IllegalArgumentException("Invalid argument value in startStream function!");
        }

        User findUserByUserName = findUserByNameInUserService(username);

        if (findUserByUserName == null) {
            throw new UserNotFoundException("There is no user with this username!");
        } else if (findUserByUserName.getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("This user is currently streaming!");
        } else {
            findUserByUserName.setStatus(UserStatus.STREAMING);
            Stream newStream = new Stream(new Metadata(title, category, findUserByUserName));
            this.contents.add(newStream);

            return newStream;
        }
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isEmpty() || stream == null) {
            throw new IllegalArgumentException("Invalid argument value in endStream function!");
        }

        User findUserByUserName = findUserByNameInUserService(username);

        if (findUserByUserName == null) {
            throw new UserNotFoundException("There is no user with this username!");
        } else if (findUserByUserName.getStatus() != UserStatus.STREAMING) {
            throw new UserStreamingException("This user is currently not streaming!");
        } else {
            findUserByUserName.setStatus(UserStatus.OFFLINE);

            contents.remove(stream);
            Video newVideoOfStream = new Video(stream.getMetadata(), stream.getDuration());

            this.contents.add(newVideoOfStream);

            ///тук зануляваме и гледанията на този streamer до текущия момент:
            for (Map.Entry<User, Integer> entry : this.userViews.entrySet()) {
                if (entry.getKey().equals(findUserByUserName)) {
                    entry.setValue(entry.getValue() - stream.getNumberOfViews());
                }
            }
            return newVideoOfStream;
        }
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (content == null || username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Invalid value of argument in watch function!");
        }
        User findUserByUserName = findUserByNameInUserService(username);

        if (findUserByUserName == null) {
            throw new UserNotFoundException("There is no user with this username!");
        } else if (findUserByUserName.getStatus() == UserStatus.STREAMING) {
            throw new UserStreamingException("This user is currently streaming!");
        } else if (this.contents.contains(content)) {
            for (Map.Entry<User, Integer> entry : this.userViews.entrySet()) {
                if (entry.getKey().getName().equals(content.getMetadata().user().getName())) {
                    entry.setValue(entry.getValue() + 1);
                }
            }

            int indexOfContent = this.contents.indexOf(content);

            this.contents.get(indexOfContent).startWatching(findUserByUserName);
            this.contentByCategoryService.addView(username, content.getMetadata().category());
        }
    }

    @Override
    public User getMostWatchedStreamer() {
        User mostWatchedStreamer = null;
        int mostViews = 0;

        for (Map.Entry<User, Integer> entry : this.userViews.entrySet()) {
            if (mostViews < entry.getValue()) {
                mostWatchedStreamer = entry.getKey();
                mostViews = entry.getValue();
            }
        }

        return mostWatchedStreamer;
    }

    @Override
    public Content getMostWatchedContent() {
        Content mostWatchedContent = null;
        for (Content content : this.contents) {
            if (mostWatchedContent == null) {
                mostWatchedContent = content;
            } else if (content != null && content.getNumberOfViews() > mostWatchedContent.getNumberOfViews()) {
                mostWatchedContent = content;
            }
        }
        return mostWatchedContent;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Invalid argument value in endStream function!");
        }

        User findUserByUserName = findUserByNameInUserService(username);

        if (findUserByUserName == null) {
            throw new UserNotFoundException("There is no user with this username!");
        } else {
            return findMostViewedContentInContentList(findAllContentByUser(findUserByUserName));
        }
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        return this.contentByCategoryService.getMostWatchedCategories(username);
    }
}
