package com.example.vfrin.valentijnfrinkingpset5;

public class TodoItem {
    private String title;
    private boolean completed;

    //    Constructor
    public TodoItem(String title){
        this.title = title;
        completed = false;
    }

    //    Returns the title of the TodoItem
    public String getTitle(){
        return this.title;
    }

    //    Returns the completed status of the TodoItem
    public boolean getCompleted(){
        return this.completed;
    }

    //    Sets the completed status of the TodoItem
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //    Switches the completed status from true to false or vice-versa
    public void switchCompleted() {
        if(completed) {
            completed = false;
        } else {
            completed = true;
        }
    }
}