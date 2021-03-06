/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Provincial_Miner.application.Speaker;
import Provincial_Miner.system.PartialQuebecScraper;
import java.util.ArrayList;
import org.testng.annotations.Test;

/**
 *
 * @author Weston Dransfield
 */
public class PartialScraperTest {

    PartialQuebecScraper scraper = new PartialQuebecScraper();
    ArrayList<String> names = scraper.getNames('a', "&Session=jd41l1se", true);

    public PartialScraperTest() {
    }

    
    @Test
    public void getSession() {
        System.out.println(scraper.sessionExists(50, 4));
        System.out.println(scraper.getSessionQuery(34, 1));
        
        ArrayList<Speaker> speakers = scraper.getSession("&Session=jd41l1se");
        
        //display the names
        System.out.println("=====SPEAKERS & TOPICS=====");
        for(Speaker speaker : speakers) {
            System.out.println(speaker.getLastName());
            for(String topic : speaker.getTopics().keySet()) {
                System.out.println("\t + > " + topic);
            }
        }
        
        //disply stats
        System.out.println("=====Stats=====");
        System.out.println("Number of Speakers: " + speakers.size());
        
        //print the names only
        for(Speaker speaker : speakers) {
            System.out.println(speaker.getLastName() + " " + speaker.getFirstName());
        }
        
        
    }
    
 

}
