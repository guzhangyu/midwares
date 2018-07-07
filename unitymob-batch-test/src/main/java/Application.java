import com.unitymob.TransactionTest;
import com.unitymob.mapper.AMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.unitymob"})
public class Application {



    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext=SpringApplication.run(Application.class,args);
//        AMapper aMapper=applicationContext.getBean(AMapper.class);
//        aMapper.insert("a");
//        aMapper.insert("a");
        TransactionTest transactionTest=applicationContext.getBean(TransactionTest.class);
        transactionTest.test();
    }
}
