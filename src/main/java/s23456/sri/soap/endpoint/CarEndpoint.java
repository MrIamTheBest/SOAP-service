package s23456.sri.soap.endpoint;


import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import s23456.sri.soap.cars.*;
import s23456.sri.soap.config.SoapWSConfig;
import s23456.sri.soap.model.Car;
import s23456.sri.soap.repo.CarRepository;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Endpoint
@RequiredArgsConstructor
public class CarEndpoint {
    private final CarRepository carRepository;
    @PayloadRoot(namespace = SoapWSConfig.CAR_NAMESPACE, localPart = "getCarsRequest")
    @ResponsePayload
    public GetCarsResponse getCars(@RequestPayload GetCarsRequest req) {
        List<Car> carList = carRepository.findAll();
        List<CarDto> dtoList = carList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetCarsResponse res = new GetCarsResponse();
        res.getCars().addAll(dtoList);
        return res;
    }
    @PayloadRoot(namespace = SoapWSConfig.CAR_NAMESPACE, localPart = "getCarsByIdRequest")
    @ResponsePayload
    public GetCarByIdResponse getCarById(@RequestPayload GetCarByIdRequest req) {
        Long id = req.getCarId().longValue();
        Optional<Car> car = carRepository.findById(id);
        GetCarByIdResponse res = new GetCarByIdResponse();
        res.setCar(convertToDto(car.orElse(null)));
        return res;
    }
    @PayloadRoot(namespace = SoapWSConfig.CAR_NAMESPACE, localPart = "addCarRequest")
    @ResponsePayload
    public AddCarResponse addCar(@RequestPayload AddCarRequest req) {
        CarDto dto = req.getCar();
        Car entity = convertToEntity(dto);
        carRepository.save(entity);
        AddCarResponse response = new AddCarResponse();
        response.setCarId(new BigDecimal(entity.getId()));
        return response;
    }
    @PayloadRoot(namespace = SoapWSConfig.CAR_NAMESPACE, localPart = "getCarNumberByBrandRequest")
    @ResponsePayload
    public GetCarNumberByBrandResponse getCarNumberByBrand(@RequestPayload GetCarNumberByBrandRequest req) {
        List<Car> carList = carRepository.findAll();
        Long carNumber = carList.stream()
                .filter(car -> car.getBrand().equals(req.getCarBrand())).count();
        GetCarNumberByBrandResponse res = new GetCarNumberByBrandResponse();
        res.setCarNumber(carNumber);
        return res;
    }
    @PayloadRoot(namespace = SoapWSConfig.CAR_NAMESPACE, localPart = "getCarsByColorRequest")
    @ResponsePayload
    public GetCarsByColorResponse getCarsByColor(@RequestPayload GetCarsByColorRequest req) {
        List<Car> carList = carRepository.findAll();
        List<CarDto> cars = carList.stream()
                .filter(car -> car.getColor().equals(req.getCarColor()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
        GetCarsByColorResponse res = new GetCarsByColorResponse();
        res.getCars().addAll(cars);
        return res;
    }
    private Car convertToEntity(CarDto dto) {
        return Car.builder()
                .id(dto.getId() != null ? dto.getId().longValue() : null)
                .brand(dto.getBrand())
                .color(dto.getColor())
                //todo date
                .build();
    }

    private CarDto convertToDto(Car entity) {
        if (entity == null) {
            return null;
        }
        try {
            CarDto dto = new CarDto();
            dto.setId(new BigDecimal(entity.getId()));
            dto.setBrand(entity.getBrand());
            dto.setColor(entity.getColor());
            XMLGregorianCalendar year = null;
            year = DatatypeFactory.newInstance().newXMLGregorianCalendar(entity.getYear().toString());
            dto.setYear(year);
            return dto;
        } catch (DatatypeConfigurationException datatypeConfigurationException) {
            datatypeConfigurationException.printStackTrace();
            return null;
        }
    }
}