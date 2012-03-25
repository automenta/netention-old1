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
public class HappySad extends PolarTagMatcher {

    public HappySad(Community c, long period, long phaseSeconds) {
        super(c, "happy", "sad", period, phaseSeconds);
        this.numAAgents = 3; //TODO this is a hack
    }

    @Override
    public String[] getSeedQueries() {
        return new String[]{"\"i am happy\"", ":)", "\"i am sad\"", "i am crying"};
    }

    @Override
    public String getTweet(String sa, String sb) {
        return sa + " seem #happy. " + Community.oneOf("So please help", "Will you help") + " " + sb + " who seem #sad ? " + Community.oneOf("#Kindness", "#Health", "#Wisdom", "#Happiness") + " " + Community.oneOf("#SocialGood", "#Cause", "#Volunteer", "#4Change", "#GiveBack", "#DoGood") + " ";
    }
    
}
