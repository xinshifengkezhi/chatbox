package com.luoxue.account_data.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.luoxue.account_data.controller.utrrls.Result;
import com.luoxue.account_data.domain.Accounts;
import com.luoxue.account_data.services.accService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/Accounts")
public class AccountController {

    @Autowired
    private accService accser;

    @GetMapping
    public Result getAll(){
        return new Result(true, accser.getAll());
    }

    @PostMapping
    public Result save(@RequestBody Accounts accounts) {

        return new Result(accser.save(accounts));
    }

    @PutMapping
    public Result update(@RequestBody Accounts accounts) {
        return new Result(accser.updata(accounts));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return new Result(accser.delect(id));
    }

    @PostMapping("/login")
    public Result getByNum(@RequestBody Accounts account) {
        Result rs = accser.getByNum(account.getAccountNum(), account.getPassword());
        if (rs.getFlag()) {
            if(rs.getData() !=null)
                return rs;   // 登录成功，返回用户信息
            else{
                rs.setMsg("密码错误");
            }
        } else {
            rs.setMsg("不存在的账号"); // 登录失败
        }
        return rs;
    }

    @GetMapping("/{id}")
    public Result getIdUser(@PathVariable Integer id){
        return new Result(accser.getById(id));
    }

    @GetMapping("/login/{accountNum}")
    public Result getUser(@PathVariable String accountNum){
        return new Result(true, accser.getuser(accountNum));
    }

    @GetMapping("/page/{pageno}/{pageSize}")
    public Result getPage(@PathVariable int pageno, @PathVariable int pageSize,Accounts acc) {
        IPage<Accounts> page = accser.getPage(pageno, pageSize, acc);
        if(pageno > page.getPages()){
            page = accser.getPage((int)page.getPages(), pageSize, acc);
        }
        return new Result(true, page);
    }
}
