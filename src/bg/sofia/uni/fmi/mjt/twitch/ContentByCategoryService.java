package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ContentByCategoryService {
    private final Map<String, CategoryViews> userContentByCategoryViews;

    public ContentByCategoryService(UserService userService) {
        this.userContentByCategoryViews = new HashMap<>();

        LinkedList<String> usernameList = new LinkedList<>(userService.getUsers().keySet());

        for (String s : usernameList) {
            this.userContentByCategoryViews.put(s, new CategoryViews());
        }
    }

    public void addView(String username, Category category) {
        this.userContentByCategoryViews.get(username).addViewOfCategory(category);
    }

    public LinkedList<Category> getMostWatchedCategories(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Invalid value of argument username! Username must be a non-empty string!");
        }
        if (!this.userContentByCategoryViews.containsKey(username)) {
            throw new UserNotFoundException("No such user, with this username!");
        }
        return this.userContentByCategoryViews.get(username).getMostWatchedCategories();
    }
}
