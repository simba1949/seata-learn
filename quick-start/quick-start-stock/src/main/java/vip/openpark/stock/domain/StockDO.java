package vip.openpark.stock.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表名：t_stock
 * 表注释：库存表
*/
@Getter
@Setter
@ToString
@Table(name = "t_stock")
public class StockDO {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 库存总数
     */
    @Column(name = "total")
    private Integer total;

    /**
     * 已用库存
     */
    @Column(name = "used")
    private Integer used;

    /**
     * 剩余库存
     */
    @Column(name = "residue")
    private Integer residue;
}