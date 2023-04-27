package lt.ku.javacrud.controllers;

import jakarta.validation.Valid;
import lt.ku.javacrud.entities.Client;
import lt.ku.javacrud.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lt.ku.javacrud.repositories.ClientRepository;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public FileStorageService fileStorageService;

    @GetMapping("/clients/")
    public String clients(Model model) {
        List<Client> clients = clientRepository.getClients();

        model.addAttribute("title", "Klientų sąrašas");
        model.addAttribute("clients", clients);

        return "clients/list";
    }

    @GetMapping("/clients/create")
    public String create(Model model) {
        model.addAttribute("title", "Kuriamas naujas klientas");

        model.addAttribute("client", new Client());

        return "clients/create";
    }

    @PostMapping("/clients/create")
    public String store(
            Model model,
            @RequestParam("file") MultipartFile document,
            @Valid
            @ModelAttribute Client client,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Sukurkite naują klientą");
            return "/clients/create";
        }

        if(!document.isEmpty()) {
            client.setDocument(document.getOriginalFilename());
        }

        clientRepository.save(client);

        if(!document.isEmpty()) {
            fileStorageService.store(document, client.getId().toString());
        }

        return "redirect:/clients/";
    }

    @GetMapping("/clients/{id}/file")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        Client client = clientRepository.getReferenceById(id);
        Resource file = fileStorageService.load(client.getId().toString());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ client.getDocument() +"\"")
                .body(file);
    }

    @GetMapping("/clients/edit/{id}")
    public String edit(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Client client = clientRepository.getReferenceById(id);

        model.addAttribute("title", "Keičiamas klientas: " + client.getName() + " " + client.getSurname());
        model.addAttribute("client", client);

        return "clients/edit";
    }

    @PostMapping("/clients/edit/{id}")
    public String edit(
            @RequestParam("file") MultipartFile document,
            @Valid
            @ModelAttribute Client client,
            BindingResult result,
            Model model
    ) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Keičiamas klientas: " + client.getName() + " " + client.getSurname());
            model.addAttribute("client", client);
            return "clients/edit";
        }

        if(!document.isEmpty()) {
            client.setDocument(document.getOriginalFilename());
            fileStorageService.store(document, client.getId().toString());
        }

        clientRepository.save(client);

        return "redirect:/clients/";
    }

    @GetMapping("/clients/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ) {
        clientRepository.delete(
                clientRepository.getReferenceById(id)
        );

        return "redirect:/clients/";
    }
}
