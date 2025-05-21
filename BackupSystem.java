import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

class FileEntry {
    String filePath;
    LocalDateTime backupTime;

    public FileEntry(String filePath) {
        this.filePath = filePath;
        this.backupTime = LocalDateTime.now();
    }

    public void displayFileInfo() {
        System.out.println("File: " + filePath + " | Backup Time: " + backupTime);
    }
}

class BackupJob {
    List<FileEntry> files = new ArrayList<>();
    String backupLocation;

    public BackupJob(String backupLocation) {
        this.backupLocation = backupLocation;
    }

    public void addFile(String filePath) {
        files.add(new FileEntry(filePath));
    }

    public void performBackup() {
        System.out.println("Starting backup...");
        for (FileEntry f : files) {
            Path source = Paths.get(f.filePath);
            Path destination = Paths.get(backupLocation, source.getFileName().toString());
            try {
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Backed up: " + f.filePath);
            } catch (IOException e) {
                System.out.println("Failed to backup: " + f.filePath);
            }
        }
    }

    public void restoreFile(String fileName, String destinationPath) {
        Path source = Paths.get(backupLocation, fileName);
        Path destination = Paths.get(destinationPath, fileName);
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Restored: " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to restore: " + fileName);
        }
    }
}

public class BackupSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter backup directory path: ");
        String backupPath = sc.nextLine();

        BackupJob job = new BackupJob(backupPath);

        while (true) {
            System.out.println("\n--- Automatic Backup System ---");
            System.out.println("1. Add File to Backup");
            System.out.println("2. Perform Backup Now");
            System.out.println("3. Restore File");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter full path of file to backup: ");
                    String filePath = sc.nextLine();
                    job.addFile(filePath);
                    break;
                case 2:
                    job.performBackup();
                    break;
                case 3:
                    System.out.print("Enter file name to restore: ");
                    String restoreFile = sc.nextLine();
                    System.out.print("Enter destination path: ");
                    String restorePath = sc.nextLine();
                    job.restoreFile(restoreFile, restorePath);
                    break;
                case 4:
                    System.out.println("Exiting system. Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
