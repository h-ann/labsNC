package journal;

public enum Importance {
    one    (1,"."),
    two    (2,"!"),
    three  (3,"!!!"),
    four   (4,"!!!!!");
    private final int value;
    private final String view;

    Importance(int value, String view) {
        this.value = value;
        this.view = view;
    }

    public String getString(){
        return view;
    }
    public int getValue(){
        return value;
    }
}
