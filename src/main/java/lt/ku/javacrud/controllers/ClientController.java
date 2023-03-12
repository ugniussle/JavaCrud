package lt.ku.javacrud.controllers;

import lt.ku.javacrud.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lt.ku.javacrud.repositories.ClientRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    public ClientRepository clientRepository;

    private final List<String> fields = getClassFields(Client.class);

    private final List<String> translatedFields = getTranslatedFields(fields);

    @GetMapping("/")
    public String clients(Model model) {
        List<Client> clients = clientRepository.getClients();

        model.addAttribute("title", "Klientų sąrašas");
        model.addAttribute("fields", fields);
        model.addAttribute("translatedFields", translatedFields);
        model.addAttribute("clients", clients);

        return "clientList";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("title", "Kuriamas naujas klientas");

        List<String> fieldsNoId = fields.subList(1, fields.size());
        List<String> translatedFieldsNoId = translatedFields.subList(1, translatedFields.size());

        model.addAttribute("fields", fieldsNoId);
        model.addAttribute("translations", translatedFieldsNoId);

        return "clientCreate";
    }

    @PostMapping("/create")
    public String store(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone
    ) {
        Client client = new Client(name, surname, email, phone);

        clientRepository.save(client);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
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

        return "clientEdit";
    }

    @PostMapping("/edit/{id}")
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

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ) {
        clientRepository.delete(
                clientRepository.getReferenceById(id)
        );

        return "redirect:/";
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
