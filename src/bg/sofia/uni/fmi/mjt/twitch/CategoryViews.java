package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class CategoryViews {
    private final Comparator<CategoryViewsPair> categoryViewsPairComparator = new Comparator<CategoryViewsPair>() {
        @Override
        public int compare(CategoryViewsPair o1, CategoryViewsPair o2) {
            return o2.getViews() - o1.getViews();
        }
    };
    private final LinkedList<CategoryViewsPair> categoryNumberOfViews;

    public CategoryViews() {
        this.categoryNumberOfViews = new LinkedList<>();
        this.categoryNumberOfViews.add(0, new CategoryViewsPair(Category.ESPORTS, 0));
        this.categoryNumberOfViews.add(1, new CategoryViewsPair(Category.GAMES, 0));
        this.categoryNumberOfViews.add(2, new CategoryViewsPair(Category.IRL, 0));
        this.categoryNumberOfViews.add(3, new CategoryViewsPair(Category.MUSIC, 0));
    }

    public void addViewOfCategory(Category category) {
        switch (category) {
            case IRL -> this.categoryNumberOfViews.get(2).setViews(this.categoryNumberOfViews.get(2).getViews() + 1);
            case ESPORTS -> this.categoryNumberOfViews.get(0).setViews(this.categoryNumberOfViews.get(0).getViews() + 1);
            case GAMES -> this.categoryNumberOfViews.get(1).setViews(this.categoryNumberOfViews.get(1).getViews() + 1);
            case MUSIC -> this.categoryNumberOfViews.get(3).setViews(this.categoryNumberOfViews.get(3).getViews() + 1);
        }
    }

    public LinkedList<Category> getMostWatchedCategories() {
        LinkedList<CategoryViewsPair> categoryViewsPairLinkedList = new LinkedList<>(this.categoryNumberOfViews);

        categoryViewsPairLinkedList.sort(categoryViewsPairComparator);

        Iterator<CategoryViewsPair> iterator = categoryViewsPairLinkedList.iterator();

        LinkedList<Category> result = new LinkedList<>();
        while (iterator.hasNext()) {
            CategoryViewsPair currentCategoryViewPair = iterator.next();
            if (currentCategoryViewPair.getViews() > 0) {
                result.add(currentCategoryViewPair.getCategory());
            }
        }

        return result;
    }
}
