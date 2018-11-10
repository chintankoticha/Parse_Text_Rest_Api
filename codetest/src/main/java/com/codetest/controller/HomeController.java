package com.codetest.controller;

import com.codetest.Bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@EnableAutoConfiguration
@ComponentScan
public class HomeController  {

    @Autowired
    public Person person;

    List<Person> personList = new ArrayList<>();

    @RequestMapping(value = "/")
    public String home() {
        return "Home";
    }

    @RequestMapping(value = "/result")
    public String result() {
        return "result";
    }

    @RequestMapping(value = "/parse",method = RequestMethod.GET)
    public String parse(Model model){
        System.out.println("PARSE GET CALLED!");
        model.addAttribute("fileError","");
        return "Home";
    }

    @RequestMapping(value = "/parse",method = RequestMethod.POST,consumes = "multipart/form-data")
    public String parse(Model model, HttpServletRequest request, @RequestParam("file") MultipartFile file){
        System.out.println("Hello Parser!");
        model.addAttribute("fileError","");
        System.out.println("SELECTED: "+request.getParameter("fileType"));
        personList = new ArrayList<>();
        BufferedReader br;
        try {
            String line;
            boolean flag = false;
            List<String> result = new ArrayList<>();
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result.add(line);
                if(!flag && (line.split(", ").length>1 || line.split(" \\| ").length>1)){
                    flag = true;
                }
            }

            if(!flag && !request.getParameter("fileType").equalsIgnoreCase("1")){
                model.addAttribute("fileError","error");
                return "Home";
            }

            if(!flag){
                for(String s:result) {
                    Person p = new Person();
                    String[] spaceSeparated = s.split(" ");
                    p.setFirstName(spaceSeparated[1]);
                    p.setLastName(spaceSeparated[0]);
                    p.setFavoriteColor(spaceSeparated[5]);
                    p.setGender(spaceSeparated[3]);
                    try {
                        p.setDateOfBirth(new SimpleDateFormat("MM-dd-yyyy").parse(spaceSeparated[4]));
                    }catch(Exception e){
                        System.out.println("Date Parse Exception: "+e.getMessage());
                    }
                    personList.add(p);
                }
                personList = sortOutput1List(personList);
                String path1 = writeFile1(personList);
                String opath1 = "file:///"+path1;
                personList = sortOutput2List(personList);
                String path2 = writeFile2(personList);
                String opath2 = "file:///"+path2;
                personList = sortOutput3List(personList);
                String path3 = writeFile3(personList);
                String opath3 = "file:///"+path3;
                model.addAttribute("filePath1",opath1);
                model.addAttribute("filePath2",opath2);
                model.addAttribute("filePath3",opath3);
                return "result";
            }else{
                if(result.get(1).split(" \\| ").length>1) {
                    if(!request.getParameter("fileType").equalsIgnoreCase("3")){
                        model.addAttribute("fileError","error");
                        return "Home";
                    }
                    for (String s : result) {
                        Person p = new Person();
                        String[] spaceSeparated = s.split(" \\| ");
                        p.setFirstName(spaceSeparated[1]);
                        p.setLastName(spaceSeparated[0]);
                        p.setFavoriteColor(spaceSeparated[4]);
                        p.setGender(spaceSeparated[3]);
                        try {
                            p.setDateOfBirth(new SimpleDateFormat("MM-dd-yyyy").parse(spaceSeparated[5]));
                        }catch(Exception e){
                            System.out.println("Date Parse Exception: "+e.getMessage());
                        }
                        personList.add(p);
                    }
                    personList = sortOutput1List(personList);
                    String path1 = writeFile1(personList);
                    String opath1 = "file:///"+path1;
                    personList = sortOutput2List(personList);
                    String path2 = writeFile2(personList);
                    String opath2 = "file:///"+path2;
                    personList = sortOutput3List(personList);
                    String path3 = writeFile3(personList);
                    String opath3 = "file:///"+path3;
                    model.addAttribute("filePath1",opath1);
                    model.addAttribute("filePath2",opath2);
                    model.addAttribute("filePath3",opath3);
                }else{
                    if(!request.getParameter("fileType").equalsIgnoreCase("2")){
                        model.addAttribute("fileError","error");
                        return "Home";
                    }
                    for(String s:result) {
                        Person p = new Person();
                        String[] spaceSeparated = s.split(", ");
                        p.setFirstName(spaceSeparated[1]);
                        p.setLastName(spaceSeparated[0]);
                        p.setFavoriteColor(spaceSeparated[3]);
                        p.setGender(spaceSeparated[2]);
                        try {
                            p.setDateOfBirth(new SimpleDateFormat("MM/dd/yyyy").parse(spaceSeparated[4]));
                        }catch(Exception e){
                            System.out.println("Date Parse Exception: "+e.getMessage());
                        }
                        personList.add(p);
                    }
                    personList = sortOutput1List(personList);
                    String path1 = writeFile1(personList);
                    String opath1 = "file:///"+path1;
                    personList = sortOutput2List(personList);
                    String path2 = writeFile2(personList);
                    String opath2 = "file:///"+path2;
                    personList = sortOutput3List(personList);
                    String path3 = writeFile3(personList);
                    String opath3 = "file:///"+path3;
                    model.addAttribute("filePath1",opath1);
                    model.addAttribute("filePath2",opath2);
                    model.addAttribute("filePath3",opath3);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "result";
    }

    public List<Person> sortOutput1List(List<Person> personList1){
        personList1.sort(Comparator.comparing(Person::getGender).thenComparing(Person::getLastName));
        return personList1;
    }

    public List<Person> sortOutput2List(List<Person> personList1){
        personList1.sort(Comparator.comparing(Person::getDateOfBirth).thenComparing(Person::getLastName));
        return personList1;
    }

    public List<Person> sortOutput3List(List<Person> personList1){
        personList1.sort(Comparator.comparing(Person::getLastName).reversed());
        return personList1;
    }

    public String writeFile1(List<Person> personList1){
        //System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\parseFile1.txt";
        path = writeFile(personList1,path);
        return path;
    }

    public String writeFile2(List<Person> personList1){
        //System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\parseFile2.txt";
        path = writeFile(personList1,path);
        return path;
    }

    public String writeFile(List<Person> personList1, String path){
        try {
            Files.deleteIfExists(Paths.get(path));
        }catch(Exception e){
            System.out.println("Error while deleting the file:"+e.getMessage());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))){
            for(Person p:personList1) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getLastName() + " ");
                sb.append(p.getFirstName() + " ");
                if(p.getGender().equalsIgnoreCase("F")) {
                    sb.append("FEMALE ");
                }else{
                    sb.append("MALE ");
                }
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                sb.append(formatter.format(p.getDateOfBirth())+ " ");
                sb.append(p.getFavoriteColor());
                sb.append("\n");
                writer.write(sb.toString());
            }
            return path;
        }catch(Exception e){
            System.out.println("Write to File Error: "+e.getMessage());
        }
        return path;
    }

    public String writeFile3(List<Person> personList1){
        //System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\parseFile3.txt";
        path = writeFile(personList1,path);
        return path;
    }
}