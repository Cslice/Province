/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Provincial_Miner;

import Provincial_Miner.application.Speaker;
import Provincial_Miner.system.PartialQuebecScraper;
import Provincial_Miner.system.WriteFile;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Stephen
 */
public class UpdateGui implements Runnable {

    String fileName;
    public static Label updateNotification;

    // a new stage for update scene
    Stage window;
    Scene check;
    GridPane stack;
    public static ProgressBar pb;

    boolean running = true;
    boolean sessions = true;

    Label updateLabel = new Label("Update in Progress");
    PartialQuebecScraper scraper = new PartialQuebecScraper();

    //variables used to find the most recent session.
    int session;
    int subsession;

    /**
     * Builds the Gui that is used when the update button is pressed it has a
     * couple labels and a progress bar
     */
    public UpdateGui() {

        window = new Stage();
        stack = new GridPane();
        pb = new ProgressBar();
        check = new Scene(stack, 380, 120);
        stack.setAlignment(Pos.CENTER);
        stack.setHgap(0);
        stack.setVgap(10);
        stack.setPadding(new Insets(25, 25, 25, 25));
        stack.add(updateLabel, 0, 0, 2, 1);
        // new progress bar
        pb.setMinWidth(240);
        stack.add(pb, 0, 2, 2, 1);
        pb.setVisible(false);
        window.setTitle("Update");
        updateNotification = new Label("updating");
        updateNotification.setId("update");
        stack.add(updateNotification, 0, 1, 2, 1);
        // add the stylesheet
        check.getStylesheets().add("fxml.css");
        window.setScene(check);
        window.show();
        window.toFront();
    }

    /**
     * will run through and find and update the most current session
     */
    public void run() {

        //variables for finding the newest session
        String newestSession = new String();

        //number of times a subsession does not exist
        int noDataCount = 0;
        int sessionCounter = 40;

        //while we there is still a new session
        while (noDataCount < 4) {
            noDataCount = 0;

            //loop through the sub sessions
            for (int i = 1; i <= 4; i++) {
                if (scraper.sessionExists(sessionCounter, i)) {
                    //set varaibles to new session
                    session = sessionCounter;
                    subsession = i;

                    //update the displayed message
                    newestSession = sessionCounter + "-" + i;
                    fileName = "Checking " + newestSession;
                } else {
                    noDataCount++;
                }
            }

            //check the next session
            sessionCounter++;

            //update the label
            if (!window.isShowing()) {
                this.kill();
            }
            if (running) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateNotification.setText(fileName);
                    }

                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    updateNotification.setText("Interrupted!");
                    pb.setVisible(false);
                    this.kill();

                }

            } else {
                break;
            }

        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateNotification.setText("Grabbing Data");
                pb.setVisible(true);
            }

        });
        //download the newest session

        ArrayList<Speaker> speakers = scraper.getSession(scraper.getSessionQuery(session, subsession));

        //write the session to an XML file
        new WriteFile().PersonXmlWriter(speakers);

        // if the loop finishes 
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateNotification.setText("Done!");
                pb.setVisible(false);
                updateNotification = null;
                pb = null;

                window.close();
            }

        });
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            this.kill();
        }
    }

    /**
     * change running to false;
     */
    public void kill() {

        this.running = false;
    }
}
