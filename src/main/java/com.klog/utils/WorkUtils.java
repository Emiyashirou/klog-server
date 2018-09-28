package com.klog.utils;

import com.klog.model.basic.Work;
import com.klog.model.input.AddIntoWorkInput;
import com.klog.model.input.RemoveFromWorkInput;

public class WorkUtils {

    public static boolean isValidWork(Work work){
        return work != null;
    }

    public static boolean isValidWorkEdit(Work work) {
        return work != null;
    }

    public static boolean isValidAddIntoWork(AddIntoWorkInput input){
        return input != null
                && input.getPostId() != null
                && input.getPostId().length() != 0
                && input.getWorkId() != null
                && input.getWorkId().length() != 0;
    }

    public static boolean isValidRemoveFromWork(RemoveFromWorkInput input){
        return input != null
                && input.getPostId() != null
                && input.getPostId().length() != 0
                && input.getWorkId() != null
                && input.getWorkId().length() != 0;
    }

    public static boolean isValidArchiveWork(Work work) {
        return work != null
                && work.getId() != null
                && work.getId().length() != 0;
    }

    public static int getStatusOfActive(){
        return 0;
    }

    public static int getStatusOfArchive(){
        return 1;
    }
}
