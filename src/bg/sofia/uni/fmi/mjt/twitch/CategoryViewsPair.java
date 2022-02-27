package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;

public class CategoryViewsPair {
    private final Category category;
    private Integer views;

    public CategoryViewsPair(Category category, Integer views) {
        this.category = category;
        this.views = views;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
}
