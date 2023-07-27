package com.pizzu.graphpdf.controller;

import com.pizzu.graphpdf.entity.Author;
import com.pizzu.graphpdf.entity.Book;
import com.pizzu.graphpdf.repository.AuthorRepository;
import com.pizzu.graphpdf.repository.BookRepository;
import com.pizzu.graphpdf.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Controller
public class BookResolver {

    private BookRepository bookrepository;
    private AuthorRepository authorRepository;

    private ReportService service;

    @Autowired
    public BookResolver(BookRepository bookrepository, AuthorRepository authorRepository, ReportService service) {
        this.bookrepository = bookrepository;
        this.authorRepository = authorRepository;
        this.service = service;
    }

    @QueryMapping
    public List<Book> getAllBooks() {
        List<Book> result = bookrepository.findAll();
        return result;
    }


    @QueryMapping
    public List<Author> getAllAuthor() {
        List<Author> result = authorRepository.findAll();
        return result;
    }

    @QueryMapping
    public List<Book> getGenreBook(@Argument String genre) {
        List<Book> result = bookrepository.getBooksByGenre(genre);
        return result;
    }

    @QueryMapping
    public String generateReport(@Argument String format) throws JRException, FileNotFoundException {
        return service.exportReport(format);
    }

    @Transactional
    @MutationMapping
    public Book insertNewBook(@Argument Book book) {
        //!authorRepository.findById(book.getAuthor().getId()).isPresent()
        if (book.getAuthor() != null && !authorRepository.findById(book.getAuthor().getId()).isPresent()) {
            authorRepository.save(book.getAuthor());
        } else {
            book.setAuthor(authorRepository.findById(book.getAuthor().getId()).get());
        }

        Book result = bookrepository.saveAndFlush(book);

        Book resultUpdated = bookrepository.findById(result.getId()).get();
        return resultUpdated;
    }

    @Transactional
    @MutationMapping
    public String deleteBook(@Argument Long id) {
        //!authorRepository.findById(book.getAuthor().getId()).isPresent()
        if (bookrepository.findById(id).isPresent()) {
            bookrepository.deleteById(id);
            return "ok";
        }
        else
            return "No book with id " + id;
    }

}
