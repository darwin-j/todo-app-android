package com.example.todoapp.adapter;

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

import com.example.todoapp.NotificationHelper;
import com.example.todoapp.R;
import com.example.todoapp.db.DatabaseHelper;
import com.example.todoapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private DatabaseHelper db;
    private NotificationHelper notificationHelper;
    private int notificationIdCounter = 100; // unique id for notifications
    private String status;


    public TaskAdapter(Context context, List<Task> taskList, DatabaseHelper db) {
        this.context = context;
        this.taskList = taskList;
        this.db = db;
        this.notificationHelper = new NotificationHelper(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.txtTask.setText(task.getTask());

        holder.btnDelete.setOnClickListener(v -> {
            db.deleteTask(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());

            // Show notification
            notificationHelper.showNotification("Task Removed", task.getTask(), notificationIdCounter++);
            Toast.makeText(context, "Task removed", Toast.LENGTH_SHORT).show();
        });

        holder.btnComplete.setOnCheckedChangeListener(null); // prevent unwanted triggers during recycling
        holder.btnComplete.setChecked(task.getStatus() == 1); // 1 = completed, 0 = pending

        holder.btnComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;
            task.setStatus(newStatus);

            // Update database
            db.updateTask(task.getId(), task.getTask(), newStatus);

            // Update RecyclerView
            notifyItemChanged(position);

            // Notification
            String msg = isChecked ? "Task completed" : "Task marked pending";
            notificationHelper.showNotification(msg, task.getTask(), notificationIdCounter++);
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTask;
        ImageButton btnDelete;

        CheckBox btnComplete;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.txtTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}
