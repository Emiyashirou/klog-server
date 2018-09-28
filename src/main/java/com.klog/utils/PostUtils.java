package com.klog.utils;

import com.klog.model.basic.Post;

public class PostUtils {

    public static boolean isValidPost(Post post){
        return post != null;
    }

    public static boolean isValidPostEdit(Post post){
        return post != null;
    }

    public static boolean isValidArchivePost(Post post){
        return post != null
                && post.getId() != null
                && post.getId().length() != 0;
    }

    public static int getStatusOfActive() {
        return 0;
    }

    public static int getStatusOfArchive() {
        return 1;
    }
}
