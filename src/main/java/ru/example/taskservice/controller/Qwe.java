//package ru.example.taskservice.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.example.taskservice.dto.EmailMessageRequestDto;
//import ru.example.taskservice.publisher.NotificationPublisher;
//
//import java.util.Map;
//
//@RequestMapping("/qwe")
//@RestController
//@RequiredArgsConstructor
//public class Qwe {
//          private final NotificationPublisher notificationPublisher;
//
//          @GetMapping
//          public void qwe(){
//                    notificationPublisher.send(   new EmailMessageRequestDto("qwe","qwe", Map.of("zxc","asd")));
//          }
//}
