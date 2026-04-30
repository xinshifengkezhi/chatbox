package com.luoxue.message_window.controller;

import com.luoxue.message_window.controller.utrls.RelatResult;
import com.luoxue.message_window.domain.UserRelations;
import com.luoxue.message_window.services.RelatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/Relations")
public class RelationsController {

    @Autowired
    private RelatService relatSer;

    @PostMapping
    public RelatResult save(@RequestBody UserRelations relation){
        return new RelatResult(relatSer.save(relation));
    }

    @PutMapping
    public RelatResult update(@RequestBody UserRelations relation) {
        Boolean success = relatSer.update(relation.getFriendId(), relation.getUsername());
        return new RelatResult(success);
    }

    @DeleteMapping("/{selfId}/{friendId}")
    public RelatResult delRelation(@PathVariable Integer selfId,
                                 @PathVariable Integer friendId){
        return new RelatResult(relatSer.delete(selfId, friendId));
    }

    @DeleteMapping("/{selfId}")
    public RelatResult delRelation(@PathVariable Integer selfId){
        return new RelatResult(relatSer.deleteAll(selfId));
    }

    @GetMapping("/{selfId}")
    public RelatResult getRelation(@PathVariable Integer selfId){
        return new RelatResult(true, relatSer.getFriends(selfId));
    }

    @GetMapping("/{selfId}/{friendId}")
    public RelatResult getRelation(@PathVariable Integer selfId,
                                   @PathVariable Integer friendId){
        return new RelatResult(relatSer.getFriend(selfId, friendId));
    }

    @GetMapping("/login/{username}")
    public RelatResult chatFriends(@PathVariable String username){
        return new RelatResult(true, relatSer.chatFriends(username));
    }

}
