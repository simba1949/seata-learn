package vip.openpark.xa.order.domain;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表名：t_order
 * 表注释：订单表
*/
@Getter
@Setter
@ToString
@Table(name = "t_order")
public class OrderDO {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 商品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 数量
     */
    @Column(name = "count")
    private Integer count;

    /**
     * 金额
     */
    @Column(name = "money")
    private BigDecimal money;

    /**
     * 订单状态，0表示创建中，1表示已完结
     */
    @Column(name = "status")
    private Byte status;
}