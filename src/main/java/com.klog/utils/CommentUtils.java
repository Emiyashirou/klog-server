package com.klog.utils;

import com.klog.model.basic.Comment;

public class CommentUtils {

    public static boolean isValidComment(Comment comment){
        return comment != null;
    }

    public static boolean isValidCommentEdit(Comment comment){
        return comment != null
                && comment.getId() != null
                && comment.getId().length() != 0;
    }

    public static boolean isValidHideComment(Comment comment) {
        return comment != null
                && comment.getId() != null
                && comment.getId().length() != 0;
    }

    public static int getStatusOfOpen(){
        return 0;
    }

    public static int getStatusOfHidden(){
        return 1;
    }
}
