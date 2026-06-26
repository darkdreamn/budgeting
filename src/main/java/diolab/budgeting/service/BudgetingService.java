package diolab.budgeting.service;

import diolab.budgeting.model.Transaction;
import diolab.budgeting.repository.TransactionRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BudgetingService {
    private final ChatModel chatModel;

    @Autowired
    public BudgetingService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String processCommand(String textCommand) {
        return chatModel.call(textCommand);
    }
}
