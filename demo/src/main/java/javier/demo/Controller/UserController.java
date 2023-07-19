package javier.demo.Controller;

import javier.demo.Entity.User;
import javier.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create nuevo user
    @PostMapping
    public ResponseEntity<?> create (@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));

    }
    //read user
    @GetMapping("/{id}")
    public ResponseEntity<?> read (@PathVariable(value = "id") Long userid) {
        Optional<User> oUser = userService.findById(userid);

        if(!oUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oUser);
    }

    //update an user
    @PutMapping("/{id}")
    public ResponseEntity<?> update (@RequestBody User userDetails, @PathVariable (value = "Id") Long userId) {
        Optional<User> user = userService.findById(userId);

        if(!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        user.get().setName(userDetails.getName());
        user.get().setSurname(userDetails.getSurname());
        user.get().setEmail(userDetails.getEmail());
        user.get().setEnabled(userDetails.getEnabled());

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.get()));
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (@PathVariable (value = "Id") Long userId) {

        if(!userService.findById(userId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    //Read all users
    @GetMapping
    public List<User> readAll () {
        List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false).collect(Collectors.toList());

        return users;
    }

}