/*
 * UserController.java
 *
 * Copyright (c) 2018, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.example.spring.cloud.ms.user.web;

import org.nerdcoding.example.spring.cloud.ms.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final List<User> IN_MEMORY_DB = new ArrayList<>(Arrays.asList(
            new User.Builder()
                    .withId(System.nanoTime())
                    .withFirstName("Bud")
                    .withLastName("Spencer")
                    .withEmail("bud.spencer@gmail.com")
                    .withUsername("bspencer")
                    .withPassword("stdpsw987").build(),
            new User.Builder()
                    .withId(System.nanoTime())
                    .withFirstName("Terence")
                    .withLastName("Hill")
                    .withEmail("terence.hill@gmail.com")
                    .withUsername("thill")
                    .withPassword("stdpsw987").build()
    ));

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(IN_MEMORY_DB, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") final Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return IN_MEMORY_DB.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(foundUser -> new ResponseEntity<>(foundUser, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody final User user) {
        if (user == null
                || user.getFirstName() == null
                || user.getLastName() == null
                || user.getEmail() == null
                || user.getUsername() == null
                || user.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        IN_MEMORY_DB.add(new User.Builder()
                .withId(System.nanoTime())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withUsername(user.getEmail())
                .withPassword(user.getPassword()).build()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") final Long id, @RequestBody final User updatedUser) {
        if (id == null
                || updatedUser == null
                || updatedUser.getFirstName() == null
                || updatedUser.getLastName() == null
                || updatedUser.getEmail() == null
                || updatedUser.getUsername() == null
                || updatedUser.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (IN_MEMORY_DB.removeIf(user -> user.getId().equals(id))) {
            IN_MEMORY_DB.add(new User.Builder()
                    .withId(id)
                    .withFirstName(updatedUser.getFirstName())
                    .withLastName(updatedUser.getLastName())
                    .withEmail(updatedUser.getEmail())
                    .withUsername(updatedUser.getEmail())
                    .withPassword(updatedUser.getPassword()).build()
            );
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") final Long id) {
        if (IN_MEMORY_DB.removeIf(user -> user.getId().equals(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
