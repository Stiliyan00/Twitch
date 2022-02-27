package bg.sofia.uni.fmi.mjt.twitch;


import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.*;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TwitchTest {

    private final UserStubImplementation newUser = new UserStubImplementation("stiliyan");
    private final UserStubImplementation firstUser = new UserStubImplementation("alex");
    private final UserStubImplementation newerUser = new UserStubImplementation("kris");
    private final UserStubImplementation mostRecentUser = new UserStubImplementation("konstantin");

    @Mock
    private UserService userService;

    private Twitch twitch;

    @BeforeEach
    void setUp() {
        Map<String, User> myUserLinkedList = new HashMap<>();

        myUserLinkedList.put("alex", firstUser);
        myUserLinkedList.put("stiliyan", newUser);
        myUserLinkedList.put("kris", newerUser);
        myUserLinkedList.put("konstantin", mostRecentUser);

        Mockito.when(userService.getUsers()).thenReturn(myUserLinkedList);

        twitch = new Twitch(userService);
    }

    @AfterEach
    void tearDown() {
        firstUser.setStatus(UserStatus.OFFLINE);
        newUser.setStatus(UserStatus.OFFLINE);
    }

    @Test
    void testStartStreamWithNullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream(null, "titlte", Category.GAMES),
                "startStream with value for argument username null, should throw IllegalArgumentException!");
    }

    @Test
    void testStartStreamWithEmptyStringUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("", "titlte", Category.GAMES),
                "startStream with value for argument username empty string, should throw IllegalArgumentException!");
    }

    @Test
    void testStartStreamWithNullTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("username", null, Category.GAMES),
                "startStream with value for argument title null, should throw IllegalArgumentException!");
    }

    @Test
    void testStartStreamWithEmptyStringTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("username", "", Category.GAMES),
                "startStream with value for argument title empty string, should throw IllegalArgumentException!");
    }

    @Test
    void testStreamWithNullCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("username", "title", null),
                "startStream with value for argument category null, should throw IllegalArgumentException!");
    }

    @Test
    void testStartStreamWithUserNotFoundInUserService() {
        assertThrows(UserNotFoundException.class,
                () -> twitch.startStream("stiliyan00", "Any Stream", Category.GAMES),
                "startStream with this username value, should throw UserNotFoundException!");
    }

    @Test
    void testStartStreamWithAlreadyStreamingUser() {
        newUser.setStatus(UserStatus.STREAMING);

        assertThrows(UserStreamingException.class,
                () -> twitch.startStream("stiliyan", "Any title", Category.GAMES),
                "startStream with this username value, should throw UserStreamingException!");
    }

    @Test
    void testStartStreamWithValidArguments() throws UserNotFoundException, UserStreamingException {

        assertEquals(new Stream(new Metadata("Any title", Category.GAMES, newUser)),
                twitch.startStream("stiliyan", "Any title", Category.GAMES),
                "startStream should return the new Stream in the Twitch platform!");
    }

    @Test
    void testEndStreamWithNullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream(null,
                        new Stream(new Metadata("any title", Category.GAMES, newUser))),
                "endStream with value for argument username null, should throw IllegalArgumentException!");
    }

    @Test
    void testEndStreamWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream("",
                        new Stream(new Metadata("any titlte", Category.GAMES, newUser))),
                "endStream with value for argument username empty string, should throw IllegalArgumentException!");
    }

    @Test
    void testEndStreamWithNullStream() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream("",
                        null),
                "endStream with value for argument stream null, should throw IllegalArgumentException!");
    }

    @Test
    void testEndStreamWithUserNotFoundInUserService() {
        assertThrows(UserNotFoundException.class,
                () -> twitch.endStream("stiliyan00",
                        new Stream(new Metadata("Any Stream", Category.GAMES, newUser))),
                "endStream with this username value, should throw UserNotFoundException!");
    }

    @Test
    void testEndStreamWithAlreadyStreamingUser() {

        assertThrows(UserStreamingException.class,
                () -> twitch.endStream("stiliyan",
                        new Stream(new Metadata("Any title", Category.GAMES, newUser))),
                "endStream with this username value, should throw UserStreamingException!");
    }

    /*
     * няма как да напиша тестове където enStream работи, понеже ще ми трябва да мога да борява с
     * Duration.now(),а Стоян препоръча да не се занимава като по-неопитни с това :Д
     * */

    @Test
    void testWatchWithNullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.watch(null,
                        new Stream(new Metadata("any title", Category.GAMES, newUser))),
                "watch with value for argument username null, should throw IllegalArgumentException!");
    }

    @Test
    void testWatchWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.watch("",
                        new Stream(new Metadata("any title", Category.GAMES, newUser))),
                "watch with value for argument username empty string, should throw IllegalArgumentException!");
    }

    @Test
    void testWatchWithNullContent() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream("Any Username", null),
                "endStream with value for argument username null, should throw IllegalArgumentException!");
    }

    @Test
    void testWatchWithUserNotFoundInUserService() {
        assertThrows(UserNotFoundException.class,
                () -> twitch.watch("stiliyan00",
                        new Stream(new Metadata("Any Stream", Category.GAMES, newUser))),
                "watch with this username value, should throw UserNotFoundException!");
    }

    @Test
    void testWatchWithAlreadyStreamingUser() {
        firstUser.setStatus(UserStatus.STREAMING);

        assertThrows(UserStreamingException.class,
                () -> twitch.watch("alex",
                        new Stream(new Metadata("Any title", Category.GAMES, newUser))),
                "watch with this username value, should throw UserStreamingException!");
    }

    @Test
    void testWatchWithValidArguments() {

    }
    /*
    ne znam kak se pishat testove za watch, koito da sa verni, poneje e void :D
     */

    @Test
    void testGetMostWatchedStreamerWithZeroViewsForEveryStreamer() {
        assertNull(twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedStreamerWithOnlyOneUserWithOneViewWhileStreaming() throws UserNotFoundException, UserStreamingException {
        Stream tempStream = twitch.startStream("alex", "Happy", Category.MUSIC);
        twitch.watch("stiliyan", tempStream);

        assertEquals(firstUser, twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedStreamerWithTwoStreamerWhoAreCurrentlyStreaming() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "Happy", Category.MUSIC);
        Stream secondStream = twitch.startStream("stiliyan", "CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        assertEquals(firstUser, twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedStreamerWithTwoStreamerWhoAreCurrentlyStreaming2() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "Happy", Category.MUSIC);
        Stream secondStream = twitch.startStream("stiliyan", "CS", Category.GAMES);

        twitch.watch("kris", secondStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        assertEquals(newUser, twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedStreamerWithOneStreamingAndOneStoppedStreamingStreamers() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "Happy", Category.MUSIC);
        Stream secondStream = twitch.startStream("stiliyan", "CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        twitch.endStream("alex", firstStream);

        assertEquals(newUser, twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedStreamerWithBothStreamerStoppedStreaming() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "Happy", Category.MUSIC);
        Stream secondStream = twitch.startStream("stiliyan", "CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        twitch.endStream("alex", firstStream);
        twitch.endStream("stiliyan", secondStream);
        assertNull(twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedStreamerWithTwoVideos() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "Happy", Category.MUSIC);
        Stream secondStream = twitch.startStream("stiliyan", "CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        Video firstVideo = twitch.endStream("alex", firstStream);
        Video secondVideo = twitch.endStream("stiliyan", secondStream);


        twitch.watch("kris", firstVideo);
        twitch.watch("kris", secondVideo);
        twitch.watch("konstantin", firstVideo);

        assertEquals(firstUser, twitch.getMostWatchedStreamer());
    }

    @Test
    void testGetMostWatchedContentWithNoContentAvailable() {
        assertNull(twitch.getMostWatchedContent());
    }

    @Test
    void testGetMostWatchedContentWithOneContentWithZeroViews() throws UserNotFoundException, UserStreamingException {
        Stream stream = twitch.startStream("alex", "new stream", Category.IRL);
        Video video = twitch.endStream("alex", new Stream(stream.getMetadata()));

        assertEquals(new Video(video.getMetadata(), video.getDuration()), twitch.getMostWatchedContent());
    }

    @Test
    void testGetMostWatchedContendWithTwoStreams() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "new stream", Category.IRL);
        Stream secondStream = twitch.startStream("stiliyan","CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        assertEquals(firstStream, twitch.getMostWatchedContent());
    }

    @Test
    void testGetMostWatchedContentWithOneStreamAndOneVideo () throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "new stream", Category.IRL);
        Stream secondStream = twitch.startStream("stiliyan","CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        twitch.endStream("alex", firstStream);

        assertEquals(secondStream, twitch.getMostWatchedContent());
    }

    @Test
    void testGetMostWatchedContentFromWithNullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.getMostWatchedContentFrom(null));
    }

    @Test
    void testGetMostWatchedContentFromEmptyStringUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.getMostWatchedContentFrom(""));
    }

    @Test
    void testGetMostWatchedContentFromNonExistingUser() {
        assertThrows(UserNotFoundException.class,
                () -> twitch.getMostWatchedContentFrom("stiliyan00"));
    }

    @Test
    void testGetMostWatchedContentFromUserWithNonContent() throws UserNotFoundException {
        assertNull(twitch.getMostWatchedContentFrom("stiliyan"));
    }

    @Test
    void testGetMostWatchedContentFromWithTwoUsersWithOneStreaming() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "new stream", Category.IRL);
        Stream secondStream = twitch.startStream("stiliyan","CS", Category.GAMES);

        twitch.watch("kris", firstStream);
        twitch.watch("kris", secondStream);
        twitch.watch("konstantin", firstStream);

        assertEquals(firstStream, twitch.getMostWatchedContentFrom("alex"));
    }

    @Test
    void testGetMostWatchedContentFromUserWithTwoVideos() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "new stream", Category.IRL);
        Video video1 = twitch.endStream("alex", firstStream);

        Stream secondStream = twitch.startStream("alex","CS", Category.GAMES);
        Video video2 = twitch.endStream("alex", secondStream);

        twitch.watch("kris", video1);
        twitch.watch("kris", video2);
        twitch.watch("kris", video2);
        twitch.watch("konstantin", video1);
        twitch.watch("konstantin", video1);

        assertEquals(video1, twitch.getMostWatchedContentFrom("alex"));
    }

    @Test
    void testGetMostWatchedCategoriesByWithNullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.getMostWatchedCategoriesBy(null));
    }

    @Test
    void testGetMostWatchedCategoriesByWithEmptyStringUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.getMostWatchedCategoriesBy(""));
    }

    @Test
    void testGetMostWatchedCategoriesByWithNonExistingUsername() {
        assertThrows(UserNotFoundException.class,
                () -> twitch.getMostWatchedCategoriesBy("stiliyan00"));
    }

    @Test
    void testGetMostWatchedCategoriesByWithNonCategories() throws UserNotFoundException {
        assertEquals(new LinkedList<>(), twitch.getMostWatchedCategoriesBy("konstantin"));
    }

    @Test
    void testGetMostWatchedCategoriesByWithOneStreamWithZeroViews() throws UserNotFoundException {
        assertEquals(new LinkedList<>(), twitch.getMostWatchedCategoriesBy("konstantin"));
    }

    @Test
    void testGetMostWatchedCategoriesByWithOneStreamWithOneView() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "new stream", Category.IRL);

        twitch.watch("kris", firstStream);

        LinkedList<Category> list = new LinkedList<>();
        list.add(firstStream.getMetadata().category());

        assertEquals(list , twitch.getMostWatchedCategoriesBy("kris"));
    }


    @Test
    void testGetMostWatchedCategoriesByWithTwoVideos() throws UserNotFoundException, UserStreamingException {
        Stream firstStream = twitch.startStream("alex", "new stream", Category.IRL);
        Video video1 = twitch.endStream("alex", firstStream);

        Stream secondStream = twitch.startStream("alex","CS", Category.GAMES);
        Video video2 = twitch.endStream("alex", secondStream);

        twitch.watch("kris", video1);
        twitch.watch("kris", video2);
        twitch.watch("kris", video2);
        twitch.watch("konstantin", video1);
        twitch.watch("konstantin", video1);

        LinkedList<Category> list = new LinkedList<>();
        list.add(video2.getMetadata().category());
        list.add(video1.getMetadata().category());

        assertEquals(list , twitch.getMostWatchedCategoriesBy("kris"));

    }
}