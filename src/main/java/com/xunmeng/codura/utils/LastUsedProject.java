package com.xunmeng.codura.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.IdeFrame;

public class LastUsedProject {
    public static Project getLastUsedProject(){
        IdeFrame lastFocusedFrame = IdeFocusManager.getGlobalInstance().getLastFocusedFrame();
        if (lastFocusedFrame!=null){
            return lastFocusedFrame.getProject();
        }
        Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
        if (openProjects!=null && openProjects.length>0){
            return openProjects[0];
        }
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        if (defaultProject!=null){
            return defaultProject;
        }
        return null;
    }
}
