public class Shramba {
    // Singleton vzorec
    private static Shramba instance;
    public int uporabnikId;

    private Shramba() {

    }

    // Singleton metoda za dostop do instance
    public static synchronized Shramba getInstance() {
        if (instance == null) {
            instance = new Shramba();
        }
        return instance;
    }

    public boolean isAdministrator() {
        return this.uporabnikId == 1;
    }
}
