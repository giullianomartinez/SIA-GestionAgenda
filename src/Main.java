public class Main {

    public static void main(String[] args) {
        SistemaAgenda sistema = new SistemaAgenda();
        MenuAgenda menu = new MenuAgenda(sistema);
        menu.iniciar();
    }
}
