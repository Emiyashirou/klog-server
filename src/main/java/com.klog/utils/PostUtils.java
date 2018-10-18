package com.klog.utils;

import com.klog.model.basic.Post;
import com.klog.model.input.GetPostListInput;

public class PostUtils {

    public static boolean isValidPost(Post post){
        return post != null;
    }

    public static boolean isValidPostListInput(GetPostListInput input){
        return input == null
                || input.getWorkId() == null
                || input.getWorkId().length() == 0
                || (input.getWorkId() != null && input.getWorkId().length() != 0);
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
