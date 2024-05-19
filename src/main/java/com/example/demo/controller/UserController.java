package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

@RestController
@ResponseBody
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> findById() {
        UserDto userDto = UserDto.builder()
                .email("qwe")
                .build();
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        UserDto savedUserDto = userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
    }

    @GetMapping("/search/")
    public ResponseEntity<PagedModel<UserDto>> searchUsers(@RequestParam String fromDateString,
                                                           @RequestParam String toDateString,
                                                           Pageable pageable) {
        LocalDate fromDate = LocalDate.parse(fromDateString);
        LocalDate toDate = LocalDate.parse(toDateString);
        Page<UserDto> users = userService.findByBirthDateBetween(fromDate, toDate, pageable);
        PagedModel<UserDto> userDtoPagedModel = PagedModel.of(users.getContent(), new PagedModel.PageMetadata(users.getSize(), users.getNumber(), users.getTotalElements()));
        if (users.hasPrevious()) {
            String prevLink = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", users.getNumber() - 1)
                    .replaceQueryParam("size", users.getSize())
                    .toUriString();
            userDtoPagedModel.add(Link.of(prevLink, "prev"));
        }
        if (users.hasNext()) {
            String nextLink = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", users.getNumber() + 1)
                    .replaceQueryParam("size", users.getSize())
                    .toUriString();
            userDtoPagedModel.add(Link.of(nextLink, "next"));
        }
        return ResponseEntity.ok(userDtoPagedModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserDto> updateCompletely(@PathVariable Integer id, @RequestBody UserDto userDto) {
        userService.updateCompletely(id, userDto);
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserDto> updatePartially(@PathVariable Integer id, @RequestBody UserDto userDto) {
        UserDto responceUserDto = userService.updatePartially(id, userDto);
        return ResponseEntity.ok(responceUserDto);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}
