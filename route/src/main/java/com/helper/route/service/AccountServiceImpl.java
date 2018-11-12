package com.helper.route.service;

import com.helper.route.entity.AccountEntity;
import com.helper.route.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/11 - 上午10:33
 * Created by IntelliJ IDEA.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountEntity findById(Long id){
        Optional<AccountEntity> accountEntity = accountRepository.findById(id);
        return accountEntity.get();
    }

    @Override
    public Page<AccountEntity> findAll(Example<AccountEntity> example, Pageable pageable){
        Page<AccountEntity> accountEntityList = accountRepository.findAll(example,pageable);
        return accountEntityList;
    }

    @Override
    public AccountEntity findByAccount(String account){
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccount(account);
        //将匹配对象封装成Example对象
        Example<AccountEntity> example =Example.of(accountEntity);
        return findByOne(example);
    }

    @Override
    public AccountEntity findByOne(Example<AccountEntity> example){
        Optional<AccountEntity> optional = accountRepository.findOne(example);
        return optional.get();
    }
}
