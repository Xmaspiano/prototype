package com.helper.route.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/11 - 上午9:44
 * Created by IntelliJ IDEA.
 */
@Entity
@Data
@Table(name = "account")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class AccountEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String account;
    String password;
//    //cascade:表的级联操作
//    @OneToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = UserEntity.class) //JPA注释： 一对一 关系
//    /**
//     * referencedColumnName：参考列名,默认的情况下是列表的主键
//     * nullable=是否可以为空，
//     * insertable：是否可以插入，
//     * updatable：是否可以更新
//     * columnDefinition=列定义，
//     * foreignKey=外键
//     */
//    @JoinColumn(name="accountid",referencedColumnName="id",nullable=false)
//    UserEntity userEntity;
}
