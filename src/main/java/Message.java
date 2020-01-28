import lombok.Data;

@Data
public class Message {
    private int senderId;
    private int receiverId;

    private int length;
    private String text;

    public Message() {
    }


    public Message(int senderId, int receiverId, int length, String text) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.length = length;
        this.text = text;
    }
}
