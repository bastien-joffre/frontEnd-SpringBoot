package com.frontend.controllers;

import com.frontend.forms.CarForm;
import com.frontend.models.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @Value("${api.url}")
    private String api;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("message", message);

        return "index";
    }

    @RequestMapping(value = { "/carList" }, method = RequestMethod.GET)
    public String carList(Model model) {

        Car[] cars = restTemplate.getForObject(api + "/cars", Car[].class);

        model.addAttribute("cars", cars);

        return "carList";
    }

    @RequestMapping(value = { "/addCar" }, method = RequestMethod.GET)
    public String showAddCarPage(Model model) {

        CarForm carForm = new CarForm();
        model.addAttribute("carForm", carForm);

        return "addCar";
    }

    @RequestMapping(value = { "/addCar" }, method = RequestMethod.POST)
    public String saveCar(Model model, //
                          @ModelAttribute("carForm") CarForm carForm) {

        String brand = carForm.getBrand();
        String type = carForm.getType();
        int id = carForm.getId();

        if (brand != null && brand.length() > 0 &&
            type != null && type.length() > 0 &&
            id > 0) {

            restTemplate.postForObject(api + "/create", new Car(brand, type, id), Car.class);

            return "redirect:/carList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCar";
    }
}