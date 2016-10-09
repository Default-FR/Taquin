public class Control_Gen {
    protected Model m;

    public Control_Gen(Model m){
        this.m = m;

        View v = new View(m);

        ControlMenu ctrlMenu = new ControlMenu(m, v);
        ControlBouton ctrlBouton = new ControlBouton(m, v);

        v.display();
    }
}