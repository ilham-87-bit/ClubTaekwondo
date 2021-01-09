package com.clubtaekwondo.club.controller.user;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.management.ListenerNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private StorageService storageService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private TariffService tariffService;
    @Autowired
    private SubscriptionPeriodService subscriptionPeriodService;
    @Autowired
    private SubscriptionTypeService subscriptionTypeService;

    @GetMapping(value = "/school")
    public String getAllSchool(Model model) {
        List<School> allSchool = schoolService.getAllSchool();
        allSchool.stream().forEach(school -> {
            String newImageName = String.format("schools/%s.jpeg", school.getIdSchool());
            school.setFullUrlImg(fileToPath(storageService.load(newImageName)));
        });
        model.addAttribute("schoolList", allSchool);
        model.addAttribute("subscriptions", subscriptionService.getCart());
        return "user/school";
    }

    @GetMapping(value = "/tariff")
    public String getAllTariff(Model model) {
        Map<Long, List> categoryByPeriod = new HashMap<>();

        List<Categories> categoriesList = categoriesService.getAllCategory();
        List<SubscriptionPeriod> subscriptionPeriodList = subscriptionPeriodService.getAllPeriod();
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeService.getAllSubscriptionType();
        List<Tariff> tariffs = tariffService.getAllTariff();

        model.addAttribute("tariffList", tariffs);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());

        return "user/tariff";
    }

    @GetMapping("/{filename:imgs.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadImageFromServer(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename.replaceAll(">", "\\\\"));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename()).body(file);
    }

    public static String fileToPath(Path path) {
        return MvcUriComponentsBuilder.fromMethodName(UserController.class,
                "loadImageFromServer", path.toString().replaceAll("\\\\", ">")).build().toString();
    }
}
