package k;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Task {
    private String description;
    private boolean completed;
    private Date dueDate;

    public Task(String description, Date dueDate) {
        this.description = description;
        this.completed = false;
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }

    public void markPending() {
        completed = false;
    }

    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        String status = completed ? "Completed" : "Pending";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(dueDate);
        return description + " - " + status + ", Due: " + formattedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(description, task.description) && Objects.equals(dueDate, task.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, dueDate);
    }
}

public class todolist2 {
    private List<Task> tasks;
    private Deque<List<Task>> undoStack;
    private Deque<List<Task>> redoStack;

    public todolist2() {
        tasks = new ArrayList<>();
        undoStack = new LinkedList<>();
        redoStack = new LinkedList<>();
    }

    public void addTasks(List<Task> newTasks) {
        saveState(); // Save state before making changes
        tasks.addAll(newTasks);
    }

    public void markTaskCompleted(String description) {
        saveState(); // Save state before making changes
        for (Task task : tasks) {
            if (task.getDescription().equals(description)) {
                task.markCompleted();
                return;
            }
        }
    }

    public void deleteTask(String description) {
        saveState(); // Save state before making changes
        tasks.removeIf(task -> task.getDescription().equals(description));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Task> getCompletedTasks() {
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    public List<Task> getPendingTasks() {
        List<Task> pendingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                pendingTasks.add(task);
            }
        }
        return pendingTasks;
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new ArrayList<>(tasks)); // Save the current state for redo
            tasks = new ArrayList<>(undoStack.pop()); // Restore the previous state
        } else {
            System.out.println("Undo not available.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new ArrayList<>(tasks)); // Save the current state for undo
            tasks = new ArrayList<>(redoStack.pop()); // Restore the next state
        } else {
            System.out.println("Redo not available.");
        }
    }

    private void saveState() {
        undoStack.push(new ArrayList<>(tasks)); // Save the current state for undo
        redoStack.clear(); // Clear redo stack when a new action is performed
    }

    public static void main(String[] args) {
        todolist2 manager = new todolist2();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Task(s)");
            System.out.println("2. Mark Task as Completed");
            System.out.println("3. Delete Task");
            System.out.println("4. View All Tasks");
            System.out.println("5. View Completed Tasks");
            System.out.println("6. View Pending Tasks");
            System.out.println("7. Undo");
            System.out.println("8. Redo");
            System.out.println("9. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (option) {
                case 1:
                    System.out.print("Enter task(s) separated by commas: ");
                    String taskInput = scanner.nextLine();
                    String[] taskDescriptions = taskInput.split(",");
                    List<Task> newTasks = new ArrayList<>();
                    for (String taskDescription : taskDescriptions) {
                        taskDescription = taskDescription.trim();
                        if (!taskDescription.isEmpty()) {
                            while (true) {
                                System.out.print("Enter due date for '" + taskDescription + "' (yyyy-MM-dd): ");
                                String dueDateStr = scanner.nextLine();
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    dateFormat.setLenient(false); // Make date validation strict
                                    Date dueDate = dateFormat.parse(dueDateStr);
                                    Date currentDate = new Date();
                                    if (dueDate.after(currentDate)) {
                                        newTasks.add(new Task(taskDescription, dueDate));
                                        break; // Valid date, exit the loop
                                    } else {
                                        System.out.println("Due date cannot be in the past.");
                                    }
                                } catch (ParseException e) {
                                    System.out.println("Invalid date format or invalid date. Please use the format 'yyyy-MM-dd' and ensure the date is valid.");
                                }
                            }
                        }
                    }
                    manager.addTasks(newTasks);
                    break;
                case 2:
                    System.out.print("Enter task description to mark as completed: ");
                    String completedDescription = scanner.nextLine();
                    manager.markTaskCompleted(completedDescription);
                    break;
                case 3:
                    System.out.print("Enter task description to delete: ");
                    String deleteDescription = scanner.nextLine();
                    manager.deleteTask(deleteDescription);
                    break;
                case 4:
                    List<Task> allTasks = manager.getAllTasks();
                    displayTasks("All Tasks", allTasks);
                    break;
                case 5:
                    List<Task> completedTasks = manager.getCompletedTasks();
                    displayTasks("Completed Tasks", completedTasks);
                    break;
                case 6:
                    List<Task> pendingTasks = manager.getPendingTasks();
                    displayTasks("Pending Tasks", pendingTasks);
                    break;
                case 7:
                    manager.undo();
                    break;
                case 8:
                    manager.redo();
                    break;
                case 9:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayTasks(String title, List<Task> tasks) {
        System.out.println("\n" + title + ":");
        if (tasks.isEmpty()) {
            System.out.println("No tasks to display.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }
}
