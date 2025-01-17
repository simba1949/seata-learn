package vip.openpark.at.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 16:34
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "vip.openpark.api")
@MapperScan(basePackages = "vip.openpark.at.order.mapper")
public class AtOrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtOrderApplication.class, args);
	}
}