package com.luoxue.account_data.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.luoxue.account_data.domain.Accounts;
import com.luoxue.account_data.controller.utrrls.Result;

import java.util.List;

public interface accService {
    Boolean save(Accounts acc);
    Boolean updata(Accounts acc);
    Boolean delect(Integer id);

    Result getByNum(String accountNum, String password);
    Boolean getById(Integer id);

    List<Accounts> getuser(String accNum);

    List<Accounts> getAll();

    IPage<Accounts> getPage(int pageNo, int pageSize, Accounts acc);
}
