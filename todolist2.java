import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Task class represents a task with description, completion status, and due date
class Task {
    private String description;
    private boolean completed;
    private Date dueDate;

    // Constructor to initialize a task with description and due date
    public Task(String description, Date dueDate) {
        this.description = description;
        this.completed = false;
        this.dueDate = dueDate;
    }

    // Getter methods for task attributes
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

    // toString method to represent the task as a string
    @Override
    public String toString() {
        String status = completed ? "Completed" : "Pending";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(dueDate);
        return description + " - " + status + ", Due: " + formattedDate;
    }

    // equals and hashCode methods for task comparison
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

// todolist2 class manages a list of tasks without undo and redo functionality
public class todolist2 {
    private List<Task> tasks;

    // Constructor to initialize the task list
    public todolist2() {
        tasks = new ArrayList<>();
    }

    // Method to add tasks to the list
    public void addTasks(List<Task> newTasks) {
        tasks.addAll(newTasks);
    }

    // Method to mark a task as completed
    public void markTaskCompleted(String description) {
        for (Task task : tasks) {
            if (task.getDescription().equals(description)) {
                task.markCompleted();
                System.out.println("Successfully marked task as completed.");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    // Method to delete a task
    public void deleteTask(String description) {
        if (tasks.removeIf(task -> task.getDescription().equals(description))) {
            System.out.println("Successfully deleted task.");
        } else {
            System.out.println("Task not found.");
        }
    }

    // Methods to get different sets of tasks
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

    // Main method for user interaction
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
            System.out.println("7. Exit");
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
                                System.out.print("Enter due date for '" + taskDescription + "' (yyyy-MM-dd or 'today'): ");
                                String dueDateStr = scanner.nextLine();
                                try {
                                    Date dueDate;
                                    if (dueDateStr.equalsIgnoreCase("today")) {
                                        dueDate = new Date(); // Set the due date to today
                                    } else {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        dateFormat.setLenient(false); // Make date validation strict
                                        dueDate = dateFormat.parse(dueDateStr);
                                        Date currentDate = new Date();
                                        if (dueDate.before(currentDate)) {
                                            System.out.println("Due date cannot be in the past.");
                                            continue; // Continue the loop to re-enter the date
                                        }
                                    }
                                    newTasks.add(new Task(taskDescription, dueDate));
                                    break; // Valid date, exit the loop
                                } catch (ParseException e) {
                                    System.out.println("Invalid date format or invalid date. Please use the format 'yyyy-MM-dd' or 'today' and ensure the date is valid.");
                                }
                            }
                        }
                    }
                    manager.addTasks(newTasks);
                    System.out.println("Successfully added task(s).");
                    break; // Valid date, exit the loop
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
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to display tasks
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
