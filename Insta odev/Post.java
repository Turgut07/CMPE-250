import java.util.stream.Stream;

/**
 * Represents a social media post created by a user.
 * A post includes a unique ID, content, a creator, and a record of likes.
 * Implements the Comparable interface for sorting posts by likes.
 */
public class Post implements Comparable<Post> {

    // Unique identifier for the post
    String postId;

    // Number of likes on the post
    int likes;

    // Content of the post
    String content;

    // User who created the post
    User creator;

    // CustomHashMap of users who liked the post
    CustomHashMap<String, User> likedBy;

    /**
     * Constructs a new Post with the given ID, content, and creator.
     * Initializes the likedBy map to keep track of users who like the post.
     *
     * @param postId  The unique identifier for the post.
     * @param content The content of the post.
     * @param creator The user who created the post.
     */
    public Post(String postId, String content, User creator) {
        this.postId = postId;
        this.content = content;
        this.likedBy = new CustomHashMap<>();
        this.creator = creator;
    }

    /**
     * Compares this post to another post for ordering.
     * Comparison is based on the number of likes. In case of a tie, the postId is used.
     *
     * @param o The other post to compare to.
     * @return A positive integer if this post has more likes, a negative integer if it has fewer likes,
     *         or the result of comparing post IDs if likes are equal.
     */
    public int compareTo(Post o) {
        if (this.likes > o.likes) {
            return 1; // This post has more likes
        }
        if (this.likes < o.likes) {
            return -1; // This post has fewer likes
        }

        // If likes are equal, compare by postId
        return this.postId.compareTo(o.postId);
    }
}
