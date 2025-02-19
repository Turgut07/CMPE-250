import java.io.*;
import java.util.Scanner;

/**
 * Main class that manages user interactions, posts, and social media-like functionality.
 */
public class Main {

    /**
     * Creates a new user and adds them to the provided CustomHashMap.
     *
     * @param customHashMap HashMap to store the user.
     * @param userid        The ID of the new user.
     * @return Status message indicating success or failure.
     */
    public static String createUser(CustomHashMap<String, User> customHashMap, String userid) {
        int k = customHashMap.put(userid, new User(userid));
        if (k == -1) {
            return "Some error occurred in create_user.";
        }
        return "Created user with Id " + userid + ".";
    }

    /**
     * Allows one user to follow another user.
     *
     * @param customHashMap HashMap containing all users.
     * @param userId1       ID of the user initiating the follow.
     * @param userId2       ID of the user to be followed.
     * @return Status message indicating success or failure.
     */
    public static String follow_user(CustomHashMap<String, User> customHashMap, String userId1, String userId2) {
        User user1 = customHashMap.get(userId1);
        User user2 = customHashMap.get(userId2);

        // Check for invalid users or duplicate follow attempts
        if (user1 == null || user2 == null || user1.followedUsers.containsKey(userId2) || user1 == user2) {
            return "Some error occurred in follow_user.";
        }
        user1.followedUsers.put(user2.id, user2);
        return userId1 + " followed " + userId2 + ".";
    }

    /**
     * Allows one user to unfollow another user.
     *
     * @param customHashMap HashMap containing all users.
     * @param userId1       ID of the user initiating the unfollow.
     * @param userId2       ID of the user to be unfollowed.
     * @return Status message indicating success or failure.
     */
    public static String unfollow_user(CustomHashMap<String, User> customHashMap, String userId1, String userId2) {
        User user1 = customHashMap.get(userId1);
        User user2 = customHashMap.get(userId2);
        // Check if users exist or the user to be unfollowed is already not followed
        if (user1 == null || user2 == null || user1.followedUsers.get(userId2) == null) {
            return "Some error occurred in unfollow_user.";
        }
        user1.followedUsers.remove(userId2);
        return userId1 + " unfollowed " + userId2 + ".";
    }

    /**
     * Creates a post by a user and adds it to the priority queue and HashMap.
     *
     * @param customHashMap HashMap containing all users.
     * @param priorityQueue Queue to prioritize posts.
     * @param postHash      HashMap storing posts by ID.
     * @param userid        ID of the user creating the post.
     * @param postID        ID of the post.
     * @param content       Content of the post.
     * @return Status message indicating success or failure.
     */
    public static String createPost(CustomHashMap<String, User> customHashMap, PriorityQueue<Post> priorityQueue,
                                    CustomHashMap<String, Post> postHash, String userid, String postID, String content) {
        User user = customHashMap.get(userid);
        Post post = new Post(postID, content, user);
        // Check for null user or duplicate post ID
        if (user == null) {
            return "Some error occurred in create_post.";
        }
        if (postHash.put(postID, post) == -1) {
            return "Some error occurred in create_post.";
        }
        postHash.put(postID, post);
        priorityQueue.insert(post);
        user.posts.add(post);
        return userid + " created a post with Id " + postID + ".";
    }

    /**
     * Marks a specific post as seen by a user.
     *
     * @param allUserHM HashMap containing all users.
     * @param allPosts  HashMap containing all posts.
     * @param userId    ID of the user viewing the post.
     * @param postId    ID of the post to be marked as seen.
     * @return Status message indicating success or failure.
     */
    public static String see_post(CustomHashMap<String, User> allUserHM, CustomHashMap<String, Post> allPosts,
                                  String userId, String postId) {
        User user = allUserHM.get(userId);
        Post post = allPosts.get(postId);
        // Check for null user or post
        if (user == null || post == null) {
            return "Some error occurred in see_post.";
        }
        user.seenPost.put(postId, post);
        return userId + " saw " + postId + ".";
    }

    /**
     * Allows a user to view all posts from another user.
     *
     * @param allUserHM HashMap containing all users.
     * @param allPosts  HashMap containing all posts.
     * @param userId1   ID of the user viewing posts.
     * @param userId2   ID of the user whose posts are being viewed.
     * @return Status message indicating success or failure.
     */
    public static String see_all_post(CustomHashMap<String, User> allUserHM, CustomHashMap<String, Post> allPosts,
                                      String userId1, String userId2) {
        User user1 = allUserHM.get(userId1);
        User user2 = allUserHM.get(userId2);
        // Check for null users
        if (user1 == null || user2 == null) {
            return "Some error occurred in see_all_posts_from_user.";
        }
        // Add all posts from user2 to user1's seen posts
        for (Post i : user2.posts) {
            user1.seenPost.put(i.postId, i);
        }
        return userId1 + " saw all posts of " + userId2 + ".";
    }
    /**
     * Toggles like status on a post for a specific user.
     *
     * @param priorityQueue Priority queue containing posts.
     * @param allUserHM     HashMap containing all users.
     * @param postHash      HashMap containing all posts.
     * @param user          ID of the user liking/unliking the post.
     * @param postId        ID of the post to be liked/unliked.
     * @return Status message indicating success or failure.
     */
    public static String toggle_like(PriorityQueue<Post> priorityQueue, CustomHashMap<String, User> allUserHM,
                                     CustomHashMap<String, Post> postHash, String user, String postId) {
        Post post = postHash.get(postId);
        User user1 = allUserHM.get(user);
        // Check for null user or post
        if (user1 == null || post == null) {
            return "Some error occurred in toggle_like.";
        }
        if (post.likedBy.containsKey(user)) {
            post.likedBy.remove(user);
            post.likes--;
            return user + " unliked " + postId + ".";
        }
        post.likedBy.put(user, user1);
        post.likes++;
        see_post(allUserHM, postHash, user, postId);
        return user + " liked " + postId + ".";
    }


    /**
     * Generates a feed of posts for a user.
     *
     * @param allUserHM HashMap containing all users.
     * @param userId    ID of the user requesting the feed.
     * @param num       Number of posts to fetch for the feed.
     * @return String representing the generated feed.
     */
    public static String generate_feed(CustomHashMap allUserHM, String userId, int num) {
        User user = (User) allUserHM.get(userId);
        PriorityQueue<Post> tempPriorityQueue = new PriorityQueue<>();

        // Check for null user
        if (user == null) {
            return "Some error occurred in generate_feed.\n";
        }
        user.Feed = new PriorityQueue<>();
        PriorityQueue<Post> postPriorityQueue = user.followedUsers.addFollowedUsersPostsToQueue(user.followedUsers,
                tempPriorityQueue);

        String string = "Feed for " + userId + ":\n";

        int number = num;

        while (number > 0 && !postPriorityQueue.isEmpty()) {
            Post k = postPriorityQueue.removeMax();

            if (user.seenPost.containsKey(k.postId) || user.posts.contains(k)) {
                continue;
            } else {
                user.Feed.insert(k);
                string = string + "Post ID: " + k.postId + ", Author: " + k.creator.id + ", Likes: " + k.likes + "\n";
                number--;
            }
        }
        if (number > 0) {
            return string + "No more posts available for " + userId + ".\n";
        }
        return string;
    }

    /**
     * Allows a user to scroll through their feed and interact with posts.
     *
     * @param allUserHM    HashMap containing all users.
     * @param userID       ID of the user scrolling through the feed.
     * @param numbercount  Number of posts to scroll through.
     * @param string       Array of commands for interacting with posts (e.g., like, view).
     * @return String representing the scroll action details.
     */
    public static String scrollFeed(CustomHashMap<String, User> allUserHM, String userID, int numbercount, String[] string) {
        User user = allUserHM.get(userID);
        String forReturn = "";

        // Check if the user exists
        if (user == null) {
            return "Some error occurred in scroll_through_feed.\n";
        }

        forReturn = forReturn + userID + " is scrolling through feed:\n";
        generate_feed(allUserHM, userID, numbercount);

        for (int i = 3; i < numbercount + 3; i++) {
            if (user.Feed.isEmpty()) {
                forReturn = forReturn + "No more posts in feed.\n";
                return forReturn;
            }
            if (string[i].equals("0")) {
                Post k = user.Feed.removeMax();
                if (user.seenPost.containsKey(k.postId) || user.posts.contains(k)) {
                    i--;
                    continue;
                }
                user.seenPost.put(k.postId, k);
                forReturn = forReturn + userID + " saw " + k.postId + " while scrolling.\n";
            }
            if (string[i].equals("1")) {
                Post s = user.Feed.removeMax();
                if (user.seenPost.containsKey(s.postId) || user.posts.contains(s)) {
                    i--;
                    continue;
                }
                user.seenPost.put(s.postId, s);
                s.likes++;
                s.likedBy.put(user.id, user);
                forReturn = forReturn + userID + " saw " + s.postId + " while scrolling and clicked the like button.\n";
            }
        }
        return forReturn;
    }
    /**
     * Sorts the posts of a user in descending order of likes.
     *
     * @param allUserHM HashMap containing all users.
     * @param userID    ID of the user whose posts need to be sorted.
     * @return A string representing the sorted posts or an error message if sorting fails.
     */
    public static String sort_posts(CustomHashMap<String, User> allUserHM, String userID) {
        String forReturn = "";
        User user = allUserHM.get(userID);

        // Check if the user exists
        if (user == null) {
            return "Some error occurred in sort_posts.\n";
        }

        // Check if the user has any posts to sort
        if (user.posts.isEmpty()) {
            return "No posts from " + user.id + ".\n";
        }

        forReturn = forReturn + "Sorting " + user.id + "'s posts:\n";

        // Sort the user's posts using the quick sort method
        SortArrayList.quickSort(user.posts);

        // Append the sorted posts to the output string
        for (int i = user.posts.size() - 1; i >= 0; i--) {
            forReturn = forReturn + user.posts.get(i).postId + ", Likes: " + user.posts.get(i).likes + "\n";
        }
        return forReturn;
    }

    /**
     * Main method to run the program, process commands, and manage user interactions.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException if file operations fail.
     */
    public static void main(String[] args) throws IOException {
        double time = System.currentTimeMillis();
        CustomHashMap<String, User> allUserHM = new CustomHashMap<>();
        PriorityQueue<Post> allPosts = new PriorityQueue<>();
        CustomHashMap<String, Post> postHash = new CustomHashMap<>();

        File fh = new File(args[0]);
        File file1 = new File(args[1]);
        Scanner scanner = new Scanner(fh);
        FileWriter writer = new FileWriter(file1);

        while (scanner.hasNextLine()) {
            String[] array = scanner.nextLine().split(" ");
            if (array.length == 0 || array[0].trim().isEmpty()) {
                writer.write("Invalid command: Line is empty or invalid.\n");
            } else if (array[0].equals("create_user")) {
                String k = createUser(allUserHM, array[1]);
                writer.write(k + "\n");
            } else if (array[0].equals("follow_user")) {
                String x = follow_user(allUserHM, array[1], array[2]);
                writer.write(x + "\n");
            } else if (array[0].equals("unfollow_user")) {
                String f = unfollow_user(allUserHM, array[1], array[2]);
                writer.write(f + "\n");
            } else if (array[0].equals("create_post")) {
                String k = createPost(allUserHM, allPosts, postHash, array[1], array[2], array[3]);
                writer.write(k + "\n");
            } else if (array[0].equals("see_post")) {
                String z = see_post(allUserHM, postHash, array[1], array[2]);
                writer.write(z + "\n");
            } else if (array[0].equals("toggle_like")) {
                String toggle = toggle_like(allPosts, allUserHM, postHash, array[1], array[2]);
                writer.write(toggle + "\n");
            } else if (array[0].equals("see_all_posts_from_user")) {
                String seeAll = see_all_post(allUserHM, postHash, array[1], array[2]);
                writer.write(seeAll + "\n");
            } else if (array[0].equals("generate_feed")) {
                String generatefeed = generate_feed(allUserHM, array[1], Integer.parseInt(array[2]));
                writer.write(generatefeed);
            } else if (array[0].equals("scroll_through_feed")) {
                String scroll = scrollFeed(allUserHM, array[1], Integer.parseInt(array[2]), array);
                writer.write(scroll);
            } else if (array[0].equals("sort_posts")) {
                String sort = sort_posts(allUserHM, array[1]);
                writer.write(sort);
            }
        }
        writer.close();
    }
}
