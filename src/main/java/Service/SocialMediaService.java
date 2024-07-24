package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class SocialMediaService {
    
    private final SocialMediaDAO dao;

    public SocialMediaService () {
        this.dao = new SocialMediaDAO();
    }

    public Account registerUser (Account account) {
        if(account.getUsername() == null || account.getPassword() == null ||
            account.getUsername().trim().isEmpty() || account.getPassword().length() < 4) {
            return null;
        }
        return dao.registerUser(account);
    }

    public Account loginUser (Account account) {
        if(account.getUsername() == null || account.getPassword() == null) {
            return null;
        }
        return dao.loginUser(account);
    }

    public Message createMessage (Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255 || message.getPosted_by() <= 0) {
                return null;
        }
        return dao.createMessage(message);
    }

    public List<Message> getAllMessages () {
        return dao.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return dao.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        return dao.deleteMessageById(messageId);
    }

    public Message updateMessageById(int messageId, String newMessageText) {
        if (newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        if(dao.getMessageById(messageId) == null) {
            return null;
        }
        return dao.updateMessageById(messageId, newMessageText);
    }

    public List<Message> getMessagesByUser (int accountId) {
        return dao.getMessagesByUser(accountId);
    }
}
