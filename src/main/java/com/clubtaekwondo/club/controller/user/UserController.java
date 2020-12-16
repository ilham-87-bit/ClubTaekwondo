package com.clubtaekwondo.club.controller.user;

import com.clubtaekwondo.club.model.School;
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

import java.nio.file.Path;
import java.util.List;



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

    @GetMapping(value = "/school")
    public String getAllSchool(Model model) {
        List<School> allSchool = schoolService.getAllSchool();
        allSchool.stream().forEach(school -> {
            String newImageName = String.format("schools/%s.jpeg", school.getId());
            school.setFullUrlImg(fileToPath(storageService.load(newImageName)));
        });
        model.addAttribute("schoolList", allSchool);
        return "user/school";
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
