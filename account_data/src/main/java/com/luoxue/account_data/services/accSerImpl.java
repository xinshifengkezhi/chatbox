package com.luoxue.account_data.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoxue.account_data.controller.utrrls.Result;
import com.luoxue.account_data.domain.Accounts;
import com.luoxue.account_data.mapper.AccountMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class accSerImpl implements accService {

    @Autowired
    private AccountMapper account;

    @Override
    public Boolean save(Accounts acc) {
        return account.insert(acc) > 0;
    }

    @Override
    public Boolean updata(Accounts acc) {
        return account.updateById(acc) > 0;
    }

    @Override
    public Boolean delect(Integer id) {
        return account.deleteById(id) > 0;
    }

    @Override
    public Result getByNum(String accountNum, String password) {
        LambdaQueryWrapper<Accounts> wrapper = new LambdaQueryWrapper<>();
        Result rs = new Result();
        wrapper.eq(Accounts::getAccountNum, accountNum);
        Accounts user = account.selectOne(wrapper);
        if(user == null){
            rs.setFlag(false);
            rs.setData(null);
        }else{
            rs.setFlag(true);
            if (user.getPassword().equals(password)) {
                rs.setData(user);
            } else {
                rs.setData(null);
            }
        }
        return rs;
    }

    @Override
    public Boolean getById(Integer id){
        return account.selectById(id) != null;
    }

    @Override
    public List<Accounts> getuser(String accNum){
        LambdaQueryWrapper<Accounts> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Accounts::getAccountNum, accNum);
        return account.selectList(wrapper);
    };

    @Override
    public List<Accounts> getAll() {
        return account.selectList(null);
    }

    @Override
    public IPage<Accounts> getPage(int pageNo, int pageSize, Accounts acc) {
        LambdaQueryWrapper<Accounts> wrapper = new LambdaQueryWrapper<Accounts>();
        if (acc.getId() > 0) {
            wrapper.eq(Accounts::getId, acc.getId());
        }
        if (acc.getAccountNum() != null && !acc.getAccountNum().isEmpty()) {
            wrapper.like(Accounts::getAccountNum, acc.getAccountNum());
        }
        if (acc.getUsername() != null && !acc.getUsername().isEmpty()) {
            wrapper.like(Accounts::getUsername, acc.getUsername());
        }
        IPage page = new Page(pageNo, pageSize);
        account.selectPage(page, wrapper);
        return page;
    }
}
