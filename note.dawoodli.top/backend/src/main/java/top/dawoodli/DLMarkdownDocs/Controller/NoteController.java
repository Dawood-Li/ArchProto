package top.dawoodli.DLMarkdownDocs.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.dawoodli.DLMarkdownDocs.Request.NoteCreateRequest;
import top.dawoodli.DLMarkdownDocs.Request.NoteUpdateRequest;
import top.dawoodli.DLMarkdownDocs.Service.NoteService2;

import java.util.*;

@RestController
@RequestMapping("/api/Notes")
@CrossOrigin
public class NoteController {

    @Autowired
    private NoteService2 noteService;

    // 所有人公开笔记列表
    @PostMapping("/GetPublicList")
    public Map<String, Object> GetList() {
        return Map.of("data", noteService.GetPublicList());
    }

    // 用户笔记列表
    @PostMapping("/GetUserList/{Type}")
    public Map<String, Object> GetUserList(
        @RequestAttribute Map<String, Object> userInfo,
        @PathVariable String Type
    ) {
        Long userID = (Long)userInfo.get("ID");
        return Map.of("data", noteService.GetUserList(userID, Type));
    }

    // 笔记详情
    @PostMapping("/GetDetail/{DataID}")
    public Map<String, Object> GetDetail(@PathVariable Long DataID) {
        Map<String, Object> doc = noteService.GetNoteNewestContent(DataID);
        return doc != null
            ? Map.of("data", doc)
            : Map.of("err", "not found");
    }

    // 创建
    @PostMapping("/Create")
    public Map<String, Object> Create(
        @RequestAttribute Map<String, Object> userInfo,
        @RequestBody NoteCreateRequest req
    ) {
        Long userID = (Long)userInfo.get("ID");
        Long ID = noteService.create(
            userID,
            req.IsPublic,
            req.Title,
            req.Content
        );
        return Map.of("ID", ID);
    }

    // 更新
    @PostMapping("/Update/{NoteID}")
    public Map<String, String> Update(
        @PathVariable Long NoteID, @RequestBody NoteUpdateRequest req) {
        noteService.update(
            NoteID,
            req.Title,
            req.Content
        );
        return Map.of("msg", "ok");
    }

    // 删除
    @PostMapping("/Delete/{NoteID}")
    public Map<String, String> delete(@PathVariable Long NoteID) {
        noteService.delete(NoteID);
        return Map.of("msg", "ok");
    }

    // 恢复
    @PostMapping("/Restore/{NoteID}")
    public Map<String, String> restore(@PathVariable Long NoteID) {
        noteService.restore(NoteID);
        return Map.of("msg", "ok");
    }

    // 公开
    @PostMapping("/Show/{NoteID}")
    public Map<String, String> show(@PathVariable Long NoteID) {
        noteService.show(NoteID);
        return Map.of("msg", "ok");
    }

    // 隐藏
    @PostMapping("/Hide/{NoteID}")
    public Map<String, String> hide(@PathVariable Long NoteID) {
        noteService.hide(NoteID);
        return Map.of("msg", "ok");
    }

    // 搜索
    // @PostMapping("/Search/{keywords}")
    // public Map<String, Object> Search(@PathVariable String keywords) {
    //     return Map.of("data", noteService.search(keywords));
    // }
}

