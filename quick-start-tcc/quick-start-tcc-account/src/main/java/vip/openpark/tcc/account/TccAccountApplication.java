package vip.openpark.tcc.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author anthony
 * @version 2025/1/6
 * @since 2025/1/6 21:06
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "vip.openpark.api")
@MapperScan(basePackages = "vip.openpark.tcc.account.mapper")
public class TccAccountApplication {
	public static void main(String[] args) {
		SpringApplication.run(TccAccountApplication.class, args);
	}
}