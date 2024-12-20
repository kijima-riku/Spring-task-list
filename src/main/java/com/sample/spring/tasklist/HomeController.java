package com.sample.spring.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    private final TaskListDao dao;

    @Autowired
    HomeController(TaskListDao dao) {
        this.dao = dao;
    }

    record TaskItem(String id, String task, String deadline, boolean done) {
    }


    @RequestMapping(value = "list")
    String listItems(Model model) {
        List<TaskItem> taskItems = dao.findAll();
        //渡す値をここで指定
        model.addAttribute("taskList", taskItems);
        return "Home";
    }

    @RequestMapping(value = "add")
    String addItem(@RequestParam("task") String task, @RequestParam("deadline") String deadline) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        TaskItem item = new TaskItem(id, task, deadline, false);
        dao.add(item);

        return "redirect:/list";
    }

    @RequestMapping(value = "/delete")
    String deleteItem(@RequestParam("id") String id) {
        dao.delete(id);
        return "redirect:/list";
    }

    @RequestMapping(value = "/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done) {
        TaskItem taskItem = new TaskItem(id, task, deadline, done);
        dao.update(taskItem);
        return "redirect:/list";
    }

    @RequestMapping(value = "/toggleDone")
    String toggleDone(@RequestParam("id") String id) {
        dao.updateDone(id);
        return "redirect:/list";
    }
}
