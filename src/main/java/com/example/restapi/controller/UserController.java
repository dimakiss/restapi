package com.example.restapi.controller;

import com.example.restapi.model.Book;
import com.example.restapi.model.User;
import com.example.restapi.repository.BookRepository;
import com.example.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // Get all users with the books they borrowed
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // Create a new user
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        User savedUser = userRepository.save(user);
        //return ResponseEntity.ok("User created successfully with ID: " + savedUser.getId() + ", Name: " + savedUser.getUsername() + ", Email: " + savedUser.getEmail());
        return ResponseEntity.ok("User created successfully");
    }


    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    
    // Borrow a book
    @PostMapping("/{id}/borrow")
    public ResponseEntity<String> borrowBook(@PathVariable Integer id, @RequestBody Map<String, Integer> request) {
        Integer bookId = request.get("bookId");
        return userRepository.findById(id).map(user -> {
            if (user.getBorrowedBooks().size() >= 3) {
                return ResponseEntity.badRequest().body("User cannot borrow more than 3 books.");
            }

            return bookRepository.findById(bookId).map(book -> {
                if (!book.isAvailable()) {
                    return ResponseEntity.badRequest().body("Book is not available.");
                }

                user.getBorrowedBooks().add(book);
                book.setAvailable(false);
                userRepository.save(user);
                bookRepository.save(book);
                return ResponseEntity.ok("Book borrowed successfully.");
            }).orElse(ResponseEntity.notFound().build());

        }).orElse(ResponseEntity.notFound().build());
    }

    // Return a book
    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnBook(@PathVariable Integer id, @RequestBody Map<String, Integer> request) {
        Integer bookId = request.get("bookId");
        return userRepository.findById(id).map(user -> {
            return bookRepository.findById(bookId).map(book -> {
                if (!user.getBorrowedBooks().contains(book)) {
                    return ResponseEntity.badRequest().body("User has not borrowed this book.");
                }

                user.getBorrowedBooks().remove(book);
                book.setAvailable(true);
                userRepository.save(user);
                bookRepository.save(book);
                return ResponseEntity.ok("Book returned successfully.");
            }).orElse(ResponseEntity.notFound().build());

        }).orElse(ResponseEntity.notFound().build());
    }
    
    // Get library statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getLibraryStats() {
        int totalUsers = (int) userRepository.count();
        int totalBooks = (int) bookRepository.count();
        int availableBooks = (int) bookRepository.findByAvailableTrue().size();
        int borrowedBooks = totalBooks - availableBooks;

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalBooks", totalBooks);
        stats.put("availableBooks", availableBooks);
        stats.put("borrowedBooks", borrowedBooks);

        return ResponseEntity.ok(stats);
    }
}
