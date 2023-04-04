package lt.ku.javacrud.controllers;

import lt.ku.javacrud.entities.Workout;
import lt.ku.javacrud.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkoutController {
    @Autowired
    public WorkoutRepository workoutRepository;

    private final List<String> fields = getClassFields(Workout.class);

    private final List<String> translatedFields = getTranslatedFields(fields);

    @GetMapping("/workouts/")
    public String workouts(Model model) {
        List<Workout> workouts = workoutRepository.getWorkouts();

        model.addAttribute("title", "Treniruočių sąrašas");
        model.addAttribute("fields", fields);
        model.addAttribute("translatedFields", translatedFields);
        model.addAttribute("workouts", workouts);

        return "workouts/list";
    }

    @GetMapping("/workouts/create")
    public String create(Model model) {
        model.addAttribute("title", "Kuriama nauja treniruotė");

        model.addAttribute("workout", new Workout());

        return "workouts/create";
    }

    @PostMapping("/workouts/create")
    public String store(
            @ModelAttribute Workout workout
    ) {
        workoutRepository.save(workout);

        return "redirect:/workouts/";
    }

    @GetMapping("/workouts/edit/{id}")
    public String edit(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Workout workout = workoutRepository.getReferenceById(id);

        List<String> fieldsNoId = fields.subList(1, fields.size());
        List<String> translatedFieldsNoId = translatedFields.subList(1, translatedFields.size());

        model.addAttribute(
                "title",
                "Keičiama treniruotė: " + workout.getName() + ", " +
                workout.getDate() + ", " + workout.getLocation()
        );
        model.addAttribute("workout", workout);
        model.addAttribute("fields", fieldsNoId);
        model.addAttribute("translations", translatedFieldsNoId);

        return "workouts/edit";
    }

    @PostMapping("/workouts/edit/{id}")
    public String edit(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("date") String date,
            @RequestParam("max_size") String max_size,
            @RequestParam("location") String location
    ) {
        Workout workout = workoutRepository.getReferenceById(id);

        workout.setName(name);
        workout.setDate(date);
        workout.setMax_size(max_size);
        workout.setLocation(location);

        workoutRepository.save(workout);

        return "redirect:/workouts/";
    }

    @GetMapping("/workouts/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ) {
        workoutRepository.delete(
                workoutRepository.getReferenceById(id)
        );

        return "redirect:/workouts/";
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
            case "name" -> "Pavadinimas";
            case "date" -> "Data";
            case "max_size" -> "Vietų sk.";
            case "location" -> "Vieta";
            default -> "";
        };
    }

    private List<String> getClassFields(Class<Workout> entity) {
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
