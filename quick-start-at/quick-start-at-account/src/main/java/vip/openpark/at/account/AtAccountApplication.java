package vip.openpark.at.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author anthony
 * @version 2024/10/13
 * @since 2024/10/13 16:31
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "vip.openpark.api")
@MapperScan(basePackages = "vip.openpark.at.account.mapper")
public class AtAccountApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtAccountApplication.class, args);
	}
}