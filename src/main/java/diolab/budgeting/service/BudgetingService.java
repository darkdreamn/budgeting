package diolab.budgeting.service;

import diolab.budgeting.model.Transaction;
import diolab.budgeting.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class BudgetingService {
    private final ChatModel chatModel;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BudgetingService(ChatModel chatModel, TransactionRepository transactionRepository) {
        this.chatModel = chatModel;
        this.transactionRepository = transactionRepository;
    }

    public String processCommand(String textCommand) {
        String contextualizedPrompt =
                "Você é um assistente de finanças focado em ajudar o usuário. " +
                        "O usuário enviou o seguinte comando: " + textCommand +
                        ". Responda de forma curta, objetiva e amigável.";

        String extractionPrompt = "Extraia o valor numérico e a categoria do seguinte texto. " +
                "Responda APENAS com o valor e a categoria separados por vírgula. " +
                "Exemplo: 50.00,Alimentação. Texto: " + textCommand;

        String extractedData = chatModel.call(extractionPrompt);
        String[] valuesExtractedData = extractedData.split(",");

        if (valuesExtractedData.length >= 2) {
            String valueText = valuesExtractedData[0].trim();
            String typeText = valuesExtractedData[1].trim();

            java.math.BigDecimal convertedValue = new java.math.BigDecimal(valueText);

            Transaction newTransaction = new Transaction(textCommand, convertedValue, typeText);

            transactionRepository.save(newTransaction);
        }

        return chatModel.call(contextualizedPrompt);
    }
}
