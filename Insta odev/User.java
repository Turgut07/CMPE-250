import java.util.ArrayList;

/**
 * Represents a user in the system.
 * Each user has a unique ID, a list of followed users, posts, and a personalized feed.
 */
public class User {

    /**
     * Constructs a new User with the given ID.
     * Initializes data structures for followed users, posts, feed, and seen posts.
     *
     * @param id The unique identifier of the user.
     */
    public User(String id) {
        this.id = id;

        // Users followed by this user
        followedUsers = new CustomHashMap<>();

        // Priority queue for the user's personalized feed
        Feed = new PriorityQueue<>();

        // Posts seen by the user
        seenPost = new CustomHashMap<>();

        // List of posts created by the user
        posts = new ArrayList<>();

        // List of posts in the user's feed
        feed = new ArrayList<>();

        // Separate list for sorting posts
        postsforsort = new ArrayList<>();
    }

    /**
     * Returns the user's ID.
     *
     * @return The ID of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the list of posts in the user's feed.
     *
     * @return An ArrayList of posts in the feed.
     */
    public ArrayList<Post> feed;

    /**
     * Returns the map of users followed by this user.
     *
     * @return A CustomHashMap of followed users.
     */
    public CustomHashMap<String, User> getFollowedUsers() {
        return followedUsers;
    }

    /**
     * Returns the priority queue representing the user's feed.
     *
     * @return A PriorityQueue of posts.
     */
    public PriorityQueue<Post> getFeed() {
        return Feed;
    }

    /**
     * Returns the list of posts created by the user.
     *
     * @return An ArrayList of the user's posts.
     */
    public ArrayList<Post> getPosts() {
        return posts;
    }

    // User's unique ID
    String id;

    // Map of users followed by this user
    CustomHashMap<String, User> followedUsers;

    // Map of posts seen by this user
    CustomHashMap<String, Post> seenPost;

    // List of posts for sorting
    ArrayList<Post> postsforsort;

    // Priority queue for the user's feed
    PriorityQueue<Post> Feed;

    // List of posts created by the user
    ArrayList<Post> posts;
}
