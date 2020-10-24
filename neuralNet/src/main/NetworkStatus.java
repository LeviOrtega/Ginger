package main;

public enum NetworkStatus {
    RUN("Run"), LEARN("Learn"), END("End");

    String value;

    NetworkStatus(String aValue) {
        this.value = aValue;
    }

    public String getValue(){
        return this.value;
    }
}
