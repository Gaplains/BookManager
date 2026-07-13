package com.wangpeng.bms.web;

import com.wangpeng.bms.model.BookInfo;
import com.wangpeng.bms.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/ai")
public class AiAssistantController {

    @Autowired
    private BookInfoService bookInfoService;

    @GetMapping(value = "/ask")
    public Map<String, Object> ask(@RequestParam(value = "question", required = false) String question) {
        String normalizedQuestion = question == null ? "" : question.trim();
        if (normalizedQuestion.length() == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 400);
            result.put("msg", "请输入想咨询的图书、类型或借阅规则问题");
            return result;
        }

        List<BookInfo> books = bookInfoService.queryBookInfos();
        List<BookInfo> recommendedBooks = recommendBooks(normalizedQuestion, books);
        Map<String, Object> data = new HashMap<>();
        data.put("answer", buildAnswer(normalizedQuestion, recommendedBooks));
        data.put("recommendations", recommendedBooks);
        data.put("process", "模拟 AI：关键词识别 → 匹配书名/作者/分类/简介 → 按可借状态与相关度推荐，并补充借阅规则说明。后续可替换为大模型 API。");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", data);
        return result;
    }

    private List<BookInfo> recommendBooks(String question, List<BookInfo> books) {
        String lowerQuestion = question.toLowerCase(Locale.ROOT);
        List<BookInfo> matched = new ArrayList<>();
        for (BookInfo book : books) {
            String text = (safe(book.getBookname()) + " " + safe(book.getBookauthor()) + " " + safe(book.getBooktypename()) + " " + safe(book.getBookdesc())).toLowerCase(Locale.ROOT);
            if (containsAny(lowerQuestion, text)) {
                matched.add(book);
            }
            if (matched.size() >= 5) {
                break;
            }
        }
        if (matched.isEmpty()) {
            for (BookInfo book : books) {
                if (book.getIsborrowed() == null || book.getIsborrowed() == 0) {
                    matched.add(book);
                }
                if (matched.size() >= 5) {
                    break;
                }
            }
        }
        return matched;
    }

    private boolean containsAny(String question, String bookText) {
        String[] keywords = question.split("[\\s,，。！？?、]+");
        for (String keyword : keywords) {
            if (keyword.length() >= 2 && bookText.contains(keyword.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return bookText.contains(question.toLowerCase(Locale.ROOT));
    }

    private String buildAnswer(String question, List<BookInfo> books) {
        if (question.contains("借") || question.contains("还") || question.contains("逾期") || question.contains("规则")) {
            return "借阅规则：读者登录后可在“图书信息管理”中搜索并借阅未借出的图书；管理员可代用户借阅、维护图书与分类；归还请在“借阅信息管理”中操作。建议先确认图书状态为“未借出”。";
        }
        if (books.isEmpty()) {
            return "暂未匹配到图书。可尝试输入更明确的书名、作者、分类，例如“人工智能入门”“历史”“Java”。";
        }
        StringBuilder answer = new StringBuilder("根据您的问题，推荐优先查看：");
        for (int i = 0; i < books.size(); i++) {
            if (i > 0) {
                answer.append("、");
            }
            answer.append("《").append(books.get(i).getBookname()).append("》");
        }
        answer.append("。推荐依据为书名、作者、分类与简介关键词匹配，并优先展示馆藏中的图书。");
        return answer.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
