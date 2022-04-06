package com.galvanize.SoloP2;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Calendar;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    BookRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    @Transactional
    @Rollback
    public void addNewBookTest() throws Exception{
        this.mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Her\", \"publishDate\": \"2020-08-21\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.publishDate", is("2020-08-21")))
                .andExpect(jsonPath("$.name", is("Her")));
    }

    @Test
    @Transactional
    @Rollback
    public void getOneBookTest() throws Exception{
        //create a book to pull back
        Calendar herCalendar = Calendar.getInstance();
        herCalendar.set(2020, Calendar.AUGUST, 21);
        Book testBook = new Book("Her", herCalendar.getTime());
        Book record = repository.save(testBook);

        //test that pulling back the book you created works
        this.mvc.perform(get("/books/" + record.getId()))
                .andExpect(jsonPath("$.id", is(record.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Her")))
                .andExpect(jsonPath("$.publishDate", is("2020-08-21")));
    }

    @Test
    @Transactional
    @Rollback
    public void getAllBooks() throws Exception{
        Calendar herCalendar = Calendar.getInstance();
        herCalendar.set(2020, Calendar.AUGUST, 21);
        Book testBook = new Book("Her", herCalendar.getTime());
        Book record = this.repository.save(testBook);

        Calendar HimCalendar = Calendar.getInstance();
        HimCalendar.set(2021, Calendar.AUGUST, 21);
        Book testBook2 = new Book("Him", HimCalendar.getTime());
        Book record2 = this.repository.save(testBook2);

        Calendar HerVol2Calendar = Calendar.getInstance();
        HerVol2Calendar.set(2021, Calendar.AUGUST, 25);
        Book testBook3 = new Book("HerVol2", HerVol2Calendar.getTime());
        Book record3 = this.repository.save(testBook3);

        this.mvc.perform(get("/books"))
                .andExpect(jsonPath("$[0].id", is(record.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is("Her")))
                .andExpect(jsonPath("$[0].publishDate", is("2020-08-21")))

                .andExpect(jsonPath("$[1].id", is(record2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is("Him")))
                .andExpect(jsonPath("$[1].publishDate", is("2021-08-21")))

                .andExpect(jsonPath("$[2].id", is(record3.getId().intValue())))
                .andExpect(jsonPath("$[2].name", is("HerVol2")))
                .andExpect(jsonPath("$[2].publishDate", is("2021-08-25")));


    }

    @Test
    @Transactional
    @Rollback
    public void updateBookTest() throws Exception{
        Calendar herCalendar = Calendar.getInstance();
        herCalendar.set(2021, Calendar.AUGUST, 21);
        Book testBook = new Book("Her", herCalendar.getTime());
        Book record = this.repository.save(testBook);

        this.mvc.perform(patch("/books/" + record.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"HerVol2\", \"startDate\": \"2021-08-21\"}"))
                .andExpect(jsonPath("$.name", is("HerVol2")))
                .andExpect(jsonPath("$.publishDate", is("2021-08-21")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @Transactional
    @Rollback
    public void deleteOneBookTest() throws Exception{
        Calendar herCalendar = Calendar.getInstance();
        herCalendar.set(2020, Calendar.AUGUST, 21);
        Book testBook = new Book("Her", herCalendar.getTime());
        Calendar HimCalendar = Calendar.getInstance();
        HimCalendar.set(2021, Calendar.AUGUST, 21);
        Book testBook2 = new Book("Him", HimCalendar.getTime());
        Book record2 = this.repository.save(testBook2);

        this.mvc.perform(delete("/books/" + record2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Him\", \"startDate\": \"2021-08-21\"}"))
                .andExpect(jsonPath("$[1].name").doesNotHaveJsonPath());
    }

}
