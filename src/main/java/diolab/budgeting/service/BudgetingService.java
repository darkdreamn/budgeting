package diolab.budgeting.service;

import diolab.budgeting.model.Transaction;
import diolab.budgeting.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


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
        textCommand = textCommand.replace("\"", "").trim();

        String contextualizedPrompt =
                "Você é um assistente de finanças focado em ajudar o usuário. " +
                        "O usuário enviou o seguinte comando: " + textCommand +
                        ". Responda de forma curta, objetiva e amigável.";

        String extractionPrompt = "Extraia o valor numérico e a categoria do seguinte texto. " +
                "Responda APENAS com o valor e a categoria separados por vírgula. " +
                "Exemplo: 50.00,Alimentação. Texto: " + textCommand;

        String extractedData = chatModel.call(extractionPrompt);
        String finalAnswer = chatModel.call(contextualizedPrompt);

        try {
            String[] valuesExtractedData = extractedData.split(",");

            if (valuesExtractedData.length >= 2) {
                String valueText = valuesExtractedData[0].trim();
                String typeText = valuesExtractedData[1].trim();

                java.math.BigDecimal convertedValue = new java.math.BigDecimal(valueText);

                Transaction newTransaction = new Transaction(textCommand, convertedValue, typeText);

                transactionRepository.save(newTransaction);
            } else {
                System.out.println("Aviso: Formato de extração inválido da IA: " + extractedData);
                finalAnswer = finalAnswer + "(Nota: Não consegui registrar esse gasto automaticamente no seu banco de dados).";
            }
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("Erro ao salvar transação: Não foi possível converter o valor extraído pela IA. Resposta da IA: " + extractedData);
            finalAnswer = finalAnswer + "(Nota: Não consegui registrar esse gasto automaticamente no seu banco de dados).";
        }
        return finalAnswer;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
