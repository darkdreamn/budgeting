package diolab.budgeting.service;

import diolab.budgeting.model.Transaction;
import diolab.budgeting.repository.TransactionRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BudgetingService {
    private final TransactionRepository repository;
    private final ChatClient chatClient;

    public BudgetingService(TransactionRepository repository, ChatClient.Builder chatClientBuider) {
        this.repository = repository;
        this.chatClient = chatClientBuider.build();
    }

    @Tool(description = "Registrar uma nova transação financeira, seja uma despesa ou uma receita.")
    public String registerTransaction(String description, double value, String type) {
        Transaction transaction = new Transaction(description, BigDecimal.valueOf(value), type.toUpperCase());
        repository.save(transaction);
        return "Sucesso: Transação '" + description + "'de R$" + value + " gravada como " + type + " no banco de dados.";
    }

    @Tool(description = "Lista todas as transações financeiras gravadas até o momento.")
    public String processCommand(String textCommand) {
        return this.chatClient.prompt()
                .user(textCommand)
                .tools(this)
                .call()
                .content();
    }
}
