package vip.openpark.tcc.account.domain;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表名：t_account
 * 表注释：账户表
*/
@Getter
@Setter
@ToString
@Table(name = "t_account")
public class AccountDO {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 总金额
     */
    @Column(name = "total")
    private BigDecimal total;

    /**
     * 已用金额
     */
    @Column(name = "used")
    private BigDecimal used;

    /**
     * 剩余金额
     */
    @Column(name = "residue")
    private BigDecimal residue;
}