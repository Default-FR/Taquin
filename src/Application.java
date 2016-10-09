class Application {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                Model m = new Model();
                Control_Gen controler = new Control_Gen(m);
            }
        });

    }
}