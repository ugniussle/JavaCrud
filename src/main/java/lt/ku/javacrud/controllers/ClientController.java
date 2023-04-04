package lt.ku.javacrud.controllers;

import jakarta.validation.Valid;
import lt.ku.javacrud.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lt.ku.javacrud.repositories.ClientRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    public ClientRepository clientRepository;

    private final List<String> fields = getClassFields(Client.class);

    private final List<String> translatedFields = getTranslatedFields(fields);

    @GetMapping("/clients/")
    public String clients(Model model) {
        List<Client> clients = clientRepository.getClients();

        model.addAttribute("title", "Klientų sąrašas");
        model.addAttribute("fields", fields);
        model.addAttribute("translatedFields", translatedFields);
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
            @Valid
            @ModelAttribute Client client,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Sukurkite naują klientą");
            return "/clients/create";
        }

        clientRepository.save(client);

        return "redirect:/clients/";
    }

    @GetMapping("/clients/edit/{id}")
    public String edit(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Client client = clientRepository.getReferenceById(id);

        List<String> fieldsNoId = fields.subList(1, fields.size());
        List<String> translatedFieldsNoId = translatedFields.subList(1, translatedFields.size());

        model.addAttribute("title", "Keičiamas klientas: " + client.getName() + " " + client.getSurname());
        model.addAttribute("client", client);
        model.addAttribute("fields", fieldsNoId);
        model.addAttribute("translations", translatedFieldsNoId);

        return "clients/edit";
    }

    @PostMapping("/clients/edit/{id}")
    public String edit(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone
    ) {
        Client client = clientRepository.getReferenceById(id);

        client.setName(name);
        client.setSurname(surname);
        client.setEmail(email);
        client.setPhone(phone);

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

    private List<String> getTranslatedFields(List<String> fields) {
        List<String> translatedFields = new ArrayList<>();

        for (String field : fields) {
            translatedFields.add(translate(field));
        }

        return translatedFields;
    }

    private String translate(String text) {
        return switch (text.toLowerCase()) {
            case "id" -> "Id";
            case "name" -> "Vardas";
            case "surname" -> "Pavardė";
            case "email" -> "El. paštas";
            case "phone" -> "Telefono nr.";
            default -> "";
        };
    }

    private List<String> getClassFields(Class<Client> entity) {
        Field[] declaredFields = entity.getDeclaredFields();
        ArrayList<String> fields = new ArrayList<>();

        for (Field declaredField : declaredFields) {
            String str = declaredField.toString();

            // get just the name of the field
            int fieldStart = str.lastIndexOf(".") + 1;
            String field = str.substring(fieldStart);

            fields.add(field);
        }

        return fields;
    }
}
