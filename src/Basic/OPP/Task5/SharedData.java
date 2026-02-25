package Basic.OPP.Task5;

public class SharedData extends BaseEntity {
    private String phoneNumber;

    public SharedData(int id, String name, String phoneNumber) {
        super(id, name);
        if (checkPhoneNumber(phoneNumber))
            this.phoneNumber = phoneNumber;
    }

    public SharedData(int id, String name) {
        this(id, name, "");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() < 12 || !phoneNumber.startsWith("+20") || !phoneNumber.substring(3).matches("\\d{9}"))
            throw new IllegalArgumentException("Invalid phone number, phone number should be in the format +20XXXXXXXXX.");
        return true;
    }
}
