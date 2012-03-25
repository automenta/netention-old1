/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode.bots;

import afxdeadcode.Community;
import afxdeadcode.PolarTagMatcher;

/**
 *
 * @author SeH
 */
//    public class SmartStupid extends Matcher {
public class RichPoor extends PolarTagMatcher {

    public RichPoor(Community c, long period, long phaseSeconds) {
        super(c, "rich", "poor", period, phaseSeconds);
    }

    @Override
    public String getTweet(String sa, String sb) {
        return sa + " may have #wealth to share with " + sb + " ? " + Community.oneOf("#Generosity", "#Charity", "#Kindness", "#Opportunity", "#NewEconomy", "#Poverty") + " " + Community.oneOf("#Fundraising", "#Philanthropy", "#SocialGood", "#Cause", "#GiveBack", "#HumanRights", "#DoGood") + " ";
        //(includeReportURL ? reportURL : "")
    }

    @Override
    public String[] getSeedQueries() {
        return new String[]{"i splurged", "i spent dollars", "\"i got paid\"", "i am poor", "i am broke", "i need money"};
    }
    
}
