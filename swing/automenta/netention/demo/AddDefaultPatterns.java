package automenta.netention.demo;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.NMessage;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.bool.BoolIs;
import automenta.netention.value.integer.IntegerIs;
import automenta.netention.value.node.NodeProp;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.real.RealProp;
import automenta.netention.value.set.SelectionProp;
import automenta.netention.value.string.StringEquals;
import automenta.netention.value.string.StringIs;
import automenta.netention.value.string.StringProp;
import automenta.netention.value.time.TimePointProp;
import automenta.netention.value.uri.URIProp;

public class AddDefaultPatterns {
    public static String web = "Web";

    public AddDefaultPatterns() {
    }

    public void add(MemorySelf s) {
        Pattern thing = s.addPattern(new Pattern("http://www.w3.org/2002/07/owl#Thing").setName("Thing").setIconURL("media://tango32/categories/preferences-system.png").
                setDescription(""));
        {
            s.addProperties(thing,
                    new StringProp("tag", "Tag", 0, -1).setDescription("tag, category, genre"),
                    new URIProp("link", "Link", 0, -1).setDescription("tag, category, genre"));
        }

        Pattern built = s.addPattern(new Pattern("Built").setIconURL("media://tango32/categories/preferences-system.png").
                setDescription("Something that is built or manufactured"));
        {
            s.addProperties(built,
                    new StringProp("manufacturer", "Manufacturer"),
                    new StringProp("serialNumber", "Serial Number"),
                    new TimePointProp("builtWhen", "When Built"),
                    new SelectionProp("condition", "Condition", "New", "Used"));
        }

        s.addPattern(new Pattern("Located").setIconURL("media://tango32/actions/go-jump.png"));
        {
            s.addProperties("Located",
                    new StringProp("currentLocation", "Current Location"));
            
        }

        s.addPattern(new Pattern("Mobile", "Located").setIconURL("media://tango32/places/start-here.png"));
        {
            s.addProperties("Mobile",
                    new StringProp("nextLocation", "Next Location") );
        }

        s.addPattern(new Pattern("Person").setIconURL("media://tango32/apps/system-users.png"));
        {
            s.addProperties("Person",
                    new StringProp("fullName", "Full Name"),
                    new StringProp("biography", "Biography"),
                    new StringProp("emailAddress", "E-Mail"),
                    new StringProp("webAddress", "Website"),
                    new StringProp("birthdate", "Birthdate"),
                    new SelectionProp("gender", "Gender", "male", "female", "other"),
                    new SelectionProp("speaks", "Spoken Language", "English", "Spanish", "Arabic", "Cantonese", "French", "German", "Japanese", "Other"));
        }

        s.addPattern(new Pattern("Project").setIconURL("media://tango32/mimetypes/x-office-presentation.png"));
        {
            s.addProperties("Project",
                    new StringProp("purpose", "Purpose"),
                    new StringProp("goal", "Goal"),
                    new StringProp("member", "Member"));
        }

        s.addPattern(new Pattern("Event").setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
        {
            s.addProperties("Event",
                    new StringProp("startTime", "Start Time"),
                    new StringProp("endTime", "End Time"),
                    new StringProp("location", "Location"));
        }

        {
            Pattern body = s.addPattern(new Pattern("Bodily"));

            Pattern emotion;
            s.addPattern(emotion = new Pattern("Emotion", body.id));
            
            s.addPattern(new Pattern("Ingestion", body.id).setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
            {
                s.addProperties("Ingestion",
                        new SelectionProp("ingestionType", "Type", "Food", "Beverage", "Other"),
                        new StringProp("ingestionType", "Type"),
                        new RealProp("ingestionMass", "Volume"));
            }

            s.addPattern(new Pattern("Excretion", body.id).setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
            {
                s.addProperties("Excretion",
                        new SelectionProp("excretionType", "Type", "Urine", "Feces", "Other"),
                        new RealProp("excretionMass", "Mass"),
                        new RealProp("excretionVolume", "Volume"));
            }

            s.addPattern(new Pattern("Injury", body.id));
            //s.addPattern(new Pattern("Psych").setIconURL("media://tango32/apps/internet-mail.png"));
            {
                StringProp sp = new StringProp("emotion", "Emotion");
                {
                    //http://simple.wikipedia.org/wiki/List_of_emotions


                    /*Robert Plutchik's theory
                    
                    This says that the basic eight emotions are:
                    
                    Fear → feeling afraid. Other words are terror (strong fear), shock, phobia (fear of one thing)
                    Anger → feeling angry. A stronger word is rage. One can be angry with oneself or with others
                    Sorrow → feeling sad. Other words are sadness, grief (a stronger feeling, for example when someone has died) or depression (feeling sad for a long time). Some people think depression is a different emotion.
                    Joy → feeling happy. Other words are happiness, glee (when something good happens to someone. or something bad happens to someone else), gladness.
                    Disgust → feeling something is wrong or dirty
                    Acceptance → accepting a situation
                    Expectation → feeling about something which is going to happen
                    Surprise → how one feels when something happens quickly or when someone did not think it would happen
                     */

                    sp.addSuggestion("joy");
                    sp.addSuggestion("fear");
                    sp.addSuggestion("anger");
                    sp.addSuggestion("sorrow");
                    sp.addSuggestion("disgust");
                    sp.addSuggestion("acceptance");
                    sp.addSuggestion("expectation");
                    sp.addSuggestion("surprise");

                    //http://changingminds.org/explanations/emotions/basic%20emotions.htm
                /* Plutchik 	Acceptance, anger, anticipation, disgust, joy, fear, sadness, surprise
                    Arnold 	Anger, aversion, courage, dejection, desire, despair, fear, hate, hope, love, sadness
                    Ekman, Friesen, and Ellsworth 	Anger, disgust, fear, joy, sadness, surprise
                    Frijda 	Desire, happiness, interest, surprise, wonder, sorrow
                    Gray 	Rage and terror, anxiety, joy
                    Izard 	Anger, contempt, disgust, distress, fear, guilt, interest, joy, shame, surprise
                    James 	Fear, grief, love, rage
                    McDougall 	Anger, disgust, elation, fear, subjection, tender-emotion, wonder
                    Mowrer 	Pain, pleasure
                    Oatley and Johnson-Laird 	Anger, disgust, anxiety, happiness, sadness
                    Panksepp 	Expectancy, fear, rage, panic
                    Tomkins 	Anger, interest, contempt, disgust, distress, fear, joy, shame, surprise
                    Watson 	Fear, love, rage
                    Weiner and Graham 	Happiness, sadness 
                     */

                    //Anger, aversion, courage, dejection, desire, despair, fear, hate, hope, love, sadness
                    sp.addSuggestion("anger");
                    sp.addSuggestion("aversion");
                    sp.addSuggestion("courage");
                    sp.addSuggestion("dejection");
                    sp.addSuggestion("desire");
                    sp.addSuggestion("despair");
                    sp.addSuggestion("fear");
                    sp.addSuggestion("hate");
                    sp.addSuggestion("hope");
                    sp.addSuggestion("love");
                    sp.addSuggestion("sadness");

                }
                sp.setCardinalityMax(-1);
                sp.setCardinalityMin(1);

                s.addProperties(emotion.id, sp);
            }

        }

        s.addPattern(new Pattern("Media").setIconURL("media://tango32/categories/applications-multimedia.png"));
        {
            s.addProperties("Media",
                    new StringProp("url", "URL"));
        }

        
        s.addPattern(new Pattern(NMessage.MessagePattern).setIconURL("media://tango32/apps/internet-mail.png")).setName("Message");
        {
            s.addProperty(new StringProp(NMessage.to, "Recipient"), NMessage.MessagePattern);
            s.addProperty(new StringProp(NMessage.from, "Author"), NMessage.MessagePattern);            
            s.addProperty(new StringProp(NMessage.content, "Content").setRich(true), NMessage.MessagePattern);
        }
        s.addPattern(new Pattern(NMessage.StatusPattern, NMessage.MessagePattern).setIconURL("media://tango32/status/software-update-available.png")).setName("Status"); //nice icon name ;)
        {
        }



        s.addPattern(new Pattern("Business").setIconURL("media://tango32/apps/internet-mail.png"));
        {
            s.addProperties("Business",
                    new StringProp("stockticker", "Stock Ticker"));
        }

        Pattern valueFlow;
        s.addPattern(valueFlow = new Pattern("ValueFlow").setName("Value Flow"));
        {
            s.addProperty(new NodeProp("FlowSource", "Source").setCardinalityMin(1), valueFlow.id);
            s.addProperty(new NodeProp("FlowTarget", "Target").setCardinalityMin(1), valueFlow.id);
        }

        Pattern valueFlowOpen;
        s.addPattern(valueFlowOpen = new Pattern("valueFlowIEML").setName("IEML Value Flow"));
        {
            s.addProperties("valueFlowIEML",
                    new StringProp("openMedium", "Medium"),
                    new StringProp("openSource", "Source"),
                    new StringProp("openDestination", "Destination"),
                    new SelectionProp("seqlayermark", "Sequence Layer Mark", ":", ".", "-", "'", ",", "_", ";"));
        }        
        
        Pattern webPattern;
        s.addPattern(webPattern = new Pattern(web).setName("Web"));
        
        
        //addDefaults(s);
        
    }
    
    @Deprecated public void addDefaults(Self s) {
        Detail d1 = new Detail("Red Bike", Mode.Real, "Built", "Mobile");
        Detail d11 = new Detail("Blue Bike", Mode.Real, "Built");
        Detail d2 = new Detail("Imaginary Bike", Mode.Imaginary, "Mobile", "Built");
        Detail d3 = new Detail("What is Netention?", Mode.Real, "message");
        {
            d1.setID("default1");
            d11.setID("default2");
            d2.setID("default3");
            d3.setID("default4");

            d1.add("numWheels", new IntegerIs(4));
            d1.add("manufacturer", new StringIs("myself"));
            d1.add("wheelRadius", new RealIs(16.0));
            d1.add("hasKickStand", new BoolIs(true));
            //d1.addProperty("anotherObject", new NodeIs(d2.getID()));
            {
                //d11.addProperty("numWheels", new IntegerIs(2));
                d11.add("manufacturer", new StringIs("myself"));
                //d11.addProperty("wheelRadius", new RealIs(16.0));
                //d11.addProperty("hasKickStand", new BoolIs(true));
                //d11.addProperty("anotherObject", new NodeIs(d2.getID()));
            }

        }
        {
            //d2.addProperty("numWheels", new IntegerEquals(4));
            d2.add("manufacturer", new StringEquals("myself"));
            //d2.addProperty("wheelRadius", new RealEquals(16.0));
            //d2.addProperty("hasKickStand", new BoolEquals(true));
            //d2.addProperty("anotherObject", new NodeEquals(d1.getID()));
        }
        {
        }
        s.addDetail(d1);
        s.addDetail(d11);
        s.addDetail(d2);
        s.addDetail(d3);
        
    }
}
//<schema>
//
//	<pattern id="Owned" name="Owned">
//		Something that is or can be owned, and possibly change ownership
//		<node id="owner" class="Being"/>
//		<node id="ownerNext" name="Next Owner" class="Being"/>
//	</pattern>
//
//	<pattern id="Valued" name="Worth Something">
//		Value of something; how much its worth
//	</pattern>
//
//	<pattern id="DollarValued" name="Worth US Dollars ($)">
//		Dollar value
//        <extend id="Valued"/>
//	</pattern>
//
//	<pattern id="ThingValued" name="Worth the Same Value as...">
//		Has a value that is the same as other objects
//        <node id="valuedSimilarlyTo" name="Valued Similarly To" maxMult="-1"/>
//        <extend id="Valued"/>
//	</pattern>
//
//	<pattern id="Rented" name="Rentable">
//		Something that is or can be rented/loaned/leased/borrowed
//        <extend id="Owned"/>
//		<node id="renter" class="Being"/>
//		<node id="renterNext" name="Next Renter" class="Being"/>
//		<real id="dailyCost" name="Daily Cost" unit="currency"/>
//	</pattern>
//
//	<pattern id="Located">
//		Something that has a specific location
//		<geopoint id="location"/>
//	</pattern>
//
//	<pattern id="Mobile">
//		Something that has a specific location, and a possible next location
//		<extend id="Located"/>
//		<geopoint id="locationNext" name="Next Location"/>
//	</pattern>
//
//	<pattern id="Delivered">
//		<extend id="Mobile"/>
//	</pattern>
//
//    <pattern id="Service">
//    </pattern>
//
//	<pattern id="Event">
//		<timepoint id="startTime" name="Start Time"/>
//		<timepoint id="endTime" name="End Time"/>
//	</pattern>
//
//    <pattern id="Reservation">
//        A reservation, ticket, or other reservable activity.  Ex: Restaurants, Shows, etc...
//        <extend id="Event"/>
//    </pattern>
//
//    <pattern id="RestaurantVisit" name="Restaurant Visit">
//            <extend id="Reservation"/>
//    </pattern>
//
//    <pattern id="MovieShowing" name="Movie Showing">
//            <extend id="Reservation"/>
//    </pattern>
//
//    <pattern id="Carpool" name="Carpool">
//            <extend id="Event"/>
//            <extend id="Mobile"/>
//    </pattern>
//
//
//    <pattern id="Gig">
//        Job with fixed start and stop time.
//        <extend id="Event"/>
//        <extend id="Service"/>
//    </pattern>
//
//    <pattern id="Project">
//        Project or goal
//    </pattern>
//
//	<pattern id="PhysicalThing" name="Physical Object">
//		<string id="color" maxMult="-1">
//			Predominant exterior color
//		</string>
//
//		<string id="material" maxMult="-1">
//			Predominant exterior material
//		</string>
//
//		<string id="condition" maxMult="1">
//			Old or new
//			<values>old, new</values>
//		</string>
//
//		<real id="mass" unit="mass"/>
//		<real id="length" unit="distance"/>
//		<real id="width" unit="distance"/>
//		<real id="height" unit="distance"/>
//
//	</pattern>
//
//	<pattern id="Built">
//		Something that is built or manufactured
//		<string id="builder" name="Builder Name"/>
//		<string id="serialID" name="Serial Number"/>
//		<timepoint id="builtWhen" name="When Built"/>
//	</pattern>
//
//	<pattern id="Being">
//		Life form
//	</pattern>
//
//    <pattern id="Animal">
//		<extend id="Being"/>
//    </pattern>
//
//	<pattern id="Human">
//		Human being
//		<extend id="Being"/>
//		<string id="firstName" name="First Name"/>
//		<string id="lastName" name="Last Name"/>
//		<string id="biography"/> <!-- richtext -->
//		<string id="emailAddress" name="EMail Address"/>
//		<string id="webURL" name="Website"/>
//	</pattern>
//
//	<pattern id="Bicycle">
//		<extend id="Built"/>
//		<int id="gearCount" name="Gear Count"/>
//		<real id="wheelDiameter" name="Wheel Diameter" unit="distance"/>
//		<string id="bicycleType" name="Bicycle Type">
//			[ 'mountain',
//       			"street",
//       			"hybrid",
//       			"tricycle",
//       			"unicycle",
//       			"tandem",
//       			"recumbent"
//       		]);
//		</string>
//	</pattern>
//
//	<pattern id="Dwelling">
//		<extend id="Built"/>
//		<int id="numBedrooms" name="Number of Bedrooms"/>
//	</pattern>
//
//	<pattern id="Guitar">
//		<extend id="Built"/>
//    </pattern>
//
//	<pattern id="OnlineAccount" name="Online Account">
//        <string id="login" name="Login" minMult="1" maxMult="1"/>
//        <string type="password" id="password" name="Password" minMult="1" maxMult="1"/>
//    </pattern>
//
//    <pattern id="TwitterAccount" name="Twitter Account">
//        Twitter Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//    <pattern id="FaceBookAccount" name="FaceBook Account">
//        FaceBook Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//    <pattern id="GoogleAccount" name="Google Account">
//        Google Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//
//    <pattern id="MotorVehicle" name="Motor Vehicle">
//        <extend id="Built"/>
//    </pattern>
//
//    <pattern id="Test">Test</pattern>
//
//	<pattern id="testReal" name="Real Test">
//		<extend id="test"/>
//		<real id="rn1"/>
//		<real id="rn2"/>
//		<real id="rn3"/>
//		<real id="rn4"/>
//	</pattern>
//
//	<pattern id="testString" name="String Test">
//		<extend id="test"/>
//		<string id="s1"/>
//		<string id="s2"/>
//		<string id="s3"/>
//		<string id="s4"/>
//	</pattern>
//
//	<pattern id="testInteger" name="Integer Test">
//		<extend id="test"/>
//		<int id="i1"/>
//		<int id="i2"/>
//		<int id="i3"/>
//		<int id="i4"/>
//	</pattern>
//
//	<pattern id="testNode" name="Node Test">
//		<extend id="test"/>
//		<node id="n1"/>
//		<node id="n2"/>
//		<node id="n3"/>
//		<node id="n4"/>
//	</pattern>
//
//
//
//</schema>
//