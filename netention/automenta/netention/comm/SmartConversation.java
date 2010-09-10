package automenta.netention.comm;

/**
 *
 * @author seh
 */
public class SmartConversation {

//    Set<String> agents = new HashSet();
//    private Map<String, Integer> wordCounts = new HashMap();
//    private Map<String, String> messageToSender = new HashMap();
//
//    private static enum BooleanState {
//
//        TRUE, FALSE
//    }
//
//    private static enum SeasonState {
//
//        WINTER, SUMMER, SPRING, FALL
//    }
//
//    private static enum AgeState {
//
//        BABY, CHILD, TEENAGER, ADULT, SENIOR
//    }
//
//    private static enum FeverState {
//
//        LOW, NONE, WARM, HOT
//    }
//    private static final Random RANDOM = new Random();
//    
//    MutableBayesianAdjacencyNetwork network;
//
//    public void ExampleBayesianNetwork() {
//        //create nodes
//        BayesianNode<SeasonState> season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER, network);
//        BayesianNode<AgeState> age = new SimpleBayesianNode<AgeState>(AgeState.BABY, network);
//        BayesianNode<BooleanState> stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE, network);
//        BayesianNode<FeverState> fever = new SimpleBayesianNode<FeverState>(FeverState.HOT, network);
//        BayesianNode<BooleanState> tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);
//        BayesianNode<BooleanState> sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);
//
//
//        //add nodes
//        network.add(season);
//        network.add(age);
//        network.add(stuffyNose);
//        network.add(fever);
//        network.add(tired);
//        network.add(sick);
//
//        {
//            //others
////            network.add(new SimpleBayesianNode<SeasonState>(SeasonState.FALL, network));
////            network.add(new SimpleBayesianNode<SeasonState>(SeasonState.SPRING, network));
////            network.add(new SimpleBayesianNode<SeasonState>(SeasonState.SUMMER, network));
//        }
//
//        //connect nodes
//        network.add(new SimpleBayesianEdge<BayesianNode>(season, stuffyNose));
//        network.add(new SimpleBayesianEdge<BayesianNode>(season, fever));
//        network.add(new SimpleBayesianEdge<BayesianNode>(season, tired));
//        network.add(new SimpleBayesianEdge<BayesianNode>(season, sick));
//        network.add(new SimpleBayesianEdge<BayesianNode>(age, stuffyNose));
//        network.add(new SimpleBayesianEdge<BayesianNode>(age, fever));
//        network.add(new SimpleBayesianEdge<BayesianNode>(age, tired));
//        network.add(new SimpleBayesianEdge<BayesianNode>(age, sick));
//        network.add(new SimpleBayesianEdge<BayesianNode>(tired, fever));
//        network.add(new SimpleBayesianEdge<BayesianNode>(tired, stuffyNose));
//        network.add(new SimpleBayesianEdge<BayesianNode>(tired, sick));
//        network.add(new SimpleBayesianEdge<BayesianNode>(stuffyNose, fever));
//        network.add(new SimpleBayesianEdge<BayesianNode>(stuffyNose, sick));
//        network.add(new SimpleBayesianEdge<BayesianNode>(fever, sick));
//
//        //let the network learn
//        for (int sampleCount = 0; sampleCount < 10; sampleCount++) {
//            //sampleState();
//        }
//        //lets check some probabilities
//        final Set<BayesianNode> goals = new HashSet<BayesianNode>();
//        goals.add(sick);
//        final Set<BayesianNode> influences = new HashSet<BayesianNode>();
//        influences.add(fever);
//        sick.setState(BooleanState.TRUE);
//        fever.setState(FeverState.LOW);
//        final double lowPercentage = network.conditionalProbability(goals, influences);
//        fever.setState(FeverState.NONE);
//        final double nonePercentage = network.conditionalProbability(goals, influences);
//        fever.setState(FeverState.WARM);
//        final double warmPercentage = network.conditionalProbability(goals, influences);
//        fever.setState(FeverState.HOT);
//        final double hotPercentage = network.conditionalProbability(goals, influences);
//
//
//        final SeasonState seasonState = (SeasonState.values())[RANDOM.nextInt(SeasonState.values().length)];
//        season.setState(seasonState);
//
//        final AgeState ageState = (AgeState.values())[RANDOM.nextInt(AgeState.values().length)];
//        age.setState(ageState);
//
//        final BooleanState noseState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
//        stuffyNose.setState(noseState);
//
//        final BooleanState tiredState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
//        tired.setState(tiredState);
//
//
//        fever.setState(FeverState.NONE);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.NONE);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.NONE);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.NONE);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.NONE);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//
//        fever.setState(FeverState.LOW);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.LOW);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.LOW);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.LOW);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//        fever.setState(FeverState.LOW);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//
//        fever.setState(FeverState.WARM);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.WARM);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.WARM);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//        fever.setState(FeverState.WARM);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//        fever.setState(FeverState.WARM);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//
//        fever.setState(FeverState.HOT);
//        sick.setState(BooleanState.FALSE);
//        network.learnStates();
//        fever.setState(FeverState.HOT);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//        fever.setState(FeverState.HOT);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//        fever.setState(FeverState.HOT);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//        fever.setState(FeverState.HOT);
//        sick.setState(BooleanState.TRUE);
//        network.learnStates();
//    }
//
//    public SmartConversation() {
//    }
//
//    public void addMessage(String messageID, String senderID, String text, String retweetsMsgID, String repliesToMsgID) {
//        agents.add(senderID);
//        messageToSender.put(messageID, senderID);
//
//        if (retweetsMsgID != null) {
//        }
//    }
//
//    public void clear() {
//        agents.clear();
//
//    }
//
//    public Collection<String> getAgents() {
//        return Collections.unmodifiableSet(agents);
//    }
//
//    public Map<String, Integer> getWordCounts() {
//        return wordCounts;
//    }
//
//    /** key: time period in the future that a response ocurred, value: likelyhood of at least one response */
//    public Map<Double, Double> getProbabilityOfResponseByFuturePeriods() {
//        return new HashMap();
//    }
//
//    /** key: recipient uri, value: likelyhood of reply by them */
//    public Map<String, Double> getProbabilityOfReplyByUsers() {
//        return new HashMap();
//    }
    
}
