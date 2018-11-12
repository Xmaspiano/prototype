package com.helper.route.service;

import com.helper.route.entity.AccountEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/11 - 上午10:33
 * Created by IntelliJ IDEA.
 */
public interface AccountService {
    AccountEntity findById(Long id);

    Page<AccountEntity> findAll(Example<AccountEntity> example, Pageable pageable);

    AccountEntity findByAccount(String account);

    AccountEntity findByOne(Example<AccountEntity> example);
}
