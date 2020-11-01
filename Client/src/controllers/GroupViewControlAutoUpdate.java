package controllers;

public class GroupViewControlAutoUpdate implements Runnable{
    GroupViewControl grpView;
    GroupViewControlAutoUpdate(GroupViewControl View) { grpView = View; }
    @Override
    public void run() {
        try {
            while(true){
                Thread.sleep(5000);
                grpView.getMessages();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
