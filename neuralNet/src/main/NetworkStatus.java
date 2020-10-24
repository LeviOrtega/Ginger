package main;

public enum NetworkStatus {
    TEST("Test"), TRAIN("Train"), END("End");

    String value;

    NetworkStatus(String aValue) {
        this.value = aValue;
    }

    public String getValue(){
        return this.value;
    }
}
