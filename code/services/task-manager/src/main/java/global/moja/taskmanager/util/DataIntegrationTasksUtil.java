package global.moja.taskmanager.util;

import global.moja.taskmanager.models.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataIntegrationTasksUtil {

    private static DataIntegrationTasksUtil instance;

    private Set<Task> tasks = new HashSet<>();
    private Map<Long, Integer> tasksLevels = new HashMap<>();
    private Map<Long, Integer> taskLevelHandledIssuesCount = new HashMap<>();

    private DataIntegrationTasksUtil(){}

    public static DataIntegrationTasksUtil instance(){
        if(instance == null) { instance = new DataIntegrationTasksUtil();}
        return instance;
    }

    public void insertTask(Task task) {
        tasks.add(task);
    }

    public Task retrieveTask(Long taskId) {
        return tasks
                .stream()
                .filter(task -> task.getId().equals(taskId))
                .findAny()
                .orElse(null);
    }

    public void updateTask(Task task) {
        tasks.remove(task);
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void insertTaskLevel(Long taskId, Integer level) {
        tasksLevels.put(taskId, level);
    }


    public Integer retrieveTaskLevel(Long taskId) {
        return tasksLevels.get(taskId);
    }

    public void incrementTaskLevel(Long taskId) {
        if(tasksLevels.get(taskId) != null){
            tasksLevels.put(taskId, tasksLevels.get(taskId) + 1);
        }
    }

    public void removeTaskLevel(Long taskId) {
        tasksLevels.remove(taskId);
    }

    public void initializeTaskLevelHandledIssuesCount(Long taskId) {
        taskLevelHandledIssuesCount.put(taskId, 0);
    }


    public Integer retrieveTaskLevelHandledIssuesCount(Long taskId) {
        return taskLevelHandledIssuesCount.get(taskId);
    }

    public void incrementTaskLevelHandledIssuesCount(Long taskId) {
        if(taskLevelHandledIssuesCount.get(taskId) != null){
            taskLevelHandledIssuesCount.put(taskId, taskLevelHandledIssuesCount.get(taskId) + 1);
        }
    }

    public void removeTaskLevelHandledIssuesCount(Long taskId) {
        taskLevelHandledIssuesCount.remove(taskId);
    }
    
}
