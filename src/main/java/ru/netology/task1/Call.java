package ru.netology.task1;

public class Call {
    private final String phoneNumber;
    private final String name;

    public Call(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }
}
