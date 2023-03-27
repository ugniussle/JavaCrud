package lt.ku.javacrud.controllers;

import lt.ku.javacrud.entities.Client;
import lt.ku.javacrud.entities.Registration;
import lt.ku.javacrud.entities.Workout;
import lt.ku.javacrud.repositories.ClientRepository;
import lt.ku.javacrud.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lt.ku.javacrud.repositories.RegistrationRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class RegistrationController {
    @Autowired
    public RegistrationRepository registrationRepository;

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public WorkoutRepository workoutRepository;

    private final List<String> fields = getClassFields(Registration.class);

    private final List<String> translatedFields = getTranslatedFields(fields);

    @GetMapping("/registrations/")
    public String registrations(Model model) {
        List<Registration> registrations = registrationRepository.getRegistrations();

        System.out.println(registrations);

        model.addAttribute("title", "Registracijų sąrašas");
        model.addAttribute("fields", fields);
        model.addAttribute("translatedFields", translatedFields);
        model.addAttribute("registrations", registrations);

        return "registrations/list";
    }

    @GetMapping("/registrations/create/{id}")
    public String create(
            Model model,
            @PathVariable("id") Integer user_id
    ) {

        Client client = clientRepository.getReferenceById(user_id);
        model.addAttribute("client", client);

        model.addAttribute("title", "Registruojamas klientas: " + client.getName() + " " + client.getSurname());

        List<Workout> workouts = workoutRepository.getWorkouts();
        model.addAttribute("workouts", workouts);

        List<String> fields = Arrays.asList("name", "date", "max_size", "location");
        List<String> translatedFields = Arrays.asList("Pavadinimas", "Data", "Vietų sk.", "Vieta");
        model.addAttribute("fields", fields);
        model.addAttribute("translatedFields", translatedFields);

        return "registrations/create";
    }

    @GetMapping("registrations/create/{client_id}/{workout_id}")
    public String store(
            @PathVariable("client_id") Integer client_id,
            @PathVariable("workout_id") Integer workout_id
    ) {
        Client client = clientRepository.getReferenceById(client_id);
        Workout workout = workoutRepository.getReferenceById(workout_id);

        Date now = new Date();

        Registration registration = new Registration(client, workout, now.toString());

        registrationRepository.save(registration);

        return "redirect:/registrations/";
    }

    @GetMapping("registrations/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ) {
        registrationRepository.delete(
                registrationRepository.getReferenceById(id)
        );

        return "redirect:/registrations/";
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
            case "client" -> "Klientas";
            case "workout" -> "Treniruotė";
            case "registration_date" -> "Registracijos data";
            default -> "";
        };
    }

    private List<String> getClassFields(Class<Registration> entity) {
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
