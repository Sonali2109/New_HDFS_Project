package com.example.hdfs.controller;

import com.example.hdfs.dto.User;
import com.example.hdfs.repository.UserManager;
import com.example.hdfs.service.HDFSOperations;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();

        while (true) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (!userManager.authenticate(username, password)) {
                System.out.println("X Authentication failed. Exiting...");
                return;
            }

            User user;
            try {
                user = userManager.getUser(username);
            } catch (Exception e) {
                System.out.println("X Error creating user: " + e.getMessage());
                return;
            }

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Create a folder");
                System.out.println("2. Upload a file (encrypted)");
                System.out.println("3. Upload a file to a specific folder (encrypted)");
                System.out.println("4. Download a file");
                System.out.println("5. Delete a file");
                System.out.println("6. Delete a folder");
                System.out.println("7. List files and folders");
                System.out.println("8. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter folder name: ");
                        String folderName = scanner.nextLine();
                        HDFSOperations.createFolder(folderName); // Call HDFSOperations
                        break;

                    case 2:
                        System.out.print("Enter file path to upload: ");
                        String filePath = scanner.nextLine();

                        System.out.print("Enter HDFS destination path: ");
                        String hdfsDestinationFolder = scanner.nextLine();

                        System.out.print("Enter the name for the uploaded file: ");
                        String uploadedFileName = scanner.nextLine();

                        // Call the upload method with the new parameters
                        HDFSOperations.uploadFile(filePath, hdfsDestinationFolder, uploadedFileName, user);
                        break;

                    case 3:
                        System.out.print("Enter folder name to upload to: ");
                        String targetFolder = scanner.nextLine();
                        System.out.print("Enter file path to upload: ");
                        String fileToUpload = scanner.nextLine();
                        System.out.print("Enter the name for the uploaded file: ");
                        String uploadedFileNameSpecific = scanner.nextLine();

                        HDFSOperations.uploadFile(fileToUpload, targetFolder, uploadedFileNameSpecific, user);
                        break;

                    case 4:
                        System.out.print("Enter HDFS file path to download: ");
                        String hdfsFilePath = scanner.nextLine();
                        System.out.print("Enter local path to save the downloaded file: ");
                        String localDownloadPath = scanner.nextLine();

                        HDFSOperations.downloadFile(hdfsFilePath, localDownloadPath, user);
                        break;

                    case 5:
                        System.out.print("Enter HDFS file path to delete: ");
                        String fileToDelete = scanner.nextLine();
                        HDFSOperations.deleteFile(fileToDelete);
                        break;

                    case 6:
                        System.out.print("Enter HDFS folder path to delete: ");
                        String folderToDelete = scanner.nextLine();
                        HDFSOperations.deleteFolder(folderToDelete);
                        break;

                    case 7:
                        System.out.print("Enter HDFS path to list files and folders: ");
                        String hdfsPathToList = scanner.nextLine();
                        HDFSOperations.listFilesAndFolders(hdfsPathToList);
                        break;

                    case 8:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}
