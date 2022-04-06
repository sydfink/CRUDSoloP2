package com.galvanize.SoloP2;


import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/books")
    public Iterable<Book> getAllEmployees(){
        return repository.findAll();
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book){
        return repository.save(book);
    }

    @GetMapping("/books/{id}")
    public Optional<Book> getOneBook (@PathVariable Long id){
        return repository.findById(id);
    }

    @PatchMapping("/books/{id}")
    public Book updateOneBook(@PathVariable Long id, @RequestBody Map<String, String> books){
        //retrieve book that needs updated
        Book oldBook = this.repository.findById(id).get();
        //check passed in variable info to update book
        books.forEach((key, value) -> {
            if (key.equals("name")){
                oldBook.setName(value);
            } else if ((key.equals("publishDate"))) {
                oldBook.setPublishDate(oldBook.getPublishDate());
            }
        });
        //save changed entry for book
        return this.repository.save(oldBook);
    }

    @DeleteMapping("/books/{id}")
    public void deleteOneBook(@PathVariable Long id){
        repository.deleteById(id);
    }

}
