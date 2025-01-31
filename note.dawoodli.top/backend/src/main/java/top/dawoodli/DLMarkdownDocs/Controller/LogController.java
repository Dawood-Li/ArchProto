// package top.dawoodli.DLMarkdownDocs.Controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import top.dawoodli.DLMarkdownDocs.Service.LogService;

// import java.util.Collections;
// import java.util.Map;
// import java.util.List;

// @RestController
// @RequestMapping("/api/Logs")
// public class LogController {

//     @Autowired
//     private LogService logService;

//     @GetMapping
//     public Map<String, Object> getLogs(
//         @RequestParam(required = false) String level,
//         @RequestParam(required = false) String keyword) {
        
//         List<Map<String, Object>> logs = logService.fetchLogs(level, keyword);
//         return Map.of("data", logs);
//     }

//     @PostMapping
//     public Map<String, Object> writeLog(
//         @RequestParam String level,
//         @RequestParam String src,
//         @RequestParam String msg) {
        
//         Map<String, Object> log = logService.writeLog(level, src, msg);
//         return Map.of("data", log);
//     }
// }
