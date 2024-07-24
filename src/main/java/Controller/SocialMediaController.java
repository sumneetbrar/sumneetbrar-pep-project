package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final SocialMediaService service;

    public SocialMediaController() {
        this.service = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);

        return app;
    }

    private void registerUser(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account createdAccount = service.registerUser(account);
        if(createdAccount != null) {
            context.status(200).json(createdAccount); // successful creation
        } else {
            context.status(400); // client error
        }
    }

    private void loginUser(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account loggedInAccount = service.loginUser(account);
        if(loggedInAccount != null) {
            context.json(loggedInAccount);
        } else {
            context.status(401);
        }
    }

    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = service.createMessage(message);
        if (createdMessage != null) {
            context.json(createdMessage);
        } else {
            context.status(400);
        }
    }

    private void getAllMessages(Context context) {
        List<Message> messages = service.getAllMessages();
        context.status(200).json(messages);
    }

    private void getMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = service.getMessageById(messageId);
        if (message != null) {
            context.status(200).json(message);
        } else {
            context.status(200);
        }
    }

    private void deleteMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = service.deleteMessageById(messageId);
        if (deletedMessage != null) {
            context.status(200).json(deletedMessage);
        } else {
            context.status(200);
        }        
    }

    private void updateMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Message request = objectMapper.readValue(context.body(), Message.class);
            String newMessageText = request.message_text;

            Message updatedMessage = service.updateMessageById(messageId, newMessageText);
            
            if (updatedMessage != null) {
                String jsonResponse = objectMapper.writeValueAsString(updatedMessage);
                context.status(200).json(jsonResponse);
            } else {
                context.status(400);
            }
        } catch (JsonProcessingException e) {
            context.status(400);
        }
    }

    private void getMessagesByUser(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = service.getMessagesByUser(accountId);
        context.status(200).json(messages);
    }
}