package com.example.todoapp.adapter;

// Android imports
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// App imports
import com.example.todoapp.NotificationHelper;
import com.example.todoapp.R;
import com.example.todoapp.db.DatabaseHelper;
import com.example.todoapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // Context & data
    private Context context;          // Activity context
    private List<Task> taskList;      // List of tasks to display
    private DatabaseHelper db;        // DB helper to update/delete tasks
    private NotificationHelper notificationHelper; // For showing notifications
    private int notificationIdCounter = 100;       // Unique ID for notifications

    // Constructor
    public TaskAdapter(Context context, List<Task> taskList, DatabaseHelper db) {
        this.context = context;
        this.taskList = taskList;
        this.db = db;
        this.notificationHelper = new NotificationHelper(context); // Initialize notification helper
    }

    // Inflate item layout and create ViewHolder
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);          // Get current task
        holder.txtTask.setText(task.getTask());      // Set task text

        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            db.deleteTask(task.getId());            // Delete from DB
            taskList.remove(position);              // Remove from list
            notifyItemRemoved(position);            // Notify RecyclerView
            notifyItemRangeChanged(position, taskList.size());

            // Show notification & toast
            notificationHelper.showNotification("Task Removed", task.getTask(), notificationIdCounter++);
            Toast.makeText(context, "Task removed", Toast.LENGTH_SHORT).show();
        });

        // Reset listener to prevent unwanted triggers during recycling
        holder.btnComplete.setOnCheckedChangeListener(null);

        // Set checkbox state based on task status (1 = completed)
        holder.btnComplete.setChecked(task.getStatus() == 1);

        // Handle checkbox state change
        holder.btnComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;      // 1 = completed, 0 = pending
            task.setStatus(newStatus);               // Update task object

            db.updateTask(task.getId(), task.getTask(), newStatus); // Update DB
            notifyItemChanged(position);             // Update RecyclerView

            // Show notification
            String msg = isChecked ? "Task completed" : "Task marked pending";
            notificationHelper.showNotification(msg, task.getTask(), notificationIdCounter++);
        });
    }

    // Return number of items
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder class to hold references to UI elements of each item
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTask;     // Task description
        ImageButton btnDelete; // Delete button
        CheckBox btnComplete; // Checkbox to mark complete/pending

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.txtTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}
