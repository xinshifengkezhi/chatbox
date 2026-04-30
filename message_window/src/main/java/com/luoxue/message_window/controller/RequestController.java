package com.luoxue.message_window.controller;


import com.luoxue.message_window.controller.utrls.RequestResult;
import com.luoxue.message_window.domain.AddRequest;
import com.luoxue.message_window.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/AddRequest")
public class RequestController {
    @Autowired
    private RequestService requestSer;

    @PostMapping
    public RequestResult save(@RequestBody AddRequest request){
        return new RequestResult(requestSer.save(request));
    }

    @DeleteMapping("/{requestId}/{receiverId}")
    public RequestResult delete(@PathVariable Integer requestId,
                                @PathVariable Integer receiverId){
        return new RequestResult(requestSer.delete(requestId, receiverId));
    }

    @GetMapping("/{receiverId}")
    public RequestResult getReq(@PathVariable Integer receiverId){
        return new RequestResult(true, requestSer.getRequest(receiverId));
    }

}
