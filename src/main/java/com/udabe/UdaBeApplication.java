package com.udabe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@SpringBootApplication
@EnableJpaAuditing
public class UdaBeApplication {
	private static final Logger log = LoggerFactory.getLogger(UdaBeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UdaBeApplication.class, args);
	}

	@Component
	static class Initialize {

		private final ServletContext context;

		public Initialize(ServletContext context) {
			this.context = context;
		}

		@PostConstruct
		public void init() {
			System.setProperty("webRoot", context.getRealPath(""));
			// TODO - war 및 jar 배포환경에서 디렉토리 정보 확인 필요

			String str = "\n" +
					"\n=========================================================================================" +
					"\n:: Application Initialize Info ::" +
					"\n:: active profiles : " + System.getProperty("spring.profiles.active") +
					"\n:: web root directory : " + System.getProperty("webRoot") +
					"\n=========================================================================================" +
					"\n";
			log.info(str);
		}
	}

}
