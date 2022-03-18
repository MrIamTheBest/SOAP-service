package s23456.sri.soap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import s23456.sri.soap.model.Car;
import s23456.sri.soap.repo.CarRepository;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final CarRepository carRepository;
    public void initData() {
        Car p1 = Car.builder()
                .brand("Ford")
                .color("red")
                .year(Year.of(1990))
                .build();
        Car p2 = Car.builder()
                .brand("Renault")
                .color("black")
                .year(Year.of(1991))
                .build();
        Car p3 = Car.builder()
                .brand("BMW")
                .color("blue")
                .year(Year.of(2000))
                .build();
        List<Car> data = Arrays.asList(p1, p2, p3);
        carRepository.saveAll(data);
        log.info("Data initializes with: " + data);
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }
}
