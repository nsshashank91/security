package com.shash.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shash.model.Book;
import com.shash.model.UserRegistration;

@RestController
public class BooksController {

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;
	
	@CrossOrigin(origins = "http://localhost:4200")
	 @RequestMapping("/user")
	  public Principal user(Principal user) {
	    return user;
	  }
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegister(@RequestBody UserRegistration userRegistrationObject) {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		String encodedPassword = bCryptPasswordEncoder.encode(userRegistrationObject.getPassword());

		User user = new User(userRegistrationObject.getUsername(), encodedPassword, authorities);
		
		jdbcUserDetailsManager.createUser(user);
		return user.getUsername()+" is successfully registered";
	}

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/books")
    public Map<String, Book[]> home() {
        Map<String, Book[]> model = new HashMap<String, Book[]>();
        model.put("content", getListOfBooks());
        return model;
    }

    private Book[] getListOfBooks() {
        // Create few books
        Book book1 = new Book("Chinua Achebe", "Nigeria",
                "https://upload.wikimedia.org/wikipedia/en/6/65/ThingsFallApart.jpg", "English",
                "https://en.wikipedia.org/wiki/Things_Fall_Apart", 209, "Things Fall Apart", 1958);

        Book book2 = new Book("Hans Christian Andersen", "Denmark",
                "https://upload.wikimedia.org/wikipedia/commons/5/5b/Hans_Christian_Andersen_%281834_painting%29.jpg", "Danish",
                "https://en.wikipedia.org/wiki/Fairy_Tales_Told_for_Children._First_Collection", 784, "Fairy tales", 1836);

        Book book3 = new Book("Dante Alighieri", "Italy",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Michelino_DanteAndHisPoem.jpg/450px-Michelino_DanteAndHisPoem.jpg",
                "Italian", "https://en.wikipedia.org/wiki/Divine_Comedy", 1928, "The Divine Comedy", 1315);

        return new Book[]{book1, book2, book3};
    }
}
