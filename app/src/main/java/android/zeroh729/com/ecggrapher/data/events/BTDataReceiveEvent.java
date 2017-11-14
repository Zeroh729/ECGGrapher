package android.zeroh729.com.ecggrapher.data.events;

public class BTDataReceiveEvent {
    private byte[] data;
    private String message;

    public byte[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public BTDataReceiveEvent(byte[] data, String message) {

        this.data = data;
        this.message = message;
    }
}
